package ahgpoug.com.specshelper.Objects;

public class Motherboard {
    private int id;
    private String manufacturer;
    private String codename;
    private String socket;
    private String formFactor;
    private String chipSet;
    private String ramType;
    private int maxRamCount;
    private int maxRamClock;
    private int maxRamSize;
    private int ideCount;
    private int sata6count;
    private int sata3count;
    private int pcie16count;
    private int pcie1count;
    private int usb2count;
    private int usb3count;
    private int maxEthernetSpeed;
    private boolean sli;
    private boolean crossFire;
    private int price;

    public Motherboard(int id, String manufacturer, String codename, String socket, String formFactor, String chipSet, String ramType, int maxRamCount, int maxRamSize, int maxRamClock, int ideCount, int sata6count, int sata3count, int pcie16count, int pcie1count, int usb2count, int usb3count, int maxEthernetSpeed, boolean sli, boolean crossFire, int price) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.codename = codename;
        this.socket = socket;
        this.formFactor = formFactor;
        this.chipSet = chipSet;
        this.ramType = ramType;
        this.maxRamCount = maxRamCount;
        this.maxRamSize = maxRamSize;
        this.maxRamClock = maxRamClock;
        this.ideCount = ideCount;
        this.sata6count = sata6count;
        this.sata3count = sata3count;
        this.pcie16count = pcie16count;
        this.pcie1count = pcie1count;
        this.usb2count = usb2count;
        this.usb3count = usb3count;
        this.maxEthernetSpeed = maxEthernetSpeed;
        this.sli = sli;
        this.crossFire = crossFire;
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

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public String getRamType() {
        return ramType;
    }

    public void setRamType(String ramType) {
        this.ramType = ramType;
    }

    public int getMaxRamCount() {
        return maxRamCount;
    }

    public void setMaxRamCount(int maxRamCount) {
        this.maxRamCount = maxRamCount;
    }

    public int getMaxRamClock() {
        return maxRamClock;
    }

    public void setMaxRamClock(int maxRamClock) {
        this.maxRamClock = maxRamClock;
    }

    public int getIdeCount() {
        return ideCount;
    }

    public void setIdeCount(int ideCount) {
        this.ideCount = ideCount;
    }

    public int getSata6count() {
        return sata6count;
    }

    public void setSata6count(int sata6count) {
        this.sata6count = sata6count;
    }

    public int getSata3count() {
        return sata3count;
    }

    public void setSata3count(int sata3count) {
        this.sata3count = sata3count;
    }

    public int getPcie16count() {
        return pcie16count;
    }

    public void setPcie16count(int pcie16count) {
        this.pcie16count = pcie16count;
    }

    public int getPcie1count() {
        return pcie1count;
    }

    public void setPcie1count(int pcie1count) {
        this.pcie1count = pcie1count;
    }

    public int getUsb2count() {
        return usb2count;
    }

    public void setUsb2count(int usb2count) {
        this.usb2count = usb2count;
    }

    public int getUsb3count() {
        return usb3count;
    }

    public void setUsb3count(int usb3count) {
        this.usb3count = usb3count;
    }

    public int getMaxEthernetSpeed() {
        return maxEthernetSpeed;
    }

    public void setMaxEthernetSpeed(int maxEthernetSpeed) {
        this.maxEthernetSpeed = maxEthernetSpeed;
    }

    public boolean isSli() {
        return sli;
    }

    public void setSli(boolean sli) {
        this.sli = sli;
    }

    public boolean isCrossFire() {
        return crossFire;
    }

    public void setCrossFire(boolean crossFire) {
        this.crossFire = crossFire;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getChipSet() {
        return chipSet;
    }

    public void setChipSet(String chipSet) {
        this.chipSet = chipSet;
    }

    public int getMaxRamSize() {
        return maxRamSize;
    }

    public void setMaxRamSize(int maxRamSize) {
        this.maxRamSize = maxRamSize;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }
}
