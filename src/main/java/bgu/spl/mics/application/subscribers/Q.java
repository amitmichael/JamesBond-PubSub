package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private static class singletonHolder{
		private static Q Qinstance = new Q();}
	private LogManager logM = LogManager.getInstance();
	private Inventory inv =Inventory.getInstance();


	/**
	 * Retrieves the single instance of this class.
	 */

	public static Q getInstance() {
		return singletonHolder.Qinstance;
	}

	private Q() {
		super("Q");
	}

	@Override
	protected void initialize() {
		logM.log.info("Subscriber " + this.getName() + " initialization");
		MessageBrokerImpl.getInstance().register(this);
		subscribeToGadgetAvailableEvent();

	}

	private void subscribeToGadgetAvailableEvent (){
		Callback back = new Callback() {
			@Override
			public void call(Object c) {
				logM.log.info("call of GadgetAvailableEvent callback started ");
				if (c instanceof GadgetAvailableEvent) {
					GadgetAvailableEvent event = (GadgetAvailableEvent) c;
					String gad = event.getGadget();
					if (inv.getItem(gad)) {
						logM.log.info("gadget "+ gad+ " is available");
						MessageBrokerImpl.getInstance().complete(event,"True");
					} else {
						MessageBrokerImpl.getInstance().complete(event,"Gadget " + gad+  " is not available");
					}
				}
				else {
					logM.log.severe("Q received msg not from GadgetAvailableEvent type, type was: " + c.getClass());
				}
			}
		};
		subscribeEvent(GadgetAvailableEvent.class, back);


	}

}
