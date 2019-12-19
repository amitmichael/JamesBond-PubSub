
package bgu.spl.mics.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonEvent {

    @SerializedName("inventory")
    @Expose
    private String[] inventory = null;
    @SerializedName("services")
    @Expose
    private Services services;
    @SerializedName("squad")
    @Expose
    private List<Squad> squad = null;

    public String[]getInventory() {
        return inventory;
    }

    public void setInventory(String[] inventory) {
        this.inventory = inventory;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public List<Squad> getSquad() {
        return squad;
    }

    public void setSquad(List<Squad> squad) {
        this.squad = squad;
    }

}
