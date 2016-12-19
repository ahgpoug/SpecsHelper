package ahgpoug.com.specshelper.Objects;

public class Cart {
    private int cpuID;
    private int mbID;
    private int gpuID;
    private int ramID;
    private int gpuAmount;
    private int ramAmount;

    public Cart(){
        this.cpuID = -1;
        this.mbID = -1;
        this.gpuID = -1;
        this.ramID = -1;
        this.gpuAmount = 0;
        this.ramAmount = 0;
    }

    public int getCpuID() {
        return cpuID;
    }

    public void setCpuID(int cpuID) {
        this.cpuID = cpuID;
    }

    public int getRamID() {
        return ramID;
    }

    public void setRamID(int ramID) {
        this.ramID = ramID;
    }

    public int getMbID() {
        return mbID;
    }

    public void setMbID(int mbID) {
        this.mbID = mbID;
    }

    public int getGpuID() {
        return gpuID;
    }

    public void setGpuID(int gpuID) {
        this.gpuID = gpuID;
    }

    public int getGpuAmount() {
        return gpuAmount;
    }

    public void setGpuAmount(int gpuAmount) {
        this.gpuAmount = gpuAmount;
    }

    public int getRamAmount() {
        return ramAmount;
    }

    public void setRamAmount(int ramAmount) {
        this.ramAmount = ramAmount;
    }
}
