package bgu.spl.mics;
import com.google.gson.*;
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
    public static String[] parseSection(String section,String path)  {
        ArrayList toReturn = new ArrayList();
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader(path));
            JsonArray jsonArray = (JsonArray) jsonObject.get(section);
            Iterator<?> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                toReturn.add(iterator.next());
            }
        } catch (FileNotFoundException e){
            logM.log.severe ("File" + path+ " not found");
        }
            String[] arr = new String[toReturn.size()];
        for ( int i=0; i< toReturn.size();i++){
            arr[i] = toReturn.get(i).toString();
        }
            return arr;
        }

    public static void main(String[] args) throws FileNotFoundException {
        Object[] ar = parseSection("inventory", "src/main/java/bgu/spl/mics/input201.json");
        for (Object obj : ar){
            System.out.println(obj);
        }
    }
}
