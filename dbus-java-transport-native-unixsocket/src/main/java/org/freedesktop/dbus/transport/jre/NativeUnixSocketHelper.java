package org.freedesktop.dbus.transport.jre;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.attribute.UserPrincipal;

import jdk.net.ExtendedSocketOptions;
import jdk.net.UnixDomainPrincipal;

public class NativeUnixSocketHelper {

    /**
     * Get the UID of peer credentials.
     *
     * @param _sock socket to read from
     * @return UID, -1 if given {@link SocketChannel} was null
     *
     * @throws IOException when socket channel fails to read SO_PEERCRED option
     */
    public static int getUid(SocketChannel _sock) throws IOException {
        if (_sock == null) {
            return -1;
        }

        UnixDomainPrincipal creds = _sock.getOption(ExtendedSocketOptions.SO_PEERCRED);
        UserPrincipal user = creds.user();

        return Integer.parseInt(user.getName());
    }

}
