package bgu.spl.mics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class jsonParser {
    private String fileName;

    public jsonParser(String fileName){
        this.fileName = fileName;
    }

    public void printTofile(List<?> toPrint ) {
        if (!fileName.contains(".json"))
            System.out.println("file name is not from type json");

        else {
            File file = new File(fileName);
            if (file.exists()) // if file with the same name already exists print error
                System.out.println("file name " + fileName + " already exists");
            Gson gson = new GsonBuilder().create();
            try (FileWriter fw = new FileWriter(fileName)) { //write the gadgets to json file
                gson.toJson(toPrint, fw);
            } catch (IOException e) {
            }

        }
    }
}
