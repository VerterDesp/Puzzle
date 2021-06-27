package algorithm;

public class BorderInfo {

    private String leftBorderImName;
    //private double leftBorderRate;

    private String rightBorderImName;
    //private double rightBorderRate;

    private String topBorderImName;
    //private double topBorderRate;

    private String bottomBorderImName;
    //private double bottomBorderRate;


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

    @Override
    public String toString() {
        return " left - " + leftBorderImName +" |"+
                " right " + rightBorderImName +" |"+
                " top " + topBorderImName +" |"+
                " bottom " + bottomBorderImName;
    }

}
