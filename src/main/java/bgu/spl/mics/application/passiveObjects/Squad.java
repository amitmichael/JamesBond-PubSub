package bgu.spl.mics.application.passiveObjects;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents; //key serial number, value agent
	private static Squad squadInstance = null;

	/**
	 * Retrieves the single instance of this class.
	 */

	private Squad(){
		agents = new HashMap<String, Agent>();
	}

	public static Squad getInstance() {
		if (squadInstance == null)
			squadInstance = new Squad();

		return squadInstance;
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
		// TODO Implement this
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		// TODO Implement this
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		// TODO Implement this
		return false;
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
