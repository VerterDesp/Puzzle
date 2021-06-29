import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GameUtils {

    /**
     * Comparing coordinates of current buttons position and right buttons position
     */
    public static void checkSolution(List<PuzzleButton> buttons,
                                     List<Point> solution,
                                     JPanel panel) {
        List<Point> current = currentPosition(buttons);

        if (compareList(solution, current) && isButtonsNotFlipped(buttons)) {
            JOptionPane.showMessageDialog(panel, "Congratulation!!!",
                    "You did it!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Each time player moving white button whole panel updating due to his move
     */
    public static void updateButtons(List<PuzzleButton> buttons, JPanel panel) {
        panel.removeAll();
        buttons.forEach(panel::add);
        panel.validate();
    }

    /**
     * Flip image from param
     */
    public static BufferedImage flipImage(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));

        BufferedImage flippedImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = flippedImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return flippedImage;
    }

    private static boolean compareList(List<Point> ls1, List<Point> ls2) {
        return ls1.toString().contentEquals(ls2.toString());
    }

    private static boolean isButtonsNotFlipped(List<PuzzleButton> buttons) {
        return buttons.stream().noneMatch(PuzzleButton::isFlipped);
    }

    private static List<Point> currentPosition(List<PuzzleButton> buttons) {
        List<Point> current = new ArrayList<>();
        buttons.forEach(b -> current
                        .add((Point) b.getClientProperty("position")));
        return current;
    }
}
