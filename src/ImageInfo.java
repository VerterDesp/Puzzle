
public class ImageInfo {

    private String neighborName;
    private double rate;

    public String getNeighborName() {
        return neighborName;
    }

    public void setNeighborName(String neighborName) {
        this.neighborName = neighborName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                ", neighborName='" + neighborName + '\'' +
                ", rate=" + rate +
                '}';
    }
}
