import java.awt.*;

public class App {
    public static void main( String[] args )  {
        EventQueue.invokeLater(() -> {
            PuzzleFrame puzzleFrame;
            try {
                puzzleFrame = new PuzzleFrame();
                puzzleFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
