package com.example.notemanagement.Entity;

public class Chart {
    private int countNote;
    private int statusId;

    public Chart(int countNote, int statusId) {
        this.countNote = countNote;
        this.statusId = statusId;
    }
    public Chart(){}

    public int getCountNote() {
        return countNote;
    }

    public void setCountNote(int countNote) {
        this.countNote = countNote;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

}
