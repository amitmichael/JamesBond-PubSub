package bgu.spl.mics.application;

import bgu.spl.mics.LogManager;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.jsonHandler;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        // TODO Implement this
        Inventory inv = Inventory.getInstance();
        if (args[0] == null) {
            throw new NullPointerException("Enter input json path in program arguments");
        } else {
            String[] arr = jsonHandler.parseSection("inventory", args[0]); //"src/main/java/bgu/spl/mics/input201.json"
            inv.load(arr);
            TimeService tm = new TimeService(10);
            tm.run();

        }
    }
}
