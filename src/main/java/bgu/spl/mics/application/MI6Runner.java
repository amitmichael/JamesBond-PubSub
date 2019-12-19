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
        if (args.length == 0) {
            logM.log.severe("Enter input json path in program arguments");
        } else {
            //parse the json
            ////////////////////////////////
            JsonParser js = new JsonParser(args[0]);
            List<List<?>> services = js.parseJson();
            threads.add(new Thread(Q.getInstance()));
            if (services.size() < 4)
                logM.log.severe("json did not parse all 4 type of services");
            else {

                // run the executors
                //M
                Iterator itm = services.get(0).iterator(); //iterator on M services
                logM.log.info("Executing " + services.get(0).size() + " M services");
                while (itm.hasNext()) {
                    threads.add(new Thread((Subscriber) itm.next()));
                }

                //MP
                Iterator itmp = services.get(1).iterator(); //iterator on MoneyPenny services
                logM.log.info("Executing " + services.get(1).size() + " MoneyPenny services");
                while (itmp.hasNext()) {
                    //   executorMP.execute((Subscriber) itmp.next());
                    threads.add(new Thread((Subscriber) itmp.next()));

                }


                //Int
                Iterator itint = services.get(2).iterator(); //iterator on Intelligence services
                logM.log.info("Executing " + services.get(2).size() + " Intelligence services");

                while (itint.hasNext()) {
                    threads.add(new Thread((Subscriber) itint.next()));

                }

                sleep(100); //wait to all services finish initialization
                //TimeService
                logM.log.info("Executing " + services.get(3).size() + " executorTime services");
                //   executorTimeService.execute((Publisher) services.get(3).get(0));
                TimeService timeser = (TimeService) services.get(3).get(0);
                timeser.setThreads(threads);
                threads.add(new Thread(timeser));

            }


            ////////////////////////////////


            for (Thread t : threads) {
                t.start();
            }
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                }
            }
            Diary.getInstance().printToFile("diaryTest.json");

        }
    }


}


