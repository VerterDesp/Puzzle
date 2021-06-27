import javax.swing.*;
import java.awt.*;
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

        for (JComponent btn : buttons) {
            current.add((Point) btn.getClientProperty("position"));
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
        for (JComponent btn : buttons) {
            panel.add(btn);
        }
        panel.validate();
    }
}
