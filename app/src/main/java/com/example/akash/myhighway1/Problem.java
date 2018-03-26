package com.example.akash.myhighway1;

import android.content.Context;

/**
 * Created by Vishal on 3/23/2018.
 */

public class Problem {

    Context context;
    int problemcode;
    String problem_loc1;
    String problem_loc2;
    String problem_loc3;
    String time1;
    String time2;
    String time3;
    String device_number;
    String speed;
    String status;

    public Problem() {
    }

    public Problem(Context context) {
        this.context=context;
    }

    public Problem(Context context,int problemcode, String problem_loc1, String problem_loc2, String problem_loc3, String time1, String time2, String time3, String device_number, String speed, String status) {
        this.context=context;
        this.problemcode = problemcode;
        this.problem_loc1 = problem_loc1;
        this.problem_loc2 = problem_loc2;
        this.problem_loc3 = problem_loc3;
        this.time1 = time1;
        this.time2 = time2;
        this.time3 = time3;
        this.device_number = device_number;
        this.speed = speed;
        this.status = status;
    }

    public int getProblemcode() {
        return problemcode;
    }

    public void setProblemcode(int problemcode) {
        this.problemcode = problemcode;
    }

    public String getProblem_loc1() {
        return problem_loc1;
    }

    public void setProblem_loc1(String problem_loc1) {
        this.problem_loc1 = problem_loc1;
    }

    public String getProblem_loc2() {
        return problem_loc2;
    }

    public void setProblem_loc2(String problem_loc2) {
        this.problem_loc2 = problem_loc2;
    }

    public String getProblem_loc3() {
        return problem_loc3;
    }

    public void setProblem_loc3(String problem_loc3) {
        this.problem_loc3 = problem_loc3;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getDevice_number() {
        return device_number;
    }

    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
