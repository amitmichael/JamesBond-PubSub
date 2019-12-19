package bgu.spl.mics;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.concurrent.TimeoutException;

public class CallManager implements Runnable {
    private Object c;
    private Callback call;
    private static LogManager logM = LogManager.getInstance();

    public CallManager(Object c, Callback call) {
        this.c = c;
        this.call = call;
    }

    @Override
    public void run() {
        try {
            logM.log.severe("@@@ " +  Thread.currentThread() );
            this.call.call(c);
        } catch (InterruptedException | TimeoutException e) {
        }
    }
}
