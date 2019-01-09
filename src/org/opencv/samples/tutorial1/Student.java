package org.opencv.samples.tutorial1;
import java.util.ArrayList;

public class Student {
    int id;

    public static int QUANTITY_QUESTIONS = 7;
    public static int QUANTITY_OPTIONS = 4;

    public static ArrayList<Student> students = new ArrayList<Student>();;

    boolean[][] studentAnswer;
    boolean[][] trueAnswer;

    public Student(boolean[][] key){
        studentAnswer = new boolean[QUANTITY_QUESTIONS][QUANTITY_OPTIONS];
        trueAnswer = key;
    }

    public Student clone(){
        Student clone = new Student(trueAnswer);
        return clone;
    }

    public double calculateMark(){
        int t = 0;
        for(int i = 0; i < QUANTITY_QUESTIONS; i++){
            boolean flag = true;
            for(int j = 0;  j< QUANTITY_OPTIONS; j++){
                if(studentAnswer[i][j] != trueAnswer[i][j]){
                    flag = false;
                }
            }
            if(flag){
                t++;
            }
        }
        return 1.0*t/QUANTITY_QUESTIONS;
    }

    public void newID(){
        int newID = 0;
        for(int i = 0; i < QUANTITY_OPTIONS; i++){
            if(studentAnswer[0][i]){
                newID += Math.pow(2, i);
            }
        }
        id = newID;
    }

    public String message(){
        return Integer.toString((int) calculateMark()*100);
    }

    public boolean isInhere(){
        for(Student s : Student.students){
            if(id == s.id){
                return true;
            }
        }
        return false;
    }
}