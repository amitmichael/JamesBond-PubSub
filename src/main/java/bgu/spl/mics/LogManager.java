package bgu.spl.mics;
import bgu.spl.mics.application.subscribers.Q;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
    public Logger log;
    private static class singletonHolder{
        private static LogManager logInstance = new LogManager("SPL2Log.log");}
    FileHandler fh;

    private LogManager(String path){
        log = Logger.getLogger("testtest");

    try {

        // This block configure the logger with handler and formatter
        try {
            File file = new File (path);
            fh = new FileHandler(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        log.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        log.setUseParentHandlers(false);

        // the following statement is used to log any messages
        log.info("log start");


    } catch (SecurityException e) {
        e.printStackTrace();
    }

}
    public static LogManager getInstance() {
        return singletonHolder.logInstance;
    }
}
