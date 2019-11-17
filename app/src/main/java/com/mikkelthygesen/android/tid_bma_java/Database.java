package com.mikkelthygesen.android.tid_bma_java;

public class Database {

    public int bubbleSeekBarExercise;
    public int currentExerciseTime;
    public int timeSpentOnBlackListApp;


    private static Database instance;

    private Database(){

    }

    public static Database getinstance(){
        if (instance == null)
            instance = new Database();

        return instance;

    }

}
