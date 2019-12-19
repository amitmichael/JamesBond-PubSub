package bgu.spl.mics.application.publishers;

import bgu.spl.mics.LogManager;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.TickBroadcast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	private int termination;
	private int count;
	private Timer timer;
	private TimerTask task;
	private LogManager logM = LogManager.getInstance();
	private List<Thread> threads;


	/**
	 *
	 * @param duration the number of ticks before termination
	 */


	public TimeService(int duration) {
		super("TimeService");
		this.termination = duration;
		this.count = 1;
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public synchronized void run() {
				long start = System.currentTimeMillis();
					if (count <= termination) {
						TickBroadcast toSend = new TickBroadcast(count);
						logM.log.info("Sending Broadcast msg #" + count + " Time: " + toSend.getTime());
							getSimplePublisher().sendBroadcast(toSend);
						count++;
					}
					else {
						timer.cancel();
						for (Thread t : threads){
							t.interrupt();
							logM.log.info("$$ Interrupt to thread " + t.getName());
						}
					}
				long end = System.currentTimeMillis();
				logM.log.info("TimeService Timetick duration " + Math.subtractExact(end,start));
				}


		};
	}
	public void setThreads(List<Thread> t){
		this.threads = t;
	}

	@Override
	protected void initialize() { // ???
		// TODO Implement this
	}

	@Override
	public void run() {
		initialize();
		timer.schedule(task, 0, 100);
	}
}

