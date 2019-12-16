package bgu.spl.mics;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBroker MessageBrokerInstance = null;
	private ConcurrentHashMap<Class, ConcurrentHashMap> topics; // will hold all topics in the broker
	private MessageBrokerImpl() {
		topics = new ConcurrentHashMap<Class, ConcurrentHashMap>();
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
			topics.put(type,new ConcurrentHashMap<>());
		}
		topics.get(type).put(m.hashCode(),m); // add the subscriber to topic map
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
			topics.put(b.getClass(), new ConcurrentHashMap());
		}

		ConcurrentHashMap distributionList = topics.get(b.getClass());

		for (Object sub : distributionList.entrySet()){

			sub.notify(); // ??
			}
		}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub

//		synchronized (INSTANCE)
//		{
		//exampleLock.lock();

		//	if (!exampleQueue.isEmpty())
		//	{
				//return exampleQueue.removeFirst();
		//	}

	//	exampleLock.unlock();
//		}
		return null;
	}



}
