
package bgu.spl.mics.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Report {

    @SerializedName("missionName")
    @Expose
    private String missionName;
    @SerializedName("m")
    @Expose
    private Integer m;
    @SerializedName("moneypenny")
    @Expose
    private Integer moneypenny;
    @SerializedName("agentsSerialNumbers")
    @Expose
    private List<String> agentsSerialNumbers = null;
    @SerializedName("agentsNames")
    @Expose
    private List<String> agentsNames = null;
    @SerializedName("gadgetName")
    @Expose
    private String gadgetName;
    @SerializedName("timeCreated")
    @Expose
    private Integer timeCreated;
    @SerializedName("timeIssued")
    @Expose
    private Integer timeIssued;
    @SerializedName("qTime")
    @Expose
    private Integer qTime;

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public Report withMissionName(String missionName) {
        this.missionName = missionName;
        return this;
    }

    public Integer getM() {
        return m;
    }

    public void setM(Integer m) {
        this.m = m;
    }

    public Report withM(Integer m) {
        this.m = m;
        return this;
    }

    public Integer getMoneypenny() {
        return moneypenny;
    }

    public void setMoneypenny(Integer moneypenny) {
        this.moneypenny = moneypenny;
    }

    public Report withMoneypenny(Integer moneypenny) {
        this.moneypenny = moneypenny;
        return this;
    }

    public List<String> getAgentsSerialNumbers() {
        return agentsSerialNumbers;
    }

    public void setAgentsSerialNumbers(List<String> agentsSerialNumbers) {
        this.agentsSerialNumbers = agentsSerialNumbers;
    }

    public Report withAgentsSerialNumbers(List<String> agentsSerialNumbers) {
        this.agentsSerialNumbers = agentsSerialNumbers;
        return this;
    }

    public List<String> getAgentsNames() {
        return agentsNames;
    }

    public void setAgentsNames(List<String> agentsNames) {
        this.agentsNames = agentsNames;
    }

    public Report withAgentsNames(List<String> agentsNames) {
        this.agentsNames = agentsNames;
        return this;
    }

    public String getGadgetName() {
        return gadgetName;
    }

    public void setGadgetName(String gadgetName) {
        this.gadgetName = gadgetName;
    }

    public Report withGadgetName(String gadgetName) {
        this.gadgetName = gadgetName;
        return this;
    }

    public Integer getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Integer timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Report withTimeCreated(Integer timeCreated) {
        this.timeCreated = timeCreated;
        return this;
    }

    public Integer getTimeIssued() {
        return timeIssued;
    }

    public void setTimeIssued(Integer timeIssued) {
        this.timeIssued = timeIssued;
    }

    public Report withTimeIssued(Integer timeIssued) {
        this.timeIssued = timeIssued;
        return this;
    }

    public Integer getQTime() {
        return qTime;
    }

    public void setQTime(Integer qTime) {
        this.qTime = qTime;
    }

    public Report withQTime(Integer qTime) {
        this.qTime = qTime;
        return this;
    }

}