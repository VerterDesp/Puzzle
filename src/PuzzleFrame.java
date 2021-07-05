import algorithm.Algorithm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PuzzleFrame extends JFrame {

    private final JPanel panel = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private final int rows = 4;
    private final int columns = 3;
    private final File sourceImage = new File("src/resources/original/p.jpg");
    private final File chunksFolder = new File("src/resources/chunks");
    private final List<BufferedImage> chunks = new ArrayList<>();
    private final List<PuzzleButton> buttons = new ArrayList<>();
    private final List<Point> solution = new ArrayList<>();

    private final Random random = new Random();

    public PuzzleFrame() throws Exception {
        initUI();
    }

    private void initUI() throws Exception {
        initSolutionCoordinates(solution); // Init right position of each button

        panel.setBorder(BorderFactory.createLineBorder(Color.white));
        panel.setLayout(new GridLayout(rows, columns, 0, 0)); // Layout without any gaps between buttons

        add(panel, BorderLayout.CENTER); // Add panel to the container

        splitImage(loadImage(sourceImage), true); // Split image and init buttons

        moveChunks(chunks); // Moving image chunks to its folder

        Collections.shuffle(buttons); // Shuffle puzzle buttons
        addLastButton(buttons); // Add last button only AFTER shuffling, or it will be anywhere!!!

        addButtonsToPanel(buttons, panel); // Add puzzle buttons to game panel

        initSolveButton();

        pack(); // Sizes the frame to preferred size

        setTitle("TopPuzzle");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centralize the window
    }

    private void initSolveButton() {
        JButton jButton = new JButton("Solve");
        jButton.addActionListener(a -> autoSolve());
        add(bottomPanel, BorderLayout.EAST);
        bottomPanel.add(jButton);
    }

    private void autoSolve() {
        Algorithm al = new Algorithm(chunksFolder.getAbsolutePath());
        try {
            al.launch();
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttons.clear();
        splitImage(al.getFinalImage(), false);
        addLastButton(buttons);

        GameUtils.updateButtons(buttons, panel);
        GameUtils.checkSolution(buttons, solution, panel);
    }

    private void initSolutionCoordinates(List<Point> solution) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                solution.add(new Point(i, j));
            }
        }
    }

    private String originExt() {
        String name = sourceImage.getName();
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
            String ext = originExt();
            Iterator<Integer> random = getSet().iterator();
            for (BufferedImage im : ch) {
                String chunkPath = chunksFolder
                        .getAbsolutePath() + "/" + random.next() + "." + ext;
                File out = new File(chunkPath);
                ImageIO.write(im, ext, out);
            }
        }
    }

    private void splitImage(BufferedImage im, boolean toFlip) {
        int chunkWidth = im.getWidth() / columns;
        int chunkHeight = im.getHeight() / rows;

        int x;
        int y = 0;
        for (int i  = 0; i < rows; i++) {
            x = 0;
            for (int j = 0; j < columns; j++) {
                var chunk = im.getSubimage(x, y, chunkWidth, chunkHeight);
                initButton(chunk, i, j, toFlip);
                chunks.add(chunk);
                x += chunkWidth;
            }
            y += chunkHeight;
        }
    }

    private void initButton(BufferedImage chunk, int i, int j, boolean toFlip) {
        PuzzleButton button;
        if (toFlip && random.nextBoolean()) {
            BufferedImage flipped = GameUtils.flipImage(chunk);
            button = new PuzzleButton(flipped);
            button.setFlipped(true);
        } else
            button = new PuzzleButton(chunk);

        button.putClientProperty("position", new Point(i, j));
        button.addActionListener(new LeftClickAction(buttons, solution, panel));
        button.addMouseListener(new RightClickAction(buttons, solution, panel));

        buttons.add(button);
    }

    private PuzzleButton initLastButton() {
        PuzzleButton last = new PuzzleButton();
        last.setBorderPainted(false);
        last.setContentAreaFilled(false);
        last.setLastButton(true);
        last.putClientProperty("position", new Point(rows - 1, columns - 1));
        last.addActionListener(new LeftClickAction(buttons, solution, panel));
        return last;
    }

    private void addButtonsToPanel(List<PuzzleButton> buttons,
                                   JPanel panel) {
        buttons.forEach(panel::add);
    }

    private BufferedImage loadImage(File source) throws IOException {
        return ImageIO.read(source);
    }

    private void addLastButton(List<PuzzleButton> buttons) {
        buttons.set(buttons.size() - 1, initLastButton());
    }
}
