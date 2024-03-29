package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.json.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JsonParser {
    private String fileName;
    private static LogManager logM = LogManager.getInstance();



    public  JsonParser(String fileName){
        this.fileName = fileName;
    }


    public void printToFileDiary(Diary d) {
        if (!fileName.contains(".json"))
            logM.log.severe("file name is not from type json");

        else {
            ToFile print = new ToFile();
            print.setReports(d.getReports());
            print.setTotal((d.getTotal().get()));
            printTofile(print);
        }
    }

    public void printTofile(Object toPrint) {
        if (!fileName.contains(".json"))
            logM.log.severe("file name is not from type json");

        else {
            File file = new File(fileName);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter fw = new FileWriter(fileName)) { //write the gadgets to json file
                logM.log.info("File " + fileName + " Created");
                gson.toJson(toPrint, fw);
            } catch (IOException e) {
                logM.log.severe("printTofile end with exception");
            }

        }
    }
    public List<List<?>>  parseJson() throws FileNotFoundException {

        LinkedList<List<?>> toReturn = new LinkedList();


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
            LinkedList<Subscriber> listM = new LinkedList<>();
            int numofServicesM = event.getServices().getM();
            logM.log.info("Creating " + numofServicesM + " M Services");
            for (int j=0; j< numofServicesM; j++){
                listM.add(new M("M"+ (j+1),(j+1)));
            }
            toReturn.add(listM);

            //MoneyPenny
            LinkedList<Subscriber> listMP = new LinkedList<>();
            int numofServicesMP = event.getServices().getMoneypenny();
            logM.log.info("Creating " + numofServicesMP + " Moneypenny Services");
            for (int j=0; j< numofServicesMP; j++){
                listMP.add(new  Moneypenny(""+(j+1)));
            }
            toReturn.add(listMP);


            //Intelligence
            LinkedList<Subscriber> listint = new LinkedList<>();
            List<Intelligence> in = event.getServices().getIntelligence();
            int numofServicesInt = in.size();
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
                listint.add(new bgu.spl.mics.application.subscribers.Intelligence("Intelligence" + (k+1),m1));
                k++;
            }
            toReturn.add(listint);

            //TimeService
            logM.log.info("Creating " + 1 + " TimeService Services");
            LinkedList<Publisher> listTime = new LinkedList<>();
            listTime.add(new TimeService(event.getServices().getTime()));
            toReturn.add(listTime);

            logM.log.info("Json parse finished successfully ");



            return toReturn;
    }



}
