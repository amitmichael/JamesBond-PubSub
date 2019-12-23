package bgu.spl.mics;

import bgu.spl.mics.events.*;
import javafx.util.Pair;

import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static class singletonHolder {
		private static MessageBroker MessageBrokerInstance = new MessageBrokerImpl();
	}

	private ConcurrentHashMap<Class, LinkedBlockingQueue<Subscriber>> topics; // will hold all topics in the broker
	private HashMap<Subscriber, LinkedBlockingQueue<Message>> registered; //will hold all registered subscribers and their queues
	private ConcurrentHashMap<Event, Future> resultMap;
	private LogManager logM = LogManager.getInstance();

	private MessageBrokerImpl() {
		logM.log.info("MessageBroker constructor was called");
		topics = new ConcurrentHashMap<Class, LinkedBlockingQueue<Subscriber>>();
		registered = new HashMap<Subscriber, LinkedBlockingQueue<Message>>();
		resultMap = new ConcurrentHashMap<Event, Future>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return singletonHolder.MessageBrokerInstance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		synchronized (topics) {
			if (!topics.containsKey(type)) { // if topic does not exists
				topics.put(type, new LinkedBlockingQueue<Subscriber>());
			}
			topics.get(type).add(m); // add the subscriber to topic list
			logM.log.info("Subscriber " + m.getName() + " Subscribed to " + type +" Size " + topics.get(type).size());
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized (topics) {
			if (!topics.containsKey(type)) { // if topic does not exists
				topics.put(type, new LinkedBlockingQueue<Subscriber>());
			}
			topics.get(type).add(m); // add the subscriber to topic list
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		if (!resultMap.containsKey(e)) {
			logM.log.severe("Event is not in future map");
		} else
			resultMap.get(e).resolve(result);

	}

	@Override
	public void sendBroadcast(Broadcast b)   {
		Long start = System.currentTimeMillis();

		// add this msg to the topic broadcast
		// notify all subscribers of this topic that msg arrived
		LinkedBlockingQueue distributionList=null;
		synchronized (topics) {
			if (!topics.containsKey(b.getClass())) {
				topics.put(b.getClass(), new LinkedBlockingQueue());
				logM.log.info("topic " + b.getClass() + " added");
			}
			distributionList = topics.get(b.getClass());
		}
		if (!distributionList.isEmpty()) {
				Iterator it = distributionList.iterator();
				while (registered != null && it.hasNext()) {
					Object curr = it.next();
					Subscriber currsub = (Subscriber) curr;
					if (registered.get(currsub) != null) {
						try {
							synchronized (registered.get(currsub)) {
								if (b instanceof Termination) {
									clearQueue(currsub);
								}
								registered.get(currsub).put(b); // add b to subscriber queue
								//if (b instanceof TickBroadcast) {
								//	updateTick(currsub, (TickBroadcast) b);
								//}
								logM.log.info("%% " + System.currentTimeMillis() + " Broadcast msg added to " + currsub.getName() + " queue");
							}
						} catch (InterruptedException ex) {
							logM.log.severe("InterruptedException");
						}
					}
				}
			}
		else {
				logM.log.warning("Distribution list is empty");
			}
		Long end = System.currentTimeMillis();
		logM.log.info("sendBroadcast duration " + Math.subtractExact(end,start));
	}


	@Override
	public  <T> Future<T> sendEvent(Event<T> e) {
		logM.log.info("SendEvent started msg from type " + e.getClass());
		Future fut = new Future();
		resultMap.put(e, fut);

		synchronized (topics) {
			if (!topics.containsKey(e.getClass())) {
				topics.put(e.getClass(), new LinkedBlockingQueue<Subscriber>() {
				});
				logM.log.info("Creating new topic: "+ e.getClass());
			}
			if (!topics.get(e.getClass()).isEmpty()) {
				try {
					Subscriber curr = topics.get(e.getClass()).poll();
					logM.log.info(" removing " + curr.getName() + " from the round robin loop, size: " + topics.get(e.getClass()).size());
					logM.log.info("adding msg from type " + e.getClass() + " to "+ curr.getName()+ " queue");
						//	synchronized (registered.get(curr)) {
					registered.get(curr).put(e); //add the msg to curr queue
					topics.get(e.getClass()).put(curr); //add curr to the topic queue
					logM.log.info(" adding " + curr.getName() + " back to the round robin loop, size: " + topics.get(e.getClass()).size());

				} catch (InterruptedException ex) {
					logM.log.severe("InterruptedException");
				}
			}
			return fut;
		}
	}

	@Override
	public synchronized void register(Subscriber m) {
		if (registered.containsKey(m))
			logM.log.warning("Attempt to register exists subscriber: " + m.getName());
		registered.put(m, new LinkedBlockingQueue<>());
		logM.log.info("Subscriber " + m.getName() + " registered");
	}

	@Override
	public void unregister(Subscriber m) {
		logM.log.info("Starting unregister for : " + m.getName());
		if (!registered.containsKey(m)){
			logM.log.warning("Trying to unregister not registered subscriber: " + m.getName());
		}
		else {
			synchronized (topics) {
				Iterator it = topics.entrySet().iterator();
				while (it.hasNext()) {
					HashMap.Entry pair = (HashMap.Entry) it.next();
					Queue q1 = (Queue) pair.getValue();
					logM.log.info("remove " + m.getName() + " from " + pair.getKey().toString() + " topic");
					q1.remove(m); //remove the subscriber from each topic queue
				}
			}
				if (!registered.get(m).isEmpty()) {
					synchronized (registered.get(m)) {
						logM.log.info("Resolving msg to cancel from " + m.getName() + " Queue");
						Iterator it1 = registered.get(m).iterator();
						while (resultMap != null && it1.hasNext()) {
							Message curr = (Message) it1.next();
						}
					}
					registered.remove(m); // remove from registered map
					logM.log.info("Subscriber " + m.getName() + " Unregistered");
				}
		}
	}

	@Override
	public Message awaitMessage(Subscriber m)   {
		logM.log.info(m.getName() + " waiting for msg");
		if (!registered.containsKey(m)) {
			logM.log.warning("Waiting for msg for non registered subscriber " + m.getName());
		}
		if (registered.get(m)!=null) {
			try {
				return registered.get(m).take();

			} catch (InterruptedException e) {
				m.terminate();
				logM.log.warning(m.getName() + " terminating");
				return null;
			}
		}
		else {
			logM.log.warning("Msg queue of " + m.getName()+ " is null");
			return null;
		}

	}
	private void updateTick (Subscriber currsub , TickBroadcast b) throws InterruptedException {
		int count =0;
		Iterator itmsg = registered.get(currsub).iterator();
		while (itmsg.hasNext()){
			Message m = (Message) itmsg.next();
			if (m instanceof TickBroadcast) {
				TickBroadcast mm = (TickBroadcast) m;
				 mm.setTime(b.getTime());
				count ++;
			}
		}
		if (count>0)
			logM.log.info(currsub.getName()+  " :Clean Tick "+ count + " msg cleaned");
		else {
			registered.get(currsub).put(b); // add b to subscriber queue
		}
	}
	private void clearQueue(Subscriber currsub){
		for (Message m : registered.get(currsub)){
			if ((m instanceof GadgetAvailableEvent | m instanceof AgentsAvailableEvent| m instanceof AbortMission )){
				registered.get(currsub).remove(m);
			}
		}
	}
}







