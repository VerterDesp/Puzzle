import java.awt.*;
import java.io.IOException;


public class App {
    public static void main( String[] args )  {
        EventQueue.invokeLater(() -> {
            PuzzleFrame puzzleFrame;
            try {
                puzzleFrame = new PuzzleFrame();
                puzzleFrame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
