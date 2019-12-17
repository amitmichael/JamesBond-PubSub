package bgu.spl.mics;

import java.util.List;

public class GetAgentNamesEvent implements Event {
    private List<String> serials;

    public GetAgentNamesEvent(List<String> serials){
        this.serials = serials;
    }
    public List<String> getSerial(){
        return serials;
    }
}
