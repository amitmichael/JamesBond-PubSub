package bgu.spl.mics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBroker MessageBrokerInstance = null;
	private HashMap<Class, Queue<Subscriber>> topics; // will hold all topics in the broker
	private HashMap<Subscriber, Queue<Message>> registered; //will hold all registered subscribers and their queues
	private LogManager logM = LogManager.getInstance();

	private MessageBrokerImpl() {
		logM.log.info("MessageBroker constructor was called");
		topics = new HashMap<Class, Queue<Subscriber>>();
		registered = new HashMap<Subscriber, Queue<Message>>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		if (MessageBrokerInstance == null)
			MessageBrokerInstance = new MessageBrokerImpl();
		return MessageBrokerInstance;

	}

	@Override
	public synchronized   <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		if (!topics.containsKey(type)) { // if topic does not exists
			topics.put(type, new LinkedList<Subscriber>());
		}
		topics.get(type).add(m); // add the subscriber to topic list
		logM.log.info("Subscriber " + m.getName() + " Subscribed to " + type);

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if (!topics.containsKey(type)) { // if topic does not exists
			topics.put(type, new LinkedList<Subscriber>());
		}
		topics.get(type).add(m); // add the subscriber to topic list
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		// add this msg to the topic broadcast
		// notify all subscribers of this topic that msg arrived
		if (!topics.containsKey(b.getClass())) {
			topics.put(b.getClass(), new LinkedList());
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
					currsub.notify();
				}
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
		if (!topics.containsKey(e.getClass())){
			topics.put(e.getClass(), new LinkedList<Subscriber>() {
			});
		}
		if (!topics.get(e.getClass()).isEmpty()) {
			Subscriber curr = topics.get(e.getClass()).poll();
			e.setFuture(fut);
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
		registered.put(m, new LinkedList<>());
		logM.log.info("Subscriber " + m.getName() + " registered");

	}

	@Override
	public void unregister(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		synchronized (m) {
			while (registered.get(m).isEmpty()) {
				logM.log.info(m.getName() + " waiting for msg");
				m.wait();
			}
			return registered.get(m).poll();
		}

	}
}




