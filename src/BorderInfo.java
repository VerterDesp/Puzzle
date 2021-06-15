import java.awt.image.BufferedImage;

public class BorderInfo {
    private BufferedImage sourceIm;

    private String leftBorderImName;
    private double leftBorderRate;

    private String rightBorderImName;
    private double rightBorderRate;

    private String topBorderImName;
    private double topBorderRate;

    private String bottomBorderImName;
    private double bottomBorderRate;

    public BufferedImage getSourceIm() {
        return sourceIm;
    }

    public void setSourceIm(BufferedImage sourceIm) {
        this.sourceIm = sourceIm;
    }

    public String getLeftBorderImName() {
        return leftBorderImName;
    }

    public void setLeftBorderImName(String leftBorderImName) {
        this.leftBorderImName = leftBorderImName;
    }

    public double getLeftBorderRate() {
        return leftBorderRate;
    }

    public void setLeftBorderRate(double leftBorderRate) {
        this.leftBorderRate = leftBorderRate;
    }

    public String getRightBorderImName() {
        return rightBorderImName;
    }

    public void setRightBorderImName(String rightBorderImName) {
        this.rightBorderImName = rightBorderImName;
    }

    public double getRightBorderRate() {
        return rightBorderRate;
    }

    public void setRightBorderRate(double rightBorderRate) {
        this.rightBorderRate = rightBorderRate;
    }

    public String getTopBorderImName() {
        return topBorderImName;
    }

    public void setTopBorderImName(String topBorderImName) {
        this.topBorderImName = topBorderImName;
    }

    public double getTopBorderRate() {
        return topBorderRate;
    }

    public void setTopBorderRate(double topBorderRate) {
        this.topBorderRate = topBorderRate;
    }

    public String getBottomBorderImName() {
        return bottomBorderImName;
    }

    public void setBottomBorderImName(String bottomBorderImName) {
        this.bottomBorderImName = bottomBorderImName;
    }

    public double getBottomBorderRate() {
        return bottomBorderRate;
    }

    public void setBottomBorderRate(double bottomBorderRate) {
        this.bottomBorderRate = bottomBorderRate;
    }


    @Override
    public String toString() {
        return " left - " + leftBorderImName + " " + String.format("%.3f",leftBorderRate) +"% |"+
                " right " + rightBorderImName + " " + String.format("%.3f", rightBorderRate) +"% |"+
                " top " + topBorderImName + " " + String.format("%.3f" , topBorderRate) +"% |"+
                " bottom " + bottomBorderImName + " " + String.format("%.3f", bottomBorderRate) +"% |";
    }

}
