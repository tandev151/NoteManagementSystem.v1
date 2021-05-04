package com.example.notemanagement.Entity;

import java.util.ArrayList;

public class PointChart {
    private int countNote;
    private String status;

    public PointChart(int countNote, String status) {
        this.countNote = countNote;
        this.status = status;

    }
    public PointChart(){  }

    public int getCountNote() {
        return countNote;
    }

    public void setCountNote(int countNote) {
        this.countNote = countNote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
