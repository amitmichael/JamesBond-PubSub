package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Wrapper;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    Inventory inv;
    @BeforeEach
    public void setUp(){
        Inventory inv = Inventory.getInstance();
        this.inv = inv ;
    }


    @Test
    public void getInstancetest(){
        assertNotEquals(null,inv);
    }


    @Test
    public void loadtest(){
        String[] s =  {"amit","shachaf"};
        inv.load(s);
        assertFalse(inv.getItem(null));
        assertTrue(inv.getItem("amit"));
        assertFalse(inv.getItem("Stam"));
    }


    @Test
    public void printToFiletest1(){ //print to json file and check that file with this name exists
        String[] s =  {"amit","shachaf"};
        inv.load(s);
        String filename = "printtoFileTest1.json";
        File file = new File(filename);
        inv.printToFile(filename);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void printToFiletest2(){ //this test print to json and read the json and compare results
        String filename = "printtoFileTest2.json";
        File file = new File(filename);
        inv.printToFile(filename);
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(filename));
            String[] data = gson.fromJson(reader, String[].class);
            assertTrue(data[0].equals("amit"));
            assertTrue(data[1].equals("shachaf"));
            file.delete();
        } catch (FileNotFoundException e) {}
    }



}
