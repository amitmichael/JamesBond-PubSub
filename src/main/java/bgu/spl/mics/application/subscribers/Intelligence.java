package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.events.TickBroadcast;

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
	private long timeTick;
	private long nextMissionTime;

	public Intelligence(String name, List<MissionInfo> m) {
		super(name);
		missions = m;
		timeTick = 0;
		if (missions.size()>0)
			nextMissionTime = missions.get(0).getTimeIssued();
		logM.log.info(name + " created with " + m.size() + " missions");
	}


	@Override
	protected synchronized void initialize()   {
		logM.log.info("Subscriber " + this.getName() + " initialization");
		MessageBrokerImpl.getInstance().register(this);
		subscribeToTickBroadCastEvent();
	}

	private void sendMissions(MissionInfo m) throws InterruptedException {
		getSimplePublisher().sendEvent(new MissionReceivedEvent("MRE",m));
		logM.log.info("Time: " + timeTick + " Msg: Sending MRE for mission " + m.getMissionName() + " time issued: " + m.getTimeIssued());
		missions.remove(m);
		if (missions.size()>0)
			nextMissionTime = missions.get(0).getTimeIssued();
	}

	private void subscribeToTickBroadCastEvent(){
		Callback tickCallBack = new Callback() {
			@Override
			public void call(Object c) throws InterruptedException {
				synchronized (this) {
					if (c instanceof TickBroadcast) {
						TickBroadcast msg = (TickBroadcast) c;
						timeTick = msg.getTime();
						while (timeTick == nextMissionTime & missions.size()>0) {
							sendMissions(missions.get(0));
						}
					} else {
						logM.log.severe(getName() + " received Broadcastmsg not from Tick type, type was: " + c.getClass());
					}
				}
			}
		}	;
		subscribeBroadcast(TickBroadcast.class,tickCallBack);

	}

}

