package ahgpoug.com.specshelper.Objects;

public class CPU {

    private int id;
    private String manufacturer;
    private String codename;
    private String socket;
    private int coresCount;
    private int process;
    private int clock;
    private int tdp;
    private String gpuType;
    private int release;
    private int price;

    private CPU(){
    }

    public CPU(int id, String manufacturer, String codename, String socket, int coresCount, int process, int clock, int tdp, String gpuType, int release, int price) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.codename = codename;
        this.socket = socket;
        this.coresCount = coresCount;
        this.process = process;
        this.clock = clock;
        this.tdp = tdp;
        this.gpuType = gpuType;
        this.release = release;
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

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public int getCoresCount() {
        return coresCount;
    }

    public void setCoresCount(int coresCount) {
        this.coresCount = coresCount;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getTdp() {
        return tdp;
    }

    public void setTdp(int tdp) {
        this.tdp = tdp;
    }

    public String getGpuType() {
        return gpuType;
    }

    public void setGpuType(String gpuType) {
        this.gpuType = gpuType;
    }

    public int getRelease() {
        return release;
    }

    public void setRelease(int release) {
        this.release = release;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
