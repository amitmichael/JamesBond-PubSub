package bgu.spl.mics.application;

import bgu.spl.mics.*;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.json.Intelligence;

import java.util.*;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new LinkedList<>();
        LogManager logM = LogManager.getInstance();
        if (args.length < 3) {
            logM.log.severe("Enter input&Output json path in program arguments");
        } else {
            //parse the json
            ////////////////////////////////
            JsonParser js = new JsonParser(args[0]);
            List<List<?>> services = js.parseJson();
            Thread t = new Thread(Q.getInstance());
            t.setName("Q");
            threads.add(t);
            if (services.size() < 4)
                logM.log.severe("json did not parse all 4 type of services");
            else {

                // run the executors
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

                sleep(100); //wait to all services finish initialization
                //TimeService
                logM.log.info("Adding " + services.get(3).size() + " executorTime services");
                TimeService timeser = (TimeService) services.get(3).get(0);
                timeser.setThreads(threads);
                Thread t1 = new Thread(timeser);
                t1.setName("TimeService");
                threads.add(t1);

            }


            ////////////////////////////////


            for (Thread tt : threads) {
                tt.start();
            }
            for (Thread tt : threads) {
                try {
                    tt.join();
                } catch (InterruptedException e) {
                }
            }
            Diary.getInstance().printToFile(args[1]);
            Inventory.getInstance().printToFile(args[2]);

        }
    }


}


