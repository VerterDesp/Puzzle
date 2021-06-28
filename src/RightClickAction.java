import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class RightClickAction extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            JButton button = (JButton) e.getSource();

            BufferedImage flippedImage = GameUtils.flipImage(GameUtils.getBuffImage(button.getIcon()));

            button.setIcon(new ImageIcon(flippedImage));
        }
    }
}
