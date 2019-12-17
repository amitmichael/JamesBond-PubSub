package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.HashMap;
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
	private String name;
	long TimeTick;
	long TimeExpired;
	private Diary diary=Diary.getInstance();
	private Report report;



	public M(String name) {
		super(name);
		this.name=name;//name specific for M instance
		TimeTick=0;
		TimeExpired=0;
	}


	@Override
	protected synchronized void initialize()  {
		logM.log.info("Subscriber " + this.getName() + " initialization");
		getInstance().register(this);
		subscribeMissionRecievedEvent();
		subscribeTickBrodcast();


	}
	private void subscribeMissionRecievedEvent(){
		Callback back = new Callback() {
			@Override
			public void call(Object c) throws InterruptedException {
				if (c instanceof MissionReceivedEvent) {
					MissionReceivedEvent event = (MissionReceivedEvent) c;
					TimeExpired=event.getInfo().getTimeExpired();
					logM.log.info("new TimeExpired assigned");
					GadgetAvailableEvent eventG=new GadgetAvailableEvent(event.getInfo().getGadget());
					AgentsAvailableEvent eventA=new AgentsAvailableEvent(event.getInfo().getSerialAgentsNumbers());
					Future fut1= getInstance().sendEvent(eventG);
					logM.log.info("Subscriber " + name + " sending EventG");
					Future fut2 =MessageBrokerImpl.getInstance().sendEvent(eventA);
					logM.log.info("Subscriber " + name + " sending EventA");
					if(fut1.getSuccess()&fut2.getSuccess()){
						if(TimeTick<=TimeExpired){
							MessageBrokerImpl.getInstance().sendEvent(new ExcuteMission());
							logM.log.info("Subscriber " + name + " sending ExcuteMission");
						}
						else{
							MessageBrokerImpl.getInstance().sendEvent(new AbortMission());
							logM.log.info("Subscriber " + name + " sending AbortMission");
						}
					}
					MessageBrokerImpl.getInstance().complete(event,"Done");


				}
				else{
					logM.log.warning("call is not of type MissionReceivedEvent");

				}
			}

		};
		subscribeEvent(MissionReceivedEvent.class, back);
		// subscribe to broadcast
	}
	private void subscribeTickBrodcast(){
		Callback back=new Callback() {
			@Override
			public void call(Object c) throws TimeoutException, InterruptedException {
				if (c instanceof TickBroadcast){
					TickBroadcast tick=(TickBroadcast)c;
					TimeTick=tick.getTime();
				}
			}
		};
	}
}

