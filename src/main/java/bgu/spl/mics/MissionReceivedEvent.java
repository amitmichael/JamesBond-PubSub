package bgu.spl.mics;


import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event<String> {
    private String msg;
    private MissionInfo info;

    public MissionReceivedEvent(String msg, MissionInfo mission)
    {
        this.msg = msg;
        this.info = mission;
    }
    public MissionInfo getMissionInfo(){
        return this.info;
    }

    public MissionInfo getInfo() {
        return info;
    }
}
