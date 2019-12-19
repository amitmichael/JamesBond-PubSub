package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

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
				logM.log.info("MissionRecieve Callback Start " + start);
				if (c instanceof MissionReceivedEvent) {
					MissionReceivedEvent event = (MissionReceivedEvent) c;
					logM.log.info("Time: " + timeTick+ " " + getName() + " Handle mission "+ event.getInfo().getMissionName() + " time  issued " + event.getInfo().getTimeIssued() );
					int timeExpired = event.getInfo().getTimeExpired();
					Diary.getInstance().increment(); //increment the total

					logM.log.info(getName() +" Time: " + timeTick + " " + "new TimeExpired assigned");
					GadgetAvailableEvent eventG = new GadgetAvailableEvent(event.getInfo().getGadget());
					AgentsAvailableEvent eventA = new AgentsAvailableEvent(event.getInfo().getSerialAgentsNumbers());

					///try to acquire the agent
					Future fut2 = getSimplePublisher().sendEvent(eventA);
					logM.log.info( getName()+" Time: " + timeTick + " " + "Subscriber " + getName() + " sending EventA");
					try {
						logM.log.info(getName() +": time left  to process: "+ event.getInfo().getMissionName()+ " : " + (timeExpired - timeTick) * 100);
						String result2 = (String) fut2.get((timeExpired - timeTick) * 100, TimeUnit.MILLISECONDS);
						System.out.println("" + timeTick + " " + result2);
					///try to acquire the gadget
						Future fut1=null;
						String result1=null;
					if (result2!=null) {
						fut1 = getSimplePublisher().sendEvent(eventG);
						logM.log.info(getName()+ " Time: " + timeTick + " " + "Subscriber " + getName() + " sending EventG");
						result1 = (String) fut1.get((timeExpired - timeTick) * 100, TimeUnit.MILLISECONDS);
					}
						if (result1!=null){
							int Qtime = Integer.parseInt(result1);
							timeTick = Integer.parseInt(result1); // ?
						}
						executeOrAbort(event, fut1, result1, result2, timeExpired);
					} catch (InterruptedException e) {	terminate();
						logM.log.warning(getName()+ " terminating");}

				} else {
					logM.log.warning(getName()+" Time: " + timeTick + " " + "call is not of type MissionReceivedEvent");

				}
				Long end = System.currentTimeMillis();
				logM.log.info("MissionRecieve Callback End " + end);
				logM.log.info("MissionRecieve Callback duration " + Math.subtractExact(end, start));
			}

			private void executeOrAbort(MissionReceivedEvent event, Future fut1, String s1, String s2, int timeExpired)  {

				if (s1!=null & s2!=null) {
					if (timeTick <= timeExpired) {
						MessageBrokerImpl.getInstance().sendEvent(new ExecuteMission(event.getInfo().getSerialAgentsNumbers(), event.getInfo().getDuration()));
						//Future fut3 = MessageBrokerImpl.getInstance().sendEvent(new ExecuteMission(event.getInfo().getSerialAgentsNumbers(), event.getInfo().getDuration()));
						logM.log.info("Subscriber " + getName() + " sending ExecuteMission");
						addReport(event.getInfo(), s2, fut1);
					} else {
						MessageBrokerImpl.getInstance().sendEvent(new AbortMission(event.getInfo()));
						logM.log.warning("Time: " + timeTick + " " + "Subscriber " + getName() + " sending AbortMission due to time expired");
					}
				} else {
					MessageBrokerImpl.getInstance().sendEvent(new AbortMission(event.getInfo()));
					logM.log.warning("Time: " + timeTick + " " + "Subscriber " + getName() + " sending AbortMission due to missing condition: " + event.getInfo().getMissionName());
				}
			}

			private void addReport(MissionInfo m, String s2, Future f1) {
				try {
					List<String> serials = m.getSerialAgentsNumbers();
					Future names = getSimplePublisher().sendEvent(new GetAgentNamesEvent(serials));
					int Qtime = Integer.parseInt((String) f1.get());
					int MPinstance= 0;
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
				synchronized (this) {
					if (c instanceof TickBroadcast) {
						TickBroadcast msg = (TickBroadcast) c;
						if (msg.getTime() > timeTick)
							timeTick = msg.getTime();
					} else {
						logM.log.severe("Time: "+ timeTick + " " +getName() + " received Broadcastmsg not from Tick type, type was: " + c.getClass());
					}
				}
			}
		};
		subscribeBroadcast(TickBroadcast.class, tickCallBack);


	}


}

