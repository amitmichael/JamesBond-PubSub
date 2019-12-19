package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static class singletonHolder{ private static MessageBroker MessageBrokerInstance = new MessageBrokerImpl();}
	private ConcurrentHashMap<Class, ConcurrentLinkedQueue<Subscriber>> topics; // will hold all topics in the broker
	private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Message>> registered; //will hold all registered subscribers and their queues
	private ConcurrentHashMap<Event,Future> resultMap;
	private LogManager logM = LogManager.getInstance();

	private MessageBrokerImpl() {
		logM.log.info("MessageBroker constructor was called");
		topics = new ConcurrentHashMap<Class, ConcurrentLinkedQueue<Subscriber>>();
		registered = new ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Message>>();
		resultMap = new ConcurrentHashMap<Event,Future>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return singletonHolder.MessageBrokerInstance;
	}

	@Override
	public  <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		synchronized (topics) {
			if (!topics.containsKey(type)) { // if topic does not exists
				topics.put(type, new ConcurrentLinkedQueue<Subscriber>());
			}
			topics.get(type).add(m); // add the subscriber to topic list
			logM.log.info("Subscriber " + m.getName() + " Subscribed to " + type);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized (topics) {
			if (!topics.containsKey(type)) { // if topic does not exists
				topics.put(type, new ConcurrentLinkedQueue<Subscriber>());
			}
			topics.get(type).add(m); // add the subscriber to topic list
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		if (!resultMap.containsKey(e)){
			logM.log.severe("Event is not in futur map");
		}
		else
			resultMap.get(e).resolve(result);

	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		// add this msg to the topic broadcast
		// notify all subscribers of this topic that msg arrived
		if (!topics.containsKey(b.getClass())) {
			topics.put(b.getClass(), new ConcurrentLinkedQueue());
			logM.log.info("topic " + b.getClass() + " added");
		}
		if (topics.containsKey(b.getClass())) {
			Queue<Subscriber> distributionList = topics.get(b.getClass());
			if (!distributionList.isEmpty()) {
				Iterator it = distributionList.iterator();

				while (it.hasNext()) {
					Object curr = it.next();
					Subscriber currsub = (Subscriber) curr;
					registered.get(currsub).add(b); // add b to subscriber queue
					logM.log.info("Broadcast msg added to " + currsub.getName() + " queue");
					synchronized (currsub){
					currsub.notify();
				}}
			} else {
				logM.log.warning("Distribution list is empty");
			}
		} else {
			logM.log.severe("topic does not exists");
		}
	}


	@Override
	public synchronized  <T> Future<T> sendEvent(Event<T> e) {
		logM.log.info("SendEvent started msg from type " + e.getClass());
		Future fut = new Future();
		resultMap.put(e,fut);

		if (!topics.containsKey(e.getClass())){
			topics.put(e.getClass(), new ConcurrentLinkedQueue<Subscriber>() {
			});
		}
		if (!topics.get(e.getClass()).isEmpty()) {
			Subscriber curr = topics.get(e.getClass()).poll();
			registered.get(curr).add(e); //add the msg to curr queue
			topics.get(e.getClass()).add(curr); //add curr to the topic queue
			synchronized (curr){
				curr.notify();
			}
		}
		return fut;
	}

	@Override
	public void register(Subscriber m) {
		if (registered.containsKey(m))
			logM.log.warning("Attempt to register exists subscriber: " + m.getName());
		registered.put(m, new ConcurrentLinkedQueue<>());
		logM.log.info("Subscriber " + m.getName() + " registered");

	}

	@Override
	public void unregister(Subscriber m) {
		logM.log.info("Starting unregister for : " + m.getName());
		if (!registered.containsKey(m)){
			logM.log.warning("Trying to unregister not registered subscriber: " + m.getName());
		}
		else {
			Iterator it = topics.entrySet().iterator();
				while (it.hasNext()){
					HashMap.Entry pair = (HashMap.Entry)it.next();
					Queue q1 = (Queue) pair.getValue();
					logM.log.info("remove " + m.getName() + " from " + pair.getKey().toString() + " topic");
					q1.remove(m); //remove the subscriber from each topic queue
				}
			if (!registered.get(m).isEmpty())
			{
				logM.log.info("Resolving msg to cancel from " + m.getName() + " Queue");
				Iterator it1 = registered.get(m).iterator();
				while (it1.hasNext()){
					resultMap.get(it1.next()).resolve("Canceled");
				}
			}
			registered.remove(m); // remove from registered map
			logM.log.info("Subscriber " + m.getName()+ " Unregistered");
		}

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
			synchronized (m) {
					while (registered.get(m).isEmpty()) {
						try {
							logM.log.info(m.getName() + " waiting for msg");
							m.wait();
						} catch (InterruptedException e){ m.terminate(); }

					}
					return registered.get(m).poll();
				}
	}

}




