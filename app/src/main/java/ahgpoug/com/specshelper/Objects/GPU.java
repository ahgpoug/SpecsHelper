package ahgpoug.com.specshelper.objects;

public class GPU {
    private int id;
    private String manufacturer;
    private String codename;
    private int cClock;
    private int mClock;
    private int memorySize;
    private String memoryType;
    private int bus;
    private int process;
    private int slots;
    private boolean sli;
    private int price;

    public GPU(int id, String manufacturer, String codename, int cClock, int mClock, int memorySize, String memoryType, int bus, int process, int slots, boolean sli, int price) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.codename = codename;
        this.cClock = cClock;
        this.mClock = mClock;
        this.memorySize = memorySize;
        this.memoryType = memoryType;
        this.bus = bus;
        this.process = process;
        this.slots = slots;
        this.sli = sli;
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

    public int getcClock() {
        return cClock;
    }

    public void setcClock(int cClock) {
        this.cClock = cClock;
    }

    public int getmClock() {
        return mClock;
    }

    public void setmClock(int mClock) {
        this.mClock = mClock;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    public int getBus() {
        return bus;
    }

    public void setBus(int bus) {
        this.bus = bus;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public boolean isSli() {
        return sli;
    }

    public void setSli(boolean sli) {
        this.sli = sli;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
