
package bgu.spl.mics.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Services {

    @SerializedName("M")
    @Expose
    private Integer m;
    @SerializedName("Moneypenny")
    @Expose
    private Integer moneypenny;
    @SerializedName("intelligence")
    @Expose
    private List<Intelligence> intelligence = null;
    @SerializedName("time")
    @Expose
    private Integer time;

    public Integer getM() {
        return m;
    }

    public void setM(Integer m) {
        this.m = m;
    }

    public Integer getMoneypenny() {
        return moneypenny;
    }

    public void setMoneypenny(Integer moneypenny) {
        this.moneypenny = moneypenny;
    }

    public List<Intelligence> getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(List<Intelligence> intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}
