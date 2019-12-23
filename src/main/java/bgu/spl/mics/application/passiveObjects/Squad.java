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
		logM.log.info("Squad constructor was called");

	}

	public static Squad getInstance() {
		return singletonHolder.squadInstance;
	}
	public Map<String, Agent> getAgents(){
		return this.agents;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		logM.log.info("Squad Load start, agents size: " + this.agents.size());

		for (int i=0; i< agents.length;i++){
			this.agents.put(agents[i].getSerialNumber(),agents[i]);
		}
		logM.log.info("Squad Load finished, agents size: " + this.agents.size());

	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials) {
		synchronized (agents) {
			logM.log.info("&& releaseAgents Start");
			Iterator iter = serials.iterator();
			while (iter.hasNext()) {
				Agent curr =  agents.get(iter.next());
				if (curr!=null)
					curr.release();
			}
			logM.log.info("&& releaseAgents end");
		}
	}


	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time) throws InterruptedException {
			logM.log.info("&& Sending agents to Mission for time: "+ time +" current time : "+ System.currentTimeMillis());
			sleep(time);
			logM.log.info("&& Mission completed current time : "+ System.currentTimeMillis());
			releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) throws InterruptedException {
				Iterator iter = serials.iterator();
				while (iter.hasNext()) {
					String tmp = (String) iter.next();
					Agent next = agents.get(tmp);
					if (next == null) { // agent is not in the squad
						logM.log.severe("agent " + tmp + " is not in the squad");
						return false;
					} else
						next.acquire();
				}
				return true;
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
