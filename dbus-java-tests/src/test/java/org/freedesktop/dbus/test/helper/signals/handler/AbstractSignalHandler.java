package org.freedesktop.dbus.test.helper.signals.handler;

import java.util.concurrent.atomic.AtomicInteger;

import org.freedesktop.dbus.interfaces.DBusSigHandler;
import org.freedesktop.dbus.messages.DBusSignal;
import org.opentest4j.AssertionFailedError;

/**
 * Base class for all signals which are tested.
 * @author hypfvieh
 * @since v3.0.0 - 2018-02-26
 */
public abstract class AbstractSignalHandler<T extends DBusSignal> implements DBusSigHandler<T> {
    private final AtomicInteger testRuns = new AtomicInteger(0);

    private final int expectedRuns;

    protected AssertionFailedError assertionError;

    public AbstractSignalHandler(int _expectedRuns) {
        expectedRuns = _expectedRuns;
    }

    /** Implemented by subclasses */
    protected abstract void handleImpl(T _s);

    /** Check that we do no run to often, then call handleImpl to do the real work. */
    @Override
    public final void handle(T _s) { // should not be implemented by subclasses
        getTestRuns().incrementAndGet();

        setFailed(getExpectedRuns() > getActualTestRuns(), "Signal received to often.");

        System.out.println(getClass().getSimpleName() + " running");

        handleImpl(_s);
    }

    protected AtomicInteger getTestRuns() {
        return testRuns;
    }

    public int getActualTestRuns() {
        return testRuns.get();
    }

    public int getExpectedRuns() {
        return expectedRuns;
    }

    public void getAssertionError() {
        if (assertionError != null) {
            throw assertionError;
        }
    }

    protected void setFailed(boolean _condition, String _message) {
        if (_condition) {
            assertionError = new AssertionFailedError(_message);
            throw assertionError;
        }
    }

    protected void setFailed(boolean _condition, String _message, Exception _ex) {
        if (_condition) {
            assertionError = new AssertionFailedError(_message, _ex);
            throw assertionError;
        }
    }

}
