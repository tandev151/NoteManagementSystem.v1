package com.example.notemanagement.Entity;

public class PointChart {
    private int countNote;
    private String status;

    public PointChart(int countNote, String status) {
        this.countNote = countNote;
        this.status = status;

    }

    public int getCountNote() {
        return countNote;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
