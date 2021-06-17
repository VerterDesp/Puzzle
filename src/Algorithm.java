import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Algorithm {

    private static final File source = new File("src/resources/chunks"); // Delete
    private final String folder;


    private List<File> images; // List of files from chunks folder
    private Map<String, BorderInfo> data;
    private Map<String, File> fileNames;
    private List<File> finalFile;

    /**
     * Compare algorithm settings
     */
    private final int partThickness = 1; // Thickness of each image border to compare
    private final double defaultRate = 90.0d; // Possibility to be a neighbor

//    public Algorithm(String folder, int width, int height) {
//        this.folder = folder;
//        this.originalWidth = width;
//        this.originalHeight = height;
//    }

    public Algorithm(String folder) {
        this.folder = folder;
    }

    private void launch() throws Exception {
        makeData();
    }

    //for(Map.Entry<String, BorderInfo> m : data.entrySet())

    private void detectSchema() {
        finalFile = new ArrayList<>();
        fileNames = getFileNames(images);

        int first = images.indexOf(fileNames.get(findFirst(data)));
        finalFile.add(images.get(first));

        String next = getNextInRow(findFirst(data));

        while (next != null) {
            finalFile.add(fileNames.get(next));
            next = getNextInRow(next);
        } //

    }

    private String getNextInColumn(String previous) {
        return data.get(previous).getBottomBorderImName();
    }

    private String getNextInRow(String previous) {
        return data.get(previous).getRightBorderImName();
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

    private Map<String, File> getFileNames(List<File> fileList) {
        Map<String, File> namesFiles = new HashMap<>();
        for (File f : fileList)
            namesFiles.put(f.getName(), f);
        return namesFiles;
    }

    private void makeData() throws Exception {
        data = new HashMap<>();
        this.images = getFilesFromFolder(this.folder);

        for(File file : Objects.requireNonNull(images)) {
            BorderInfo info = new BorderInfo();

            ImageInfo rightNeighbor = findRightNeighbor(file, images);
            info.setRightBorderImName(rightNeighbor.getNeighborName());
            //info.setRightBorderRate(rightNeighbor.getRate());

            ImageInfo leftNeighbor = findLeftNeighbor(file, images);
            info.setLeftBorderImName(leftNeighbor.getNeighborName());
            //info.setLeftBorderRate(leftNeighbor.getRate());

            ImageInfo topNeighbor = findTopNeighbor(file, images);
            info.setTopBorderImName(topNeighbor.getNeighborName());
            //info.setTopBorderRate(topNeighbor.getRate());

            ImageInfo bottomNeighbor = findBottomNeighbor(file, images);
            info.setBottomBorderImName(bottomNeighbor.getNeighborName());
            //info.setBottomBorderRate(bottomNeighbor.getRate());

            data.put(file.getName(), info);
        }

        correctData(data);

        for(Map.Entry<String, BorderInfo> m : data.entrySet()) // Delete
            System.out.println(m);                            // Delete
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
                    //value.setRightBorderRate(0.0d);
                }
            }

            String leftNeighbor = value.getLeftBorderImName();
            if (leftNeighbor != null) {
                String right = rawData.get(leftNeighbor).getRightBorderImName();
                if (!right.equals(key)) {
                    value.setLeftBorderImName(null);
                    //value.setLeftBorderRate(0.0d);
                }
            }

            String topNeighbor = value.getTopBorderImName();
            if (topNeighbor != null) {
                String bottom = rawData.get(topNeighbor).getBottomBorderImName();
                if (!bottom.equals(key)) {
                    value.setTopBorderImName(null);
                    //value.setTopBorderRate(0.0d);
                }
            }

            String bottomNeighbor = value.getBottomBorderImName();
            if (bottomNeighbor != null) {
                String top = rawData.get(bottomNeighbor).getTopBorderImName();
                if (!top.equals(key)) {
                    value.setBottomBorderImName(null);
                    //value.setBottomBorderRate(0.0d);
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
            //imageInfo.setRate(0.0d);
        } else {
            imageInfo.setNeighborName(neighbor);
            //imageInfo.setRate(rate);
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
            //imageInfo.setRate(0.0d);
        } else {
            imageInfo.setNeighborName(neighbor);
            //imageInfo.setRate(rate);
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
            //imageInfo.setRate(0.0d);
        } else {
            imageInfo.setNeighborName(neighbor);
            //imageInfo.setRate(rate);
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
            //imageInfo.setRate(0.0d);
        } else {
            imageInfo.setNeighborName(neighbor);
            //imageInfo.setRate(rate);
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

        long difference = 0;
            for (int y = 0; y < height1; y++) {
                for (int x = 0; x < width1; x++) {
                    int rgbA = fileA.getRGB(x, y);
                    int rgbB = fileB.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;
                    difference += Math.abs(redA - redB);
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

    private List<BufferedImage> getImagesFromFolder(String folder) throws Exception {
        var files = getFilesFromFolder(folder);
        List<BufferedImage> imagesChunks;
        if (files != null && files.size() > 0) {
            imagesChunks = new ArrayList<>();
            for (File fl : files)
                imagesChunks.add(ImageIO.read(fl));
        } else
            throw new Exception("Wrong path to files");
        return imagesChunks;
    }

    private List<File> getFilesFromFolder(String folder) {
        File[] fileArray = Path.of(folder).toFile().listFiles();
        return Arrays.asList(Objects.requireNonNull(fileArray));
    }

    public static void main(String[] args) throws Exception {
        Algorithm algorithm = new Algorithm(source.getAbsolutePath());
        algorithm.makeData();
    }
}
