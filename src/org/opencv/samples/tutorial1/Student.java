package org.opencv.samples.tutorial1;
import java.util.ArrayList;

public class Student {
    int id;

    public static int QUANTITY_QUESTIONS = 7;
    public static int QUANTITY_OPTIONS = 4;

    boolean[][] studentAnswer;
    boolean[][] trueAnswer;

    public Student(boolean[][] result, boolean[][] key){
        studentAnswer = clone(result);
        trueAnswer = clone(key);
        newID();
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
        return t;//QUANTITY_QUESTIONS;
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
        return Integer.toString((int) calculateMark()) + " of " + Integer.toString(QUANTITY_QUESTIONS);
    }

    public boolean[][] clone(boolean[][] input){
        boolean[][] output = new boolean[input.length][input[0].length];
        for(int i = 0; i < input.length; i++){
            for(int j = 0; j < input[i].length; j++){
                output[i][j] = input[i][j];
            }
        }
        return output;
    }
}