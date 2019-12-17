package bgu.spl.mics;

import java.util.List;

public class ExcuteMission implements Event<String> {
    private List<String> serials;
    private int duration;

    public ExcuteMission(List<String> serials, int duration)
    {
        this.serials=serials;
        this.duration = duration;
    }

    public int getDuration(){
        return this.duration;
    }
    public List<String> getserials() {
        return serials;
    }

}
