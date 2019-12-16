package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.LogManager;

import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents; //key serial number, value agent
	private static class singletonHolder{
	private static Squad squadInstance = new Squad();}
	private LogManager logM = LogManager.getInstance();


	/**
	 * Retrieves the single instance of this class.
	 */

	private Squad(){
		agents = new HashMap<String, Agent>();
	}

	public static Squad getInstance() {
		return singletonHolder.squadInstance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (int i=0; i< agents.length;i++){
			this.agents.put(agents[i].getSerialNumber(),agents[i]);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		Iterator iter=serials.iterator();
		while (iter.hasNext()){
			agents.get(iter.next()).release();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time) throws InterruptedException { //@amit added that exception
		if(getAgents(serials))
			sleep(time);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		boolean done=true;
		Iterator iter=serials.iterator();
		while (iter.hasNext()&done){
			Agent next=agents.get(iter.next());
			if (!agents.get(next).isAvailable())
				done=false;
			else next.acquire();
		}
		return done;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
        List<String> copied = serials;
    	List<String> toReturn = new LinkedList<String>();
        for (int i=0; i < copied.size();i++){
        	String curr = copied.get(i);
        	if (agents.containsKey(curr)){
        		toReturn.add(agents.get(curr).getName());
			}
		}
	    return toReturn;
    }

}
