package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.events.*;
import java.util.Map;



/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private String serialNumber;
	private LogManager logM = LogManager.getInstance();
	private Squad squad = Squad.getInstance();
	private int timeTick=0;

	public Moneypenny(String serialNumber) {
		super("MoneyPenny" + serialNumber);
		this.serialNumber = serialNumber;
	}

	@Override
	protected synchronized void initialize() {
		logM.log.info("Subscriber " + this.getName() + " initialization");
		MessageBrokerImpl.getInstance().register(this);

		if (Integer.parseInt(serialNumber)%2==0){ // even
			subscribeToAbortMission();
			subscribeToExcuteMission();
			subscribedToGetAgentNamesEvent();
			subscribeToReleaseAllAgent();
			subscribeToTickBroadCastEvent();
		}
		else {
			subscribeToAgentsAvailableEvent();
			subscribeToTickBroadCastEvent();
		}
	}


	private void subscribedToGetAgentNamesEvent() {
		Callback back1 = new Callback() {
			@Override
			public void call(Object c) {
					if (c instanceof GetAgentNamesEvent) {
						GetAgentNamesEvent event = (GetAgentNamesEvent) c;
						MessageBrokerImpl.getInstance().complete(event, Squad.getInstance().getAgentsNames(event.getSerial()));

					} else {
						logM.log.warning("call is not of type GetAgentNamesEvent");
					}
			}
		};
		subscribeEvent(GetAgentNamesEvent.class, back1);
	}

	private void subscribeToExcuteMission() {
		Callback back1 = new Callback() {
			@Override
			public void call(Object c) {
				if (c instanceof ExecuteMission) {
					try {
						ExecuteMission event = (ExecuteMission) c;
						Squad.getInstance().sendAgents(event.getserials(), event.getDuration() * 100);
							logM.log.info( "Time: " + timeTick + " "+ getName() + "Send agents to Mission");
						MessageBrokerImpl.getInstance().complete(event, serialNumber);
					} catch (InterruptedException e) {logM.log.severe("&&");}

				} else {
					logM.log.warning("call is not of type ExecuteMission");
				}
			}
		};
		subscribeEvent(ExecuteMission.class,back1);
	}

	private void subscribeToAgentsAvailableEvent() {
		Callback back = new Callback() {

			@Override
			public void call(Object c)  {
				boolean result;
				if (c instanceof AgentsAvailableEvent) {
					AgentsAvailableEvent event = (AgentsAvailableEvent) c;

					logM.log.info("squad is trying to execute agents for mission : " + event.getMissionname() +" agents: " + Squad.getInstance().getAgentsNames(event.getserials()));

					try  {
						result = squad.getAgents(event.getserials());
						if (result == true) {
							logM.log.info(" agents: " + Squad.getInstance().getAgentsNames(event.getserials())+ " acquired for mission: "+ event.getMissionname() );
							MessageBrokerImpl.getInstance().complete(event, serialNumber);
						} else {
							MessageBrokerImpl.getInstance().complete(event, null);
						}
					} catch (InterruptedException e){
						MessageBrokerImpl.getInstance().complete(event, null);
					}
				} else {
					logM.log.warning("call is not of type AgentAvilableEvent");
				}
			}
		};
		subscribeEvent(AgentsAvailableEvent.class, back);

	}

	private void subscribeToAbortMission() {
		Callback back = new Callback() {
			@Override
			public void call(Object c) throws InterruptedException {
				if (c instanceof AbortMission) {
					AbortMission event = (AbortMission) c;
					logM.log.info("Time: " + timeTick +  " Abort - releasing agents");
					squad.releaseAgents(event.getInfo().getSerialAgentsNumbers());//release agents of aborted mission
					MessageBrokerImpl.getInstance().complete(event, "true");
				}
				else {
					logM.log.warning("call is not of type AbortMission");
				}
			}
		};
		subscribeEvent(AbortMission.class, back);

	}
	public String getSerialNumber(){
		return serialNumber;
	}

	private void releaseAllAgents(){ //during termination
		Map<String, Agent> all = Squad.getInstance().getAgents();
		logM.log.info("@ Starting releasing all agents");
		for (Agent a : all.values()){
			a.terminate();
			a.release();
		}
	}

	private void subscribeToReleaseAllAgent(){
		Callback back = new Callback() {
			@Override
			public void call(Object c) {
				if (c instanceof ReleaseAllAgents){
					logM.log.info( getName() + " Got ReleaseAllAgents msg");
					releaseAllAgents();
				}
				else
					logM.log.severe(getName() + " received msg not from ReleaseAllAgents type, type was: " + c.getClass());
			}
		};
		subscribeEvent(ReleaseAllAgents.class,back);
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


