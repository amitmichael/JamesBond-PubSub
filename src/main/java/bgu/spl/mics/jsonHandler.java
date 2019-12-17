package bgu.spl.mics;
import bgu.spl.mics.json.JsonEvent;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class jsonHandler {
    private String fileName;
    private static LogManager logM = LogManager.getInstance();



    public jsonHandler(String fileName){
        this.fileName = fileName;
    }

    public void printTofile(List<?> toPrint ) {
        if (!fileName.contains(".json"))
            logM.log.severe("file name is not from type json");

        else {
            File file = new File(fileName);
            if (file.exists()) // if file with the same name already exists print error
                logM.log.warning("file name " + fileName + " already exists");
            Gson gson = new GsonBuilder().create();
            try (FileWriter fw = new FileWriter(fileName)) { //write the gadgets to json file
                logM.log.info("File " + fileName + " Created");
                gson.toJson(toPrint, fw);
            } catch (IOException e) {
                logM.log.severe("printTofile end with exception");
            }

        }
    }
    public static String[] parseInventory(String section,String path) {

//        ArrayList toReturn = new ArrayList();
        try {
//            JsonParser jsonParser = new JsonParser();
//            JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader(path));
//            JsonArray jsonArray = (JsonArray) jsonObject.get(section);
//            Iterator<?> iterator = jsonArray.iterator();
//            while (iterator.hasNext()) {
//                toReturn.add(iterator.next());
//            }
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(path));
            JsonEvent event = gson.fromJson(reader, JsonEvent.class);
            System.out.println(event);
        } catch (FileNotFoundException e) {
            logM.log.severe("File" + path + " not found");
        }
//            String[] arr = new String[toReturn.size()];
//        for ( int i=0; i< toReturn.size();i++){
//            String tmp = (toReturn.get(i).toString());
//            arr[i] =  (tmp).substring(1,tmp.length()-1);
//        }
//            return arr;
//        }
            return null;
    }



    public static void main(String[] args) throws FileNotFoundException {
        Object[] ar = parseInventory("inventory", "src/main/java/bgu/spl/mics/input201.json");
        for (Object obj : ar){
            System.out.println(obj);
        }
    }
}
