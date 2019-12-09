package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {
	String name;
	String SerialNumber;

	/**
	 * constructor
	 * @param name
	 * @param SerialNumber
	 */
	public Agent(String name, String SerialNumber){
		this.name= name;
		this.SerialNumber = SerialNumber;
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
		// TODO Implement this
		return false;
	}

	/**
	 * Acquires an agent.
	 */
	public void acquire(){
		// TODO Implement this
	}

	/**
	 * Releases an agent.
	 */
	public void release(){
		// TODO Implement this
	}
}
