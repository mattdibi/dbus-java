package org.freedesktop.dbus.test.helper.callbacks.handler;

import static org.junit.jupiter.api.Assertions.fail;

import java.text.Collator;
import java.util.concurrent.atomic.AtomicInteger;

import org.freedesktop.dbus.exceptions.DBusExecutionException;
import org.freedesktop.dbus.interfaces.CallbackHandler;
import org.freedesktop.dbus.test.helper.SampleException;


/**
 * Callback handler
 */
public class CallbackHandlerImpl implements CallbackHandler<String> {

    private final AtomicInteger testHandleCalls = new AtomicInteger(0);
    private final AtomicInteger testErrorCalls = new AtomicInteger(0);

    private String lastError;

    @Override
    public void handle(String r) {
        testHandleCalls.incrementAndGet();

        System.out.println("Handling callback: " + r);
        Collator col = Collator.getInstance();
        col.setDecomposition(Collator.FULL_DECOMPOSITION);
        col.setStrength(Collator.PRIMARY);
        if (0 != col.compare("This Is A UTF-8 Name: ﺱ !!", r)) {
            lastError = "call with callback, wrong return value";
        }
    }

    @Override
    public void handleError(DBusExecutionException e) {
        testErrorCalls.incrementAndGet();

        System.out.println("Handling error callback: " + e + " message = '" + e.getMessage() + "'");
        if (!(e instanceof SampleException)) {
            fail("Exception is of the wrong sort");
        }
        Collator col = Collator.getInstance();
        col.setDecomposition(Collator.FULL_DECOMPOSITION);
        col.setStrength(Collator.PRIMARY);
        if (0 != col.compare("test", e.getMessage())) {
            lastError = "Exception has the wrong message";
        }
    }

    public int getTestHandleCalls() {
        return testHandleCalls.get();
    }

    public int getTestErrorCalls() {
        return testErrorCalls.get();
    }

    public String getLastError() {
        return lastError;
    }

}