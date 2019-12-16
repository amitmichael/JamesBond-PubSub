package bgu.spl.mics;

public class GadgetAvailableEvent implements Event<String> {
    private String gadget;
    private Future fut;

public GadgetAvailableEvent(String gadget,Future fut){
    this.gadget = gadget;
    this.fut = fut;
}

public String getGadget(){
    return this.gadget;
}
public Future getFut(){
    return this.fut;
}
public void setFuture(Future fut){
    this.fut = fut;
}
}
