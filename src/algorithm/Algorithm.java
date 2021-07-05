package algorithm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Algorithm {

    private final String folder;

    private List<File> images; // List of files from chunks folder
    private Map<String, BorderInfo> data;
    private List<String> finalSequence;
    private List<BufferedImage> finalImgSeq;

    private BufferedImage finalImage;

    // Compare algorithm settings
    private final int partThickness = 1; // Thickness of each image border to compare
    private final double defaultRate = 90.0d; // Possibility to be a neighbor

    public Algorithm(String folder) {
        this.folder = folder;
    }

    public void launch() throws Exception {
        makeData();
        detectSchema();
        makeFinalImgSeq();
        finalImage = mergeFinalImage();
    }

    private BufferedImage mergeFinalImage() throws IOException {
        BufferedImage chunk = ImageIO.read(images.get(0));
        int type = chunk.getType();
        int chunkWidth = chunk.getWidth();
        int chunkHeight = chunk.getHeight();
        int rows = getOriginalRows();
        int columns = getOriginalColumns();
        BufferedImage finalImg = new BufferedImage(
                chunkWidth * columns,
                chunkHeight * rows, type);
        int num = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                finalImg.createGraphics().drawImage(finalImgSeq.get(num),
                        chunkWidth * j,
                        chunkHeight * i,
                        null);
                num++;
            }
        }
        return finalImg;
    }

    private void makeFinalImgSeq() throws IOException {
        finalImgSeq = new ArrayList<>();
        for (String name : finalSequence) {
            for (File file : images) {
                if (file.getName().equals(name)) {
                    finalImgSeq.add(ImageIO.read(file));
                }
            }
        }
    }

    private void detectSchema() {
        finalSequence = new ArrayList<>();

        String firstInRow = findFirst(data);
        String next = getNextInRow(firstInRow);
        finalSequence.add(firstInRow);

        while (finalSequence.size() < images.size()) {
            if (next != null) {
                finalSequence.add(next);
                next = getNextInRow(next);
            } else {
                firstInRow = getNextInColumn(firstInRow);
                finalSequence.add(firstInRow);
                next = getNextInRow(firstInRow);
            }
        }
    }

    private int getOriginalRows() {
        int count = 0;
        String bottom = findFirst(data);
        while (bottom != null) {
            bottom = getNextInColumn(bottom);
            count++;
        }
        return count;
    }

    private int getOriginalColumns() {
        int count = 0;
        String right = findFirst(data);
        while (right != null) {
            right = getNextInRow(right);
            count++;
        }
        return count;
    }

    private String getNextInRow(String previous) {
        return data.get(previous).getRightBorderImName();
    }

    private String getNextInColumn(String previous) {
        return data.get(previous).getBottomBorderImName();
    }

    private String findFirst(Map<String, BorderInfo> map) {
        String first = null;
        for(Map.Entry<String, BorderInfo> m : map.entrySet()) {
            String right = m.getValue().getRightBorderImName();
            String bottom = m.getValue().getBottomBorderImName();
            String left = m.getValue().getLeftBorderImName();
            String top = m.getValue().getTopBorderImName();
            if (right != null && bottom != null &&
                left == null && top == null) {
                first = m.getKey();
            }
        }
        return first;
    }

    private void makeData() throws Exception {
        data = new HashMap<>();
        this.images = getFilesFromFolder(this.folder);

        for(File file : Objects.requireNonNull(images)) {
            BorderInfo info = new BorderInfo();

            ImageInfo rightNeighbor = findRightNeighbor(file, images);
            info.setRightBorderImName(rightNeighbor.getNeighborName());

            ImageInfo leftNeighbor = findLeftNeighbor(file, images);
            info.setLeftBorderImName(leftNeighbor.getNeighborName());

            ImageInfo topNeighbor = findTopNeighbor(file, images);
            info.setTopBorderImName(topNeighbor.getNeighborName());

            ImageInfo bottomNeighbor = findBottomNeighbor(file, images);
            info.setBottomBorderImName(bottomNeighbor.getNeighborName());

            data.put(file.getName(), info);
        }

        correctData(data);
    }

    private void correctData(Map<String, BorderInfo> rawData) {
        for(Map.Entry<String, BorderInfo> map : rawData.entrySet()) {
            BorderInfo value = map.getValue();
            String key = map.getKey();

            String rightNeighbor = value.getRightBorderImName();
            if (rightNeighbor != null) {
                String left = rawData.get(rightNeighbor).getLeftBorderImName();
                if(!left.equals(key)) {
                    value.setRightBorderImName(null);
                }
            }

            String leftNeighbor = value.getLeftBorderImName();
            if (leftNeighbor != null) {
                String right = rawData.get(leftNeighbor).getRightBorderImName();
                if (!right.equals(key)) {
                    value.setLeftBorderImName(null);
                }
            }

            String topNeighbor = value.getTopBorderImName();
            if (topNeighbor != null) {
                String bottom = rawData.get(topNeighbor).getBottomBorderImName();
                if (!bottom.equals(key)) {
                    value.setTopBorderImName(null);
                }
            }

            String bottomNeighbor = value.getBottomBorderImName();
            if (bottomNeighbor != null) {
                String top = rawData.get(bottomNeighbor).getTopBorderImName();
                if (!top.equals(key)) {
                    value.setBottomBorderImName(null);
                }
            }
        }
    }

    private ImageInfo findRightNeighbor(File file, List<File> files) throws IOException {
        BufferedImage right = rightBorder(ImageIO.read(file));
        double rate = defaultRate;
        String neighbor = null;
        for (File image : files) {
            if (!image.getName().equals(file.getName())) {
                BufferedImage left = leftBorder(ImageIO.read(image));
                double temp = compareImage(right, left);
                if (temp > rate) {
                    rate = temp;
                    neighbor = image.getName();
                }
            }
        }
        ImageInfo imageInfo = new ImageInfo();
        if(rate <= defaultRate) {
            imageInfo.setNeighborName(null);
        } else {
            imageInfo.setNeighborName(neighbor);
        }
        return imageInfo;
    }

    private ImageInfo findLeftNeighbor(File file, List<File> files) throws IOException {
        BufferedImage left = leftBorder(ImageIO.read(file));
        double rate = defaultRate;
        String neighbor = null;
        for (File image : files) {
            if (!image.getName().equals(file.getName())) {
                BufferedImage right = rightBorder(ImageIO.read(image));
                double temp = compareImage(left, right);
                if (temp > rate) {
                    rate = temp;
                    neighbor = image.getName();
                }
            }
        }
        ImageInfo imageInfo = new ImageInfo();
        if(rate <= defaultRate) {
            imageInfo.setNeighborName(null);
        } else {
            imageInfo.setNeighborName(neighbor);
        }
        return imageInfo;
    }

    private ImageInfo findTopNeighbor(File file, List<File> files) throws IOException {
        BufferedImage top = topBorder(ImageIO.read(file));
        double rate = defaultRate;
        String neighbor = null;
        for (File image : files) {
            if (!image.getName().equals(file.getName())) {
                BufferedImage bottom = bottomBorder(ImageIO.read(image));
                double temp = compareImage(top, bottom);
                if (temp > rate) {
                    rate = temp;
                    neighbor = image.getName();
                }
            }
        }
        ImageInfo imageInfo = new ImageInfo();
        if(rate <= defaultRate) {
            imageInfo.setNeighborName(null);
        } else {
            imageInfo.setNeighborName(neighbor);
        }
        return imageInfo;
    }

    private ImageInfo findBottomNeighbor(File file, List<File> files) throws IOException {
        BufferedImage bottom = bottomBorder(ImageIO.read(file));
        double rate = defaultRate;
        String neighbor = null;
        for (File image : files) {
            if (!image.getName().equals(file.getName())) {
                BufferedImage top = topBorder(ImageIO.read(image));
                double temp = compareImage(bottom, top);
                if (temp > rate) {
                    rate = temp;
                    neighbor = image.getName();
                }
            }
        }
        ImageInfo imageInfo = new ImageInfo();
        if(rate <= defaultRate) {
            imageInfo.setNeighborName(null);
        } else {
            imageInfo.setNeighborName(neighbor);
        }
        return imageInfo;
    }

    private BufferedImage leftBorder(BufferedImage im) {
        return im.getSubimage(0, 0, partThickness, im.getHeight());
    }

    private BufferedImage topBorder(BufferedImage im) {
        return im.getSubimage(0, 0, im.getWidth(), partThickness);
    }

    private BufferedImage rightBorder(BufferedImage im) {
        return im.getSubimage(im.getWidth() - partThickness, 0, partThickness, im.getHeight());
    }

    private BufferedImage bottomBorder(BufferedImage im) {
        return im.getSubimage(0, im.getHeight() - partThickness, im.getWidth(), partThickness);
    }

    private double compareImage(BufferedImage fileA, BufferedImage fileB) {
        int width1 = fileA.getWidth();
        int height1 = fileA.getHeight();

        /* Each pixel has RGB value. So cycle need to get certain
           red, green, and blue value of each pixel. And count
           difference of each rgb-colour of pixel */
        long difference = 0;
            for (int y = 0; y < height1; y++) {
                for (int x = 0; x < width1; x++) {
                    int rgbA = fileA.getRGB(x, y); // Get RGB from pixel
                    int rgbB = fileB.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff; // Get red by turning off rest bits that dont represent red
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;
                    difference += Math.abs(redA - redB); //
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                }
            }

            // Total number of red pixels = width * height
            // Total number of blue pixels = width * height
            // Total number of green pixels = width * height
            // So total number of pixels = width * height * 3
            double total_pixels = width1 * height1 * 3;

            // Normalizing the value of different pixels
            // for accuracy(average pixels per color
            // component)
            double avg_different_pixels =
                    difference / total_pixels;

            // There are 255 values of pixels in total
        return 100d - ((avg_different_pixels / 255) * 100);
    }

    private List<File> getFilesFromFolder(String folder) {
        File[] fileArray = Path.of(folder).toFile().listFiles();
        return Arrays.asList(Objects.requireNonNull(fileArray));
    }

    public BufferedImage getFinalImage() {
        return finalImage;
    }
}
