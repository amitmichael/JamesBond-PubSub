package bgu.spl.mics.application;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws InterruptedException {
        // TODO Implement this
        Inventory inv = Inventory.getInstance();
        if (args.length ==0) {
            throw new NullPointerException("Enter input json path in program arguments");
        } else {
            String[] arr = jsonHandler.parseInventory("inventory", args[0]); //"src/main/java/bgu/spl/mics/input201.json"
            inv.load(arr);
            //TimeService tm = new TimeService(5);
            //tm.run();
  /*
            Q q1 = Q.getInstance();
            M m1 = new M();
            GadgetAvailableEvent event= new GadgetAvailableEvent("Sky Hook");
            event.setFuture(new Future());
            Thread t1 = new Thread(q1);
            t1.start();
            sleep(100);
            m1.getSimplePublisher().sendEvent(event);

*/
            Agent ag1 = new Agent("Amit","007");
            Agent[] arr1 = new Agent[1];
            arr1[0] = ag1;
            Squad.getInstance().load(arr1);
            Moneypenny mp1 = new Moneypenny("1");
            Thread t2 = new Thread(mp1);
            M m2 = new M();
            List<String> l1 = new LinkedList<>();
            l1.add("006");
            t2.start();
            AgentsAvailableEvent event1 = new AgentsAvailableEvent("",l1);
            event1.setFuture(new Future());

            sleep(100);

            m2.getSimplePublisher().sendEvent(event1);

        }
    }
}
