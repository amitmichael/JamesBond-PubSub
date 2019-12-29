package bgu.spl.mics.events;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentsAvailableEvent implements Event<String> {
    private List<String> serials;
    private int durationinMillis;
    private String Missionname;

    public AgentsAvailableEvent(List<String> serials,int durationinMillis, String Missionname)
    {
        this.serials=serials;
        this.durationinMillis = durationinMillis;
        this.Missionname = Missionname;
    }

    public int getDurationinMillis() {
        return durationinMillis;
    }

    public List<String> getserials() {
        return serials;
    }

    public String getMissionname() {
        return Missionname;
    }
}
