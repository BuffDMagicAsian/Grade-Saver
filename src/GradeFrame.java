import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GradeFrame extends JFrame {
    private GradeCalculator gradeCalculator;
    private final String gradeList[] = {"F", "D-", "D", "C-", "C", "B-", "B", "A-", "A"};

    public GradeFrame(){
        gradeCalculator = new GradeCalculator();

        JTextField gradeField = new JTextField();
        gradeField.addActionListener(new GradeListener(gradeField));

    }

    private class GradeListener implements ActionListener{
        private JTextField textField;

        private GradeListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textField.getText();
            try {
                double grade = Double.parseDouble(text);
                gradeCalculator.addGrade(grade);
            } catch (NumberFormatException ex) {
                textField.setText(""); // or set to other values you want
            }

        }
    }
}
