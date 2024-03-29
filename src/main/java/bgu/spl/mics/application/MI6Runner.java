package bgu.spl.mics.application;

import bgu.spl.mics.JsonParser;
import bgu.spl.mics.LogManager;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Q;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    private static long start = System.currentTimeMillis();
    private static List<Thread> threads = new LinkedList<>();
    private  static LogManager logM = LogManager.getInstance();


    public static void main(String[] args) throws InterruptedException {
        logM.log.info("Program starts " + start);
        if (args.length < 3) {
            logM.log.severe("Enter input&Output json path in program arguments");
        } else {
            load(args[0]);


            for (Thread tt : threads) {
                tt.start();
            }
            for (Thread tt : threads) {
                try {
                    tt.join();
                } catch (InterruptedException e) {
                }
            }
            Inventory.getInstance().printToFile(args[1]);
            Diary.getInstance().printToFile(args[2]);


            long end = System.currentTimeMillis();
            logM.log.info("Program end " + end);
            logM.log.info("Program duration " + Math.subtractExact(end, start));

        }
    }

    private static void load(String s) throws InterruptedException {
        //parse the json
        JsonParser js = new JsonParser(s);
        List<List<?>> services = null;
        try {
            services = js.parseJson();
        } catch (FileNotFoundException e ){

            logM.log.severe("File " + s + " not found");
            System.out.println("File " + s + " not found");
            return;

        }
        Thread t = new Thread(Q.getInstance());
        t.setName("Q");
        threads.add(t);
        if (services.size() < 4)
            logM.log.severe("json did not parse all 4 type of services");
        else {

            //M
            Iterator itm = services.get(0).iterator(); //iterator on M services
            logM.log.info("Adding " + services.get(0).size() + " M services");
            while (itm.hasNext()) {
                Subscriber curr = (Subscriber) itm.next();
                Thread t1 = new Thread(curr);
                t1.setName(curr.getName());
                threads.add(t1);
            }

            //MP
            Iterator itmp = services.get(1).iterator(); //iterator on MoneyPenny services
            logM.log.info("Adding " + services.get(1).size() + " MoneyPenny services");
            while (itmp.hasNext()) {
                Subscriber curr = (Subscriber) itmp.next();
                Thread t1 = new Thread(curr);
                t1.setName(curr.getName());
                threads.add(t1);

            }

            //Int
            Iterator itint = services.get(2).iterator(); //iterator on Intelligence services
            logM.log.info("Adding " + services.get(2).size() + " Intelligence services");

            while (itint.hasNext()) {
                Subscriber curr = (Subscriber) itint.next();
                Thread t1 = new Thread(curr);
                t1.setName(curr.getName());
                threads.add(t1);

            }

            //TimeService
            logM.log.info("Adding " + services.get(3).size() + " executorTime services");
            TimeService timeser = (TimeService) services.get(3).get(0);
            Thread t1 = new Thread(timeser);
            t1.setName("TimeService");
            threads.add(t1);

        }

    }

}


