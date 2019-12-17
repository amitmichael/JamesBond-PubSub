package bgu.spl.mics;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.json.Intelligence;
import bgu.spl.mics.json.JsonEvent;
import bgu.spl.mics.json.Mission;
import bgu.spl.mics.json.Squad;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class JsonParser {
    private String fileName;
    private static LogManager logM = LogManager.getInstance();



    public JsonParser(String fileName){
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
    public void  parseJson() {

        try {
            logM.log.info("Starting parsing json");
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(this.fileName));
            JsonEvent event = gson.fromJson(reader, JsonEvent.class);
            //Inventory
            Inventory.getInstance().load(event.getInventory());

            // Squad
            Agent[] agentload = new Agent[event.getSquad().size()];
            Iterator it = event.getSquad().iterator();
            int i=0;
            while (it.hasNext()){
                Squad curr = (Squad) it.next();
                agentload[i] = new Agent(curr.getName(),curr.getSerialNumber());
                i++;
            }
            bgu.spl.mics.application.passiveObjects.Squad.getInstance().load(agentload);

            //Services
            //M
            int numofServicesM = event.getServices().getM();
            ExecutorService executorM = Executors.newFixedThreadPool(numofServicesM);
            logM.log.info("Creating " + numofServicesM + " M Services");
            for (int j=0; j< event.getServices().getM(); j++){
                M tmp = new M("M" + j);
                // need to run threads
                executorM.execute(tmp);
            }

            //MoneyPenny
            int numofServicesMP = event.getServices().getMoneypenny();
            ExecutorService executorMP = Executors.newFixedThreadPool(numofServicesMP);
            logM.log.info("Creating " + numofServicesMP + " Moneypenny Services");
            for (int j=0; j< event.getServices().getMoneypenny(); j++){
                Moneypenny tmp1 = new  Moneypenny(""+j);
                // need to run threads
                executorMP.execute(tmp1);

            }

            sleep(100); // wait for M and MP to finish initialization

            //Intelligence
            List<Intelligence> in = event.getServices().getIntelligence();
            int numofServicesInt = in.size();
            ExecutorService executorInt = Executors.newFixedThreadPool(numofServicesInt);
            logM.log.info("Creating " + numofServicesInt + " Intelligence Services");
            Iterator it1 = in.iterator();
            int k=0;
            while (it1.hasNext()){
                Intelligence curr = (Intelligence) it1.next();
                List<MissionInfo> m1 = new LinkedList<>();
                List<Mission> toCopy =  curr.getMissions();
                Iterator itcopy = toCopy.iterator();
                while (itcopy.hasNext()){
                    m1.add(new MissionInfo((Mission) itcopy.next()));
                }
                bgu.spl.mics.application.publishers.Intelligence tmp2 = new bgu.spl.mics.application.publishers.Intelligence("Intelligence" + k,m1);
                executorInt.execute(tmp2);
                k++;
                // need to run threads
            }
            logM.log.info("Json parse finished successfully ");


        } catch (FileNotFoundException | InterruptedException e) {
            logM.log.severe("File" + this.fileName + " not found");
        }


    }


    public static void main(String[] args) throws FileNotFoundException {

    }
}
