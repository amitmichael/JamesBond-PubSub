package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import bgu.spl.mics.json.Mission;

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
			public void call(Object c) throws InterruptedException {
				if (c instanceof MissionReceivedEvent) {
					MissionReceivedEvent event = (MissionReceivedEvent) c;
					int timeExpired = event.getInfo().getTimeExpired();
					Diary.getInstance().increment(); //increment the total

					logM.log.info("new TimeExpired assigned");
					GadgetAvailableEvent eventG = new GadgetAvailableEvent(event.getInfo().getGadget());
					AgentsAvailableEvent eventA = new AgentsAvailableEvent(event.getInfo().getSerialAgentsNumbers());
					Future fut1;
					Future fut2;
					String result1;
					String result2;
					synchronized (this) {
						fut1 = getSimplePublisher().sendEvent(eventG);
						logM.log.info("Subscriber " + getName() + " sending EventG");
						fut2 = getSimplePublisher().sendEvent(eventA);
						logM.log.info("Subscriber " + getName() + " sending EventA");
						 result1 = (String) fut1.get((timeExpired - timeTick) * 100, TimeUnit.MILLISECONDS);
						 result2 = (String) fut2.get((timeExpired - timeTick) * 100, TimeUnit.MILLISECONDS);
					}
					executeOrAbort(event,fut1,result1,result2,timeExpired);

				} else {
					logM.log.warning("call is not of type MissionReceivedEvent");

				}
			}

		};
		subscribeEvent(MissionReceivedEvent.class, back);
	}

	private void executeOrAbort(MissionReceivedEvent event, Future fut1,String s1,String s2,int timeExpired) throws InterruptedException {

			if (!s1.equals("false") & s2.equals("true")) {
				if (timeTick <= timeExpired) {
					executeMission(event,fut1);
				} else {
					MessageBrokerImpl.getInstance().sendEvent(new AbortMission(event.getInfo()));
					logM.log.warning("Subscriber " + getName() + " sending AbortMission due to time expired");
				}
			}
			else {
				MessageBrokerImpl.getInstance().sendEvent(new AbortMission(event.getInfo()));
				logM.log.warning("Subscriber " + getName() + " sending AbortMission due to missing condition");
			}
	}

	private void executeMission(MissionReceivedEvent event, Future fut1) throws InterruptedException {
		MessageBrokerImpl.getInstance().sendEvent(new ExecuteMission(event.getInfo().getSerialAgentsNumbers(),event.getInfo().getDuration()));
		Future fut3 = MessageBrokerImpl.getInstance().sendEvent(new ExecuteMission(event.getInfo().getSerialAgentsNumbers(),event.getInfo().getDuration()));
		logM.log.info("Subscriber " + getName() + " sending ExecuteMission");
		addReport(event.getInfo(),fut3,fut1);
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

	private void addReport(MissionInfo m,Future t3,Future f1)  {
		try{
		List<String> serials = m.getSerialAgentsNumbers();
		Future names = getSimplePublisher().sendEvent(new GetAgentNamesEvent(serials));
		int Qtime = Integer.parseInt((String) f1.get());
		int MPinstance = Integer.parseInt((String) t3.get());
		List<String> namesList = (List<String>) names.get();
		Report report = new Report(m.getMissionName(),serialNumber,MPinstance,serials,namesList,m.getGadget(),m.getTimeIssued(),Qtime,timeTick);
		logM.log.info("New report was created");
		Diary.getInstance().addReport(report);}
		catch (InterruptedException e){terminate();}
	}
}

