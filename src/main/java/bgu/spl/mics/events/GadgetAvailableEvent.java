package bgu.spl.mics.events;

import bgu.spl.mics.Event;

public class GadgetAvailableEvent implements Event<String> {
    private String gadget;

public GadgetAvailableEvent(String gadget){
    this.gadget = gadget;
}

public String getGadget(){
    return this.gadget;
}

}
