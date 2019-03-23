import java.io.*;
import java.util.Scanner;

public class FileHandler {
    private Object[][] assignmentData;
    public void recordAssignment(Object[] assignment){
        try(PrintWriter writer = new PrintWriter(new FileWriter("grades.csv", true))){
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < assignment.length; i++) {
                builder.append(assignment[i]);
                builder.append(",");
            }
            builder.append("\n");
            writer.append(builder.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object[][] getAssignments(){
        int numLine = 1;

        try {
            Scanner lineReader = new Scanner(new File("grades.csv"));
            lineReader.useDelimiter(",");
            while(lineReader.hasNext() && !lineReader.nextLine().isEmpty()){
                numLine++;
            }
            assignmentData = new Object[numLine][3];
            assignmentData[0] = new Object[]{"Assignment", "Your Score", "Max Score"};

            Scanner reader = new Scanner(new File("grades.csv"));
            reader.useDelimiter(",");
            for(int row = 1; row < numLine; row++){
                for(int col = 0; col < 3; col ++){
                    if(reader.hasNext()){
                        assignmentData[row][col] = reader.next();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("grades.csv has not been created, a new table will be generated.");
            e.printStackTrace();
        }
        return assignmentData;
    }

}
