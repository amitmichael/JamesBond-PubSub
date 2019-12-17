package bgu.spl.mics.application;

import bgu.spl.mics.*;
import bgu.spl.mics.Future;
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
        LogManager logM = LogManager.getInstance();
        if (args.length == 0) {
            logM.log.severe("Enter input json path in program arguments");
        } else {
            //parse the json

            JsonParser js = new JsonParser(args[0]);
            List<List<?>> services = js.parseJson();

            ExecutorService executorSingeltons = Executors.newFixedThreadPool(1);
            executorSingeltons.execute(Q.getInstance());;


            if (services.size() < 4)
                logM.log.severe("json did not parse all 4 type of services");
            else {

                // run the executors
                //M
                ExecutorService executorM = Executors.newFixedThreadPool(services.get(0).size());
                Iterator itm = services.get(0).iterator(); //iterator on M services
                logM.log.info("Executing " + services.get(0).size() + " M services");
                while (itm.hasNext()) {
                    executorM.execute((Subscriber) itm.next());
                }
                executorM.shutdown();

                //MP
                ExecutorService executorMP = Executors.newFixedThreadPool(services.get(1).size());
                Iterator itmp = services.get(1).iterator(); //iterator on MoneyPenny services
                logM.log.info("Executing " + services.get(1).size() + " MoneyPenny services");
                while (itmp.hasNext()) {
                    executorMP.execute((Subscriber) itmp.next());
                }
                executorMP.shutdown();


                //Int
                ExecutorService executorInt = Executors.newFixedThreadPool(services.get(2).size());
                Iterator itint = services.get(2).iterator(); //iterator on Intelligence services
                logM.log.info("Executing " + services.get(2).size() + " Intelligence services");

                while (itint.hasNext()) {
                    executorInt.execute((Subscriber) itint.next());
                }
                executorInt.shutdown();

                sleep(100); //wait to all services finish initialization
                //TimeService
                ExecutorService executorTimeService = Executors.newFixedThreadPool(services.get(3).size());
                logM.log.info("Executing " + services.get(3).size() + " executorTime services");
                executorTimeService.execute((Publisher) services.get(3).get(0));
                executorTimeService.shutdown();

            }


/*
            Moneypenny mp1 = new Moneypenny("1");
            Moneypenny mp2 = new Moneypenny("2");
            Thread t2 = new Thread(mp1);
            Thread t3 = new Thread(mp2);
            M m2 = new M("M2");
            List<String> l1 = new LinkedList<>();
            l1.add("006");
            t2.setName("MP1");
            t3.setName("MP2");
            t2.start();
            t3.start();
            AgentsAvailableEvent event1 = new AgentsAvailableEvent("",l1);
            AgentsAvailableEvent event2 = new AgentsAvailableEvent("",l1);
            MessageBrokerImpl.getInstance().getResultMap().put(event1,new Future<>());
            MessageBrokerImpl.getInstance().getResultMap().put(event2,new Future<>());
            sleep(100);

            m2.getSimplePublisher().sendEvent(event1);
            m2.getSimplePublisher().sendEvent(event2);
*/
        }
    }
}


