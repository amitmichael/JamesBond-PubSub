
package bgu.spl.mics.json;

import java.util.List;

import bgu.spl.mics.application.passiveObjects.MissionInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Intelligence {

    @SerializedName("missions")
    @Expose
    private List<Mission> missions = null;

    public List<Mission> getMissions() {
        return missions;
    }

    public void setMissions(List<Mission> missions) {
        this.missions = missions;
    }

}
