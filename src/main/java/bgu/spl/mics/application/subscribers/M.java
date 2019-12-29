package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import bgu.spl.mics.events.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static bgu.spl.mics.MessageBrokerImpl.getInstance;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private LogManager logM = LogManager.getInstance();

	private int timeTick;
	private int serialNumber;
	private Diary diary = Diary.getInstance();
	private Report report;


	public M(String name,int serialNumber) {
		super(name);
		this.serialNumber = serialNumber;
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
			public void call(Object c) {
				Long start = System.currentTimeMillis();
				Diary.getInstance().increment(); //increment the total
				logM.log.info("MissionRecieve Callback Start " + start);
				if (c instanceof MissionReceivedEvent) {
					MissionReceivedEvent event = (MissionReceivedEvent) c;
					logM.log.info("Time: " + timeTick + " " + getName() + " Handle mission " + event.getInfo().getMissionName() + " time  issued " + event.getInfo().getTimeIssued());
					int timeExpired = event.getInfo().getTimeExpired();

					logM.log.info(getName() + " Time: " + timeTick + " " + "new TimeExpired assigned");

					/////////////try to acquire the agent////////////////////
					if (timeTick<timeExpired) {
						Future futAgent = getSimplePublisher().sendEvent(new AgentsAvailableEvent(event.getInfo().getSerialAgentsNumbers(),(timeExpired-timeTick)*100,event.getInfo().getMissionName()));
						logM.log.info(getName() + " Time: " + timeTick + " " + "Subscriber " + getName() + " sending EventA");
						try {
							logM.log.info(getName() + ": time left  to process: " + event.getInfo().getMissionName() + " : " + (timeExpired - timeTick) * 100);
							String resultAgent = (String) futAgent.get((timeExpired - timeTick) * 100, TimeUnit.MILLISECONDS);

							/////////////try to acquire the gadget//////////////////
							Future futGadget = null;
							String resultGadget = null;
							if (resultAgent != null) {
								futGadget = getSimplePublisher().sendEvent(new GadgetAvailableEvent(event.getInfo().getGadget()));
								logM.log.info(getName() + " Time: " + timeTick + " " + "Subscriber " + getName() + " sending EventG");
								resultGadget = (String) futGadget.get((timeExpired - timeTick) * 100, TimeUnit.MILLISECONDS);
							}
							else {
								MessageBrokerImpl.getInstance().sendEvent(new AbortMission(event.getInfo()));
								logM.log.warning("Time: " + timeTick + " " + "Subscriber " + getName() + " sending AbortMission due to missing condition: " + event.getInfo().getMissionName());
							}
							//////////////////execute mission///////////////////////////
							if (resultGadget != null) {
								int Qtime;
								try {
									 Qtime = Integer.parseInt(resultGadget);
								} catch (NumberFormatException e){Qtime = timeExpired+1;}
								if (Qtime <= timeExpired) {
									addReport(event.getInfo(), resultAgent, Qtime);
									MessageBrokerImpl.getInstance().sendEvent(new ExecuteMission(event.getInfo().getSerialAgentsNumbers(), event.getInfo().getDuration()));
									logM.log.info("Subscriber " + getName() + " sending ExecuteMission");
								} else { /////////////////Abort///////////////////////////////
									MessageBrokerImpl.getInstance().sendEvent(new AbortMission(event.getInfo()));
									logM.log.warning("Time: " + timeTick + " " + "Subscriber " + getName() + " sending AbortMission due to time expired");
								}
							}
							/////////////////Abort///////////////////////////////
							else {
								MessageBrokerImpl.getInstance().sendEvent(new AbortMission(event.getInfo()));
								logM.log.warning("Time: " + timeTick + " " + "Subscriber " + getName() + " sending AbortMission due to missing condition: " + event.getInfo().getMissionName());
							}

							/////////////////Termination////////////////////////
						} catch (InterruptedException e) {
							terminate();
							logM.log.warning(getName() + " terminating");
						}
					}
				} else {
					logM.log.warning(getName() + " Time: " + timeTick + " " + "call is not of type MissionReceivedEvent");

				}
				Long end = System.currentTimeMillis();
				logM.log.info("MissionRecieve Callback End " + end);
				logM.log.info("MissionRecieve Callback duration " + Math.subtractExact(end, start));
			}

			private void addReport(MissionInfo m, String s2, int Qtime) {
				logM.log.info(getName()+" Time: "+ timeTick + " add report start");
				try {
					List<String> serials = m.getSerialAgentsNumbers();
					Future names = getSimplePublisher().sendEvent(new GetAgentNamesEvent(serials));
					int MPinstance=0;
					try {
						 MPinstance = Integer.parseInt(s2);
					} catch (NumberFormatException e ){
						logM.log.severe("NumberFormatException");
					}
					List<String> namesList = (List<String>) names.get();
					Report report = new Report(m.getMissionName(), serialNumber, MPinstance, serials, namesList, m.getGadget(), m.getTimeIssued(), Qtime, timeTick);
					logM.log.info("Time: " + timeTick + " " + "New report was created");
					Diary.getInstance().addReport(report);
				} catch (InterruptedException e) {
					terminate();
					logM.log.warning(getName()+ " terminating");
				}
			}
		};

		subscribeEvent(MissionReceivedEvent.class, back);
	}


	private void subscribeToTickBroadCastEvent() {
		Callback tickCallBack = new Callback() {
			@Override
			public void call(Object c) {
				//synchronized (this) {
					if (c instanceof TickBroadcast) {
						TickBroadcast msg = (TickBroadcast) c;
						if (msg.getTime() > timeTick)
							timeTick = msg.getTime();
					} else {
						logM.log.severe("Time: "+ timeTick + " " +getName() + " received Broadcastmsg not from Tick type, type was: " + c.getClass());
					}
				}
			//}
		};
		subscribeBroadcast(TickBroadcast.class, tickCallBack);


	}


}

