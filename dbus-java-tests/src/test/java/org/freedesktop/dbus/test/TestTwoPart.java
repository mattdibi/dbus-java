package org.freedesktop.dbus.test;

import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.test.helper.interfaces.TwoPartInterface;
import org.freedesktop.dbus.test.helper.interfaces.TwoPartObject;
import org.freedesktop.dbus.test.helper.twopart.TwoPartTestClient.TwoPartTestObject;
import org.freedesktop.dbus.test.helper.twopart.TwoPartTestServer;
import org.junit.jupiter.api.Test;

public class TestTwoPart extends AbstractDBusBaseTest {

    private volatile boolean testDone = false;
    private volatile boolean serverReady = false;

    @Test
    public void testTwoPart() throws InterruptedException {
        TwoPartServer twoPartServer = new TwoPartServer();
        twoPartServer.start();

        while (!serverReady) {
            Thread.sleep(1500L);
        }

        try {
            System.out.println("get conn");
            DBusConnection conn = DBusConnectionBuilder.forSessionBus().build();

            System.out.println("get remote");
            TwoPartInterface remote = conn.getRemoteObject("org.freedesktop.dbus.test.two_part_server", "/", TwoPartInterface.class);

            assertNotNull(remote);

            System.out.println("get object");
            TwoPartObject o = remote.getNew();
            assertNotNull(o);

            System.out.println("get name");
            assertEquals("give name", o.getName());

            TwoPartTestObject tpto = new TwoPartTestObject();
            conn.exportObject("/TestObject", tpto);
            conn.sendMessage(new TwoPartInterface.TwoPartSignal("/FromObject", tpto));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }

            if (conn != null) {
                conn.disconnect();
            }
        } catch (DBusException _ex) {
            _ex.printStackTrace();
            fail("Exception in client");
        }
    }

    private class TwoPartServer extends Thread {

        public TwoPartServer() {
            super("TwoPartServerThread");
            setDaemon(true);
        }

        @Override
        public void run() {
            DBusConnection conn;
            try {
                conn = DBusConnectionBuilder.forSessionBus().build();
                conn.requestBusName("org.freedesktop.dbus.test.two_part_server");
                TwoPartTestServer server = new TwoPartTestServer(conn);
                conn.exportObject("/", server);
                conn.addSigHandler(TwoPartInterface.TwoPartSignal.class, server);
                serverReady = true;
                while (!testDone) {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException ex) {
                    }
                }
            } catch (DBusException _ex) {
                logger.error("Exception while running TwoPartServer", _ex);
                throw new RuntimeException("Exception in server");
            }

        }

    }
}
