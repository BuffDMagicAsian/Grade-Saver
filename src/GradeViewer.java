import javax.swing.*;

public class GradeViewer {
    static GradeCalculator calculator = new GradeCalculator();
    public static void main(String[] args) {
        JFrame frame = new GradeFrame();

        frame.setTitle("Grade Saver");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
