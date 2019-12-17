package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static bgu.spl.mics.MessageBrokerImpl.getInstance;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private LogManager logM = LogManager.getInstance();
	int timeTick;
	private Diary diary = Diary.getInstance();
	private Report report;


	public M(String name) {
		super(name);
		timeTick = 0;
	}


	@Override
	protected synchronized void initialize() {
		logM.log.info("Subscriber " + this.getName() + " initialization");
		getInstance().register(this);
		subscribeToTickBroadCastEvent();
		subscribeMissionRecievedEvent();
	}

	private void subscribeMissionRecievedEvent() {
		Callback back = new Callback() {
			@Override
			public void call(Object c) throws InterruptedException {
				if (c instanceof MissionReceivedEvent) {
					MissionReceivedEvent event = (MissionReceivedEvent) c;
					int timeExpired = event.getInfo().getTimeExpired();
					logM.log.info("new TimeExpired assigned");
					GadgetAvailableEvent eventG = new GadgetAvailableEvent(event.getInfo().getGadget());
					AgentsAvailableEvent eventA = new AgentsAvailableEvent(event.getInfo().getSerialAgentsNumbers());
					Future fut1 = getSimplePublisher().sendEvent(eventG);
					logM.log.info("Subscriber " + getName() + " sending EventG");
					Future fut2 = getSimplePublisher().sendEvent(eventA);
					logM.log.info("Subscriber " + getName() + " sending EventA");
					String result1 = (String) fut1.get((timeExpired-timeTick)*100, TimeUnit.MILLISECONDS);
					String result2 = (String) fut2.get((timeExpired-timeTick)*100, TimeUnit.MILLISECONDS);
					try {
						if (result1.equals("true") & result2.equals("true")) {
							if (timeTick <= timeExpired) {
								MessageBrokerImpl.getInstance().sendEvent(new ExcuteMission());
								logM.log.info("Subscriber " + getName() + " sending ExcuteMission");
							} else {
								MessageBrokerImpl.getInstance().sendEvent(new AbortMission(event.getInfo()));
								//String result4=(String) fut4.get();
								logM.log.warning("Subscriber " + getName() + " sending AbortMission due to time expired");
							}
						}
						else {
							MessageBrokerImpl.getInstance().sendEvent(new AbortMission(event.getInfo()));
							logM.log.warning("Subscriber " + getName() + " sending AbortMission due to missing condition");
						}
					}catch (NullPointerException e){logM.log.severe( "Time: "+ timeTick + " " + event.getInfo().getMissionName() + " futur1 " + (fut1==null) + " futuer2 " + (fut2==null));}
					MessageBrokerImpl.getInstance().complete(event, "true");


				} else {
					logM.log.warning("call is not of type MissionReceivedEvent");

				}
			}

		};
		subscribeEvent(MissionReceivedEvent.class, back);
	}

	private void subscribeToTickBroadCastEvent() {
		Callback tickCallBack = new Callback() {
			@Override
			public void call(Object c) {
				synchronized (this) {
					if (c instanceof TickBroadcast) {
						TickBroadcast msg = (TickBroadcast) c;
						timeTick = msg.getTime();
					} else {
						logM.log.severe(getName() + " received Broadcastmsg not from Tick type, type was: " + c.getClass());
					}
				}
			}
		};
		subscribeBroadcast(TickBroadcast.class, tickCallBack);
	}
}

