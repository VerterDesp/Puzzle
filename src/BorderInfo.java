import java.awt.image.BufferedImage;

public class BorderInfo {
    private BufferedImage sourceIm;

    private String leftBorderImName;
    //private double leftBorderRate;

    private String rightBorderImName;
    //private double rightBorderRate;

    private String topBorderImName;
    //private double topBorderRate;

    private String bottomBorderImName;
    //private double bottomBorderRate;

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

    public String getRightBorderImName() {
        return rightBorderImName;
    }

    public void setRightBorderImName(String rightBorderImName) {
        this.rightBorderImName = rightBorderImName;
    }

    public String getTopBorderImName() {
        return topBorderImName;
    }

    public void setTopBorderImName(String topBorderImName) {
        this.topBorderImName = topBorderImName;
    }

    public String getBottomBorderImName() {
        return bottomBorderImName;
    }

    public void setBottomBorderImName(String bottomBorderImName) {
        this.bottomBorderImName = bottomBorderImName;
    }

//    @Override
//    public String toString() {
//        return " left - " + leftBorderImName + " " + String.format("%.3f",leftBorderRate) +"% |"+
//                " right " + rightBorderImName + " " + String.format("%.3f", rightBorderRate) +"% |"+
//                " top " + topBorderImName + " " + String.format("%.3f" , topBorderRate) +"% |"+
//                " bottom " + bottomBorderImName + " " + String.format("%.3f", bottomBorderRate) +"% |";
//    }

    @Override
    public String toString() {
        return " left - " + leftBorderImName +" |"+
                " right " + rightBorderImName +" |"+
                " top " + topBorderImName +" |"+
                " bottom " + bottomBorderImName;
    }

}
