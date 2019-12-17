
package bgu.spl.mics.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonEvent {

    @SerializedName("inventory")
    @Expose
    private List<String> inventory = null;
    @SerializedName("services")
    @Expose
    private Services services;
    @SerializedName("squad")
    @Expose
    private List<Squad> squad = null;

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
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
