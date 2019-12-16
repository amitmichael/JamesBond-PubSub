package bgu.spl.mics;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBroker MessageBrokerInstance = null;
	private ConcurrentHashMap<Class, LinkedList> topics; // will hold all topics in the broker
	private ConcurrentHashMap<Subscriber, Queue<Message>> registered; //will hold all registered subscribers and their queues
	private LogManager logM = LogManager.getInstance();
	private MessageBrokerImpl() {
		logM.log.info("MessageBroker constructor was called");
		topics = new ConcurrentHashMap<Class, LinkedList>();
		registered = new ConcurrentHashMap<Subscriber, Queue<Message>>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		if (MessageBrokerInstance ==null)
			MessageBrokerInstance = new MessageBrokerImpl();
		return MessageBrokerInstance;

	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if (!topics.containsKey(type)){ // if topic does not exists
			topics.put(type,new LinkedList<Subscriber>());
		}
		topics.get(type).add(m); // add the subscriber to topic list
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// add this msg to the topic broadcast
		// notify all subscribers of this topic that msg arrived
		if (!topics.containsKey(b.getClass())){
			topics.put(b.getClass(), new LinkedList<Subscriber>());
			logM.log.info("topic " + b.getClass() + " added");
		}

		LinkedList<Subscriber> distributionList = topics.get(b);
		Iterator it = distributionList.iterator();

		while (it.hasNext()){
			Object curr = it.next();
			Subscriber currsub = (Subscriber) curr;
			registered.get(currsub).add(b); // add b to subscriber queue
			logM.log.info("Broadcast msg added to "+ currsub.getName()  + " queue");
			registered.get(currsub).notify();
			}
		}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(Subscriber m) {
		registered.put(m,new ConcurrentLinkedQueue());
		logM.log.info("Subscriber " + m.getName()+ " registered");

	}

	@Override
	public void unregister(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub

		synchronized (registered.get(m)){
			while (registered.get(m).isEmpty()){
				logM.log.info(m.getName() + " waiting for msg");
				wait();
			}
			return  registered.get(m).poll();
		}

	}



}
