package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.LogManager;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private List<MissionInfo> missions;
	private LogManager logM = LogManager.getInstance();

	public Intelligence(String name,List<MissionInfo> m) {
		super(name);
		missions = m;
		logM.log.info(name + " created with " + m.size() + " missions");

		// TODO Implement this
	}

	@Override
	protected void initialize() {
		// TODO Implement this
	}



}
