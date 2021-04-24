package com.example.notemanagement.Entity;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Relation;

public class AccountWithStatus {
    @Embedded
    public Account account;
    @Relation(
            parentColumn = "id",
            entityColumn = "accountid"
    )
    public LiveData<Status> statusList;
}
