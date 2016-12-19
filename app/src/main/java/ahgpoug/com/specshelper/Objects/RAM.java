package ahgpoug.com.specshelper.objects;

import java.io.Serializable;

public class RAM implements Serializable {
    private int id;
    private String manufacturer;
    private String codename;
    private int clock;
    private int memorySize;
    private String type;
    private int price;

    public RAM(int id, String manufacturer, String codename, int clock, int memorySize, String type, int price) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.codename = codename;
        this.clock = clock;
        this.memorySize = memorySize;
        this.type = type;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }
}
