
package bgu.spl.mics.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
