import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PuzzleFrame extends JFrame {

    private final JPanel panel = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private final int rows = 4;
    private final int columns = 3;
    private final File sourceImage = new File("src/resources/original/gep.jpeg");
    private final File chunksFolder = new File("src/resources/chunks");
    private final List<BufferedImage> chunks = new ArrayList<>();
    private final List<PuzzleButton> buttons = new ArrayList<>();

    private PuzzleButton lastButton;
    private List<Point> solution;

    public PuzzleFrame() throws IOException {
        initUI();
    }

    private void initUI() throws IOException {
        initSolutionCoordinates(); // Coordinates for checking solution each time per moving last button

        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setLayout(new GridLayout(rows, columns, 0, 0));

        add(panel, BorderLayout.CENTER); // Add panel to the container

        splitImage(loadImage(sourceImage)); // Split image and init buttons

        moveChunks(chunks); // Moving image chunks to its folder

        //Collections.shuffle(buttons); // Shuffle puzzle buttons
        buttons.add(lastButton); // Put the white button to puzzle buttons

        addButtonsToPanel(buttons); // Add puzzle buttons to game panel

        pack(); // Sizes the frame to preferred size

        setTitle("TopPuzzle");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        autoSolve(chunksFolder.getAbsolutePath());


    }

    private void autoSolve(String folder) throws IOException {

    }

    private void initSolutionCoordinates() {
        solution = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                solution.add(new Point(i, j));
            }
        }
    }

    private String originExt() {
        var name = sourceImage.getName();
        return name.substring(name.indexOf(".") + 1);
    }

    private Set<Integer> getSet() {
        return new Random()
                .ints(0,Integer.MAX_VALUE)
                .distinct()
                .limit(rows * columns)
                .boxed()
                .collect(Collectors.toSet());
    }

    private void moveChunks(List<BufferedImage> ch) throws IOException {
        if (Objects.requireNonNull(chunksFolder.list()).length == 0) {
            var ext = originExt();
            var random = getSet().iterator();
            for (BufferedImage im : ch) {
                String chunkPath = chunksFolder
                        .getAbsolutePath() + "/" + random.next() + "." + ext;
                File out = new File(chunkPath);
                ImageIO.write(im, ext, out);
            }
        }
    }

    private void splitImage(BufferedImage im) {
        int chunkWidth = im.getWidth() / columns;
        int chunkHeight = im.getHeight() / rows;

        int x = 0;
        int y;
        for (int i  = 0; i < rows; i++) {
            y = 0;
            for (int j = 0; j < columns; j++) {
                var chunk = im.getSubimage(y, x, chunkWidth, chunkHeight);
                initButton(chunk, i, j);
                chunks.add(chunk);
                y += chunkWidth;
            }
            x += chunkHeight;
        }
    }

    private void initButton(BufferedImage chunk, int i, int j) {
        var button = new PuzzleButton(chunk);
        button.putClientProperty("position", new Point(i, j));

        if (i == (rows - 1) && j == (columns - 1)) {
            lastButton = new PuzzleButton();
            lastButton.setBorderPainted(false);
            lastButton.setContentAreaFilled(false);
            lastButton.setLastButton(true);
            lastButton.putClientProperty("position", new Point(i, j));
        } else {
            buttons.add(button);
        }
    }

    private void addButtonsToPanel(List<PuzzleButton> buttons) {
        for (PuzzleButton btn : buttons) {
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction());
        }
    }

    private BufferedImage loadImage(File source) throws IOException {
        return ImageIO.read(source);
    }

    private void checkSolution() {
        var current = new ArrayList<Point>();

        for (JComponent btn : buttons) {
            current.add((Point) btn.getClientProperty("position"));
        }

        if (compareList(solution, current)) {
            JOptionPane.showMessageDialog(panel, "Finished",
                    "You did it!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public boolean compareList(List<Point> ls1, List<Point> ls2) {
        return ls1.toString().contentEquals(ls2.toString());
    }

    private class ClickAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            checkButton(e);
            checkSolution();
        }

        /**
         * Each time player click on button near white button they swap
         */
        private void checkButton(ActionEvent e) {
            int lidx = 0;
            for (PuzzleButton button : buttons) {
                if (button.isLastButton()) {
                    lidx = buttons.indexOf(button);
                }
            }

            var button = (JButton) e.getSource();
            int bidx = buttons.indexOf(button);

            if ((bidx - 1 == lidx) || (bidx + 1 == lidx)
                    || (bidx - 3 == lidx) || (bidx + 3 == lidx)) {
                Collections.swap(buttons, bidx, lidx);
                updateButtons();
            }
        }

        /**
         * Each time player moving white button whole panel updating due to his move
         */
        private void updateButtons() {
            panel.removeAll();
            for (JComponent btn : buttons) {
                panel.add(btn);
            }
            panel.validate();
        }
    }
}
