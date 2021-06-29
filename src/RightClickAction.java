import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class RightClickAction extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            PuzzleButton button = (PuzzleButton) e.getSource();

            BufferedImage flipped = GameUtils.flipImage(
                    getBuffImage(button.getIcon()));

            button.setIcon(new ImageIcon(flipped));

            button.setFlipped(!button.isFlipped());
        }
        GameUtils.checkSolution(buttons, solution, panel);
    }

    private final List<PuzzleButton> buttons;
    private final List<Point> solution;
    private final JPanel panel;

    public RightClickAction(List<PuzzleButton> buttons, List<Point> solution, JPanel panel) {
        this.buttons = buttons;
        this.solution = solution;
        this.panel = panel;
    }

    private BufferedImage getBuffImage(Icon icon) {
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
