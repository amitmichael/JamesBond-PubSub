package bgu.spl.mics.application;

import bgu.spl.mics.JsonParser;

import static java.lang.Thread.sleep;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws InterruptedException {
        // TODO Implement this
        if (args.length ==0) {
            throw new NullPointerException("Enter input json path in program arguments");
        } else {
            JsonParser js = new JsonParser(args[0]);
            js.parseJson();

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
            event1.setFuture(new Future<>());

            sleep(100);

            m2.getSimplePublisher().sendEvent(event1);
            m2.getSimplePublisher().sendEvent(event2);
            */
        }
    }
}
