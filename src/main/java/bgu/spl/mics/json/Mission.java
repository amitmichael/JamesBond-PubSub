
package bgu.spl.mics.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mission {

    @SerializedName("serialAgentsNumbers")
    @Expose
    private List<String> serialAgentsNumbers = null;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("gadget")
    @Expose
    private String gadget;
    @SerializedName("missionName")
    @Expose
    private String missionName;
    @SerializedName("timeExpired")
    @Expose
    private Integer timeExpired;
    @SerializedName("timeIssued")
    @Expose
    private Integer timeIssued;

    public List<String> getSerialAgentsNumbers() {
        return serialAgentsNumbers;
    }

    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
        this.serialAgentsNumbers = serialAgentsNumbers;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGadget() {
        return gadget;
    }

    public void setGadget(String gadget) {
        this.gadget = gadget;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public Integer getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(Integer timeExpired) {
        this.timeExpired = timeExpired;
    }

    public Integer getTimeIssued() {
        return timeIssued;
    }

    public void setTimeIssued(Integer timeIssued) {
        this.timeIssued = timeIssued;
    }

}
