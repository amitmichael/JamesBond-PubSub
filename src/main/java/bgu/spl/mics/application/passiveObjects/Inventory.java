package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.LogManager;
import bgu.spl.mics.jsonHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private List<String> gadgets;
	private static class singletonHolder{
	private static Inventory inventoryInstance = new Inventory();}
	private LogManager logM = LogManager.getInstance();

	/**
	 * constructor
	 */

	private Inventory(){
		gadgets = new LinkedList<String>();
		logM.log.info("Inventory constructor was called");
	}

	/**
     * Retrieves the single instance of this class.
     */

	public static Inventory getInstance() {
		return singletonHolder.inventoryInstance;
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {
		logM.log.info("Inventory Load start, gadget size: " + this.gadgets.size());

		for (int i=0;i < inventory.length; i++){
			gadgets.add(inventory[i]);
		}
		logM.log.info("Inventory Load Finished, gadget size: " + this.gadgets.size());

	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public boolean getItem(String gadget){
		if (gadget != null) {
			return gadgets.remove(gadget);
		}
		else return false;
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Gadget> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){

		jsonHandler json = new jsonHandler(filename);
		json.printTofile(gadgets);

	}
}
