package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.LogManager;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {
	private String name;
	private String SerialNumber;
	private boolean available;
	private LogManager logM = LogManager.getInstance();


	/**
	 * constructor
	 * @param name
	 * @param SerialNumber
	 */
	public Agent(String name, String SerialNumber){
		this.name= name;
		this.SerialNumber = SerialNumber;
		available =true;
	}

	/**
	 * Sets the serial number of an agent.
	 */
	public void setSerialNumber(String serialNumber) {
		this.SerialNumber=serialNumber;
	}

	/**
     * Retrieves the serial number of an agent.
     * <p>
     * @return The serial number of an agent.
     */
	public String getSerialNumber() {

		return this.SerialNumber;
	}

	/**
	 * Sets the name of the agent.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * Retrieves the name of the agent.
     * <p>
     * @return the name of the agent.
     */
	public String getName() {
		return this.name;
	}

	/**
     * Retrieves if the agent is available.
     * <p>
     * @return if the agent is available.
     */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * Acquires an agent.
	 */
	public void acquire()  {
		synchronized (this) {
			try {
				while (available == false) {
					logM.log.info("Waiting for agent " + this.name + " to be available");
					wait();
				}

			}
			catch(InterruptedException e){
				logM.log.severe("agent " + this.name + " interrupt");

			}
			logM.log.info("agent " + this.name + " acquired");
			available = false;
		}
	}

	/**
	 * Releases an agent.
	 */
	public void release(){
		synchronized (this) {
			if (available == true)
				logM.log.warning("release was called to available agent, agent:  " + name);
			else {
			available = true;
				logM.log.info("Agent " + name + " released");
				notifyAll();
		}
	}
}
}
