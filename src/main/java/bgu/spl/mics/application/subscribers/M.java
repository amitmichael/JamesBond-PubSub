package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private LogManager logM = LogManager.getInstance();

	public M() {
		super("M");
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		logM.log.info("Subscriber " + this.getName() + " initialization");
		MessageBrokerImpl.getInstance().register(this);
		subscribeEvent(MissionReceivedEvent.class,c -> {
			// need to implement the callback method
		});

		
	}

}
