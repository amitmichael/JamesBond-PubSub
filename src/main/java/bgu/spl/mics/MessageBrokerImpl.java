package bgu.spl.mics;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBroker MessageBrokerInstance = null;

	//private static final ReentrantLock exampleLock = new ReentrantLock();
	//private static final ReentrantLock exampleLock2 = new ReentrantLock();
	//private LinkedList<String> exampleQueue = new LinkedList<>();
	//private BlockingQueue<Event> MREQueue;

	private MessageBrokerImpl() {}

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
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

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
