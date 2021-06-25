import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

public class LeftClickAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
        swapButtons(e);
        CheckUtils.checkSolution(buttons, solution, panel);
    }

    private final List<PuzzleButton> buttons;
    private final List<Point> solution;
    private final JPanel panel;

    public LeftClickAction(List<PuzzleButton> buttons,
                           List<Point> solution,
                           JPanel panel) {
        this.buttons = buttons;
        this.solution = solution;
        this.panel = panel;
    }

    /**
    * Each time player click on button near white button they swap
    */
    private void swapButtons(ActionEvent e) {
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
            CheckUtils.updateButtons(buttons, panel);
        }
    }
}
