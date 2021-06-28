import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Implementation of one image piece
 */
public class PuzzleButton extends JButton {

    private boolean lastButton;

    private boolean flipped;

    public PuzzleButton() {
        initUI();
    }

    public PuzzleButton(Image image) {
        super(new ImageIcon(image));
        initUI();
    }

    /**
     * Make button borders colorful
     */
    private void initUI() {
        lastButton = false;
        flipped = false;

        setBorder(BorderFactory.createLineBorder(Color.white));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.black));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.white));
            }
        });
    }

    public boolean isLastButton() {
        return lastButton;
    }

    public void setLastButton(boolean lastButton) {
        this.lastButton = lastButton;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
}
