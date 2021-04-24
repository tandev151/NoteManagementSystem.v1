package com.example.notemanagement.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Category;


import java.util.List;
@Dao
public interface CategoryDAO {

    @Insert(onConflict=OnConflictStrategy.IGNORE)
     void insertCategory(Category category) ;

    @Update(onConflict=OnConflictStrategy.IGNORE)
    void editCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("Select * from category where category_id")
    void ShowCategory(Category category);

    @Query("Select * from Category")
    List<Category> getAll();

}
