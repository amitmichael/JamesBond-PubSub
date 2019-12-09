package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        // TODO Implement this


        if (args.length >0){
           try {
               JsonReader reader = new JsonReader(new FileReader(args[0]));
               //String[] toLoad = new Gson().fromJson(reader,String[].class);
           } catch (FileNotFoundException e){System.out.println("FileNotFound");}
        }
        else {
            System.out.println("No arguments supplied");
        }
    }
}
