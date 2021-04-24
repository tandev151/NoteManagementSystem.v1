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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Category category);

    @Update (onConflict = OnConflictStrategy.IGNORE)
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM category where categoryId = (:idCategory)")
    void deleteById(int idCategory);

    @Query("DELETE FROM category")
    void deleteAll();

    @Query("SELECT * FROM Category WHERE Category.categoryId =:id")
    LiveData<Category> getCategory(int id);

    @Query("SELECT * FROM Category WHERE userId = (:idUser)")
    public LiveData<List<Category>> getCategoryByUser(int idUser);

    @Query("SELECT * FROM Category where Category.userId = :idUser")
    LiveData<List<Category>> getAllCategory(int idUser);



    @Query("Select * from Category")
    List<Category> getAll();

}
