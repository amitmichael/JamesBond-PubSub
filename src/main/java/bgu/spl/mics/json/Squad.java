
package bgu.spl.mics.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Squad {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
