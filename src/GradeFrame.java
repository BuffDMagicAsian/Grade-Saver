import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class GradeFrame extends JFrame {
    private FileHandler fileHandler = new FileHandler();
    private GradeCalculator gradeCalculator = GradeViewer.calculator;
    private DefaultTableModel model;
    private JTextField weightField = new JTextField(5);
    private JLabel effectiveGrade = new JLabel();
    private JLabel classGrade = new JLabel();
    private JButton confirmButton;
    private JTable table;
    JPanel gradePanel;

    public GradeFrame() {
        JPanel panel = new JPanel();
        gradePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel confirmPanel = new JPanel();
        JPanel weightPanel = new JPanel();

        gradePanel.add(effectiveGrade);
        gradePanel.add(classGrade);
        gradePanel.setVisible(false);

        JLabel weightLabel = new JLabel("Section Weight: ", SwingConstants.LEFT);

        weightPanel.add(weightLabel);
        weightPanel.add(weightField);

        JButton addRowButton = new JButton("Add Assignment");
        addRowButton.addActionListener(new AddRowListener());

        JButton delRowButton = new JButton("Delete Assignment");
        delRowButton.addActionListener(new DeleteRowListener());

        JButton calculateGradeButton = new JButton("Calculate Grade");
        calculateGradeButton.addActionListener(new CalculateClassListener());

        JButton addSectionButton = new JButton("Add Weighted Section");
        addSectionButton.addActionListener(new AddSectionListener());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearButtonListener());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonListener());

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ConfirmListener());
        confirmButton.setVisible(false);


        buttonPanel.add(addRowButton);
        buttonPanel.add(delRowButton);
        buttonPanel.add(calculateGradeButton);
        buttonPanel.add(addSectionButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);

        confirmPanel.add(confirmButton);

        if(fileHandler.getAssignments() != null){
            model = new DefaultTableModel(fileHandler.getAssignments(), new Object[]{"Assignment", "Your Score", "Max Score"}){

                public Class getColumnClass(int column)
                {
                    return column == 3 ? Boolean.class : Object.class;
                }
            };
            table = new JTable(model);
        }
        else{
            model = new DefaultTableModel(){

                public Class getColumnClass(int column)
                {
                    return column == 3 ? Boolean.class : Object.class;
                }
            };
            table = new JTable(model);
            model.addColumn("Assignment");
            model.addColumn("Your Score");
            model.addColumn("Max Score");
            model.addRow(new String[]{"Assignment", "Your Score", "Max Score"});
            model.addRow(new Object[]{null, null, null});
        }

        panel.add(buttonPanel);
        panel.add(weightPanel);
        panel.add(table);
        panel.add(gradePanel);
        panel.add(confirmPanel);

        add(panel);
    }

    public void displayError(String infoMessage, String titleBar) {
            JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    private class CalculateClassListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                storeTableData();
                double sectionGrade = gradeCalculator.getSectionGrade();
                double totalGrade = gradeCalculator.getClassGrade();
                displaySectionGrade(sectionGrade);
                displayClassGrade(totalGrade);
            }
            catch(NumberFormatException ex){
                displayError("Please enter valid numbers in the boxes", "Invalid Input");
            }
            catch(NullPointerException ex2){
                displayError("Please fill out all values in the text boxes", "No Input");
            }
            gradePanel.setVisible(true);
            repaint();
        }

        private void displaySectionGrade(double sectionGrade){
            effectiveGrade.setText("Section Grade: " + sectionGrade);
        }
        private void displayClassGrade(double totalGrade) {
            classGrade.setText("Total Class Grade: " + totalGrade);
        }

        private void storeTableData() {
            gradeCalculator.clearScores();
            for (int row = 1; row < model.getRowCount(); row++) {
                for (int column = 1; column < 3; column++) {
                    Object value = model.getValueAt(row, column);
                    if (column == 1) {
                        gradeCalculator.setScore(Double.parseDouble(value.toString()));

                    }
                    else if (column == 2) {
                        gradeCalculator.setMaxScore(Double.parseDouble(value.toString()));
                    }
                }
            }
            double weight = Double.parseDouble(weightField.getText());
            gradeCalculator.setWeight(weight);
        }
    }

     private class AddRowListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            addRow();
            repaint();
        }
        private void addRow() {
            model.addRow(new Object[]{null, null, null});
        }
    }

    private class DeleteRowListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getColumnCount() < 4) {
                Object[] checkBoxes = new Object[model.getRowCount()];
                for (int row = 1; row < checkBoxes.length; row++) {
                    checkBoxes[row] = Boolean.FALSE;
                }
                model.addColumn("Delete", checkBoxes);
                confirmButton.setVisible(true);

                repaint();
            }
        }

    }

    private class AddSectionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addSection();
        }

        /**
         * Adds another grading section (assignment, participation, etc.)
         */
        private void addSection() {
            GradeViewer viewer = new GradeViewer();
            viewer.main(new String[]{});
        }
    }

    private class ClearButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            clearAll();
            repaint();
        }

        private void clearAll() {
            gradeCalculator.clearAll();
            for(int row = model.getRowCount(); row >= 2; row--){
                model.removeRow(row - 1);
            }
            if(model.getColumnCount() > 3){
                model.setColumnCount(3);
            }
            gradePanel.setVisible(false);
            confirmButton.setVisible(false);
        }
    }

    private class ConfirmListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(model.getColumnCount() == 4){
                for (int row = 1; row < model.getRowCount(); row++) {
                    Object value = model.getValueAt(row, 3);
                    System.out.println(value);
                    if(Boolean.TRUE.equals(value)){
                        model.removeRow(row);
                    }
                }
            }
            confirmButton.setVisible(false);
            model.setColumnCount(3);
        }
    }

    private class SaveButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try(PrintWriter writer = new PrintWriter("grades.csv")){
                writer.print("");
            }
            catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            for (int row = 1; row < model.getRowCount(); row++) {
                Object[] assignment = new Object[3];
                for (int column = 0; column < 3; column++) {
                    assignment[column] = model.getValueAt(row, column);
                }
                fileHandler.recordAssignment(assignment);
            }
        }
    }

}


