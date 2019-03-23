import java.util.ArrayList;

public class GradeCalculator {
    private double weight = 0.0;
    private double classGrade = 0.0;
    private ArrayList<Double> scores = new ArrayList<>();
    private ArrayList<Double> maxScores = new ArrayList<>();

    public double getSectionGrade(){
        double scoreSum = 0;
        double maxScoreSum = 0;

        for(int score = 0; score < scores.size(); score++){
            scoreSum += scores.get(score);
        }

        for(int maxScore = 0; maxScore < maxScores.size(); maxScore++){
            maxScoreSum += maxScores.get(maxScore);
        }
        double effGrade = scoreSum * weight / maxScoreSum;
        classGrade += effGrade;

        return effGrade;
    }

    public double getClassGrade(){
        return classGrade;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public void setScore(double score) {
        scores.add(score);
    }

    public void setMaxScore(double maxScore) {
        maxScores.add(maxScore);
    }

    public void clearScores(){
        scores.clear();
        maxScores.clear();
    }
    public void clearAll(){
        scores.clear();
        maxScores.clear();
        classGrade = 0;
        weight = 0.0;
        classGrade = 0.0;
    }
}
