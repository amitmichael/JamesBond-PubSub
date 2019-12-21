package bgu.spl.mics.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;


public class AbortMission implements Event<String> {
    private MissionInfo info;


public AbortMission(MissionInfo info){
    this.info=info;

}
    public MissionInfo getInfo() {
        return info;
    }

}




