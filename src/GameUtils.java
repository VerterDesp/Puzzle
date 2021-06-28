import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GameUtils {

    private static boolean compareList(java.util.List<Point> ls1, List<Point> ls2) {
        return ls1.toString().contentEquals(ls2.toString());
    }

    /**
     * Comparing coordinates of current buttons position and right buttons position
     */
    public static void checkSolution(List<PuzzleButton> buttons,
                                     List<Point> solution,
                                     JPanel panel) {
        var current = new ArrayList<Point>();

        for (PuzzleButton btn : buttons) {
            current.add((Point) btn.getClientProperty("position"));
            if (btn.isFlipped()) {
                BufferedImage bufferedImage = flipImage(getBuffImage(btn.getIcon()));
                btn.setIcon(new ImageIcon(bufferedImage));
            }
        }

        if (compareList(solution, current)) {
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

    public static BufferedImage flipImage(BufferedImage image) {
        //BufferedImage image = getBuffImage(icon);
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

    public static BufferedImage getBuffImage(Icon icon) {
        BufferedImage im = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = im.createGraphics();
        icon.paintIcon(null, g, 0, 0); // Paint the Icon to the BufferedImage
        g.dispose();
        return im;
    }
}
