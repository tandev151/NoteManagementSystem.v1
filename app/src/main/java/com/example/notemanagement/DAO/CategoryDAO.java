package com.example.notemanagement.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Priority;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Category category);

    @Update (onConflict = OnConflictStrategy.IGNORE)
    void update(Category category);

    @Query("UPDATE Category SET Name= (:nameCategory) WHERE CategoryId= (:categoryId) AND IsDeleted=0")
    void updateNameCategory(int categoryId, String nameCategory);

    @Query("UPDATE CATEGORY SET IsDeleted= 1 where categoryId = (:idCategory)")
    void deleteById(int idCategory);

    @Query("SELECT * FROM Category WHERE Category.categoryId =(:id) AND IsDeleted=0")
    LiveData<Category> getCategory(int id);

    @Query("SELECT * FROM Category WHERE AccountId = (:accountId) AND IsDeleted=0")
    public LiveData<List<Category>> getCategoryByUser(int accountId);

    @Query("SELECT * FROM Category where Category.AccountId = (:accountId) AND IsDeleted=0")
    LiveData<List<Category>> getAllCategory(int accountId);

    @Query("SELECT * FROM Category WHERE Name = (:nameCategory) AND IsDeleted=0")
    Category getCategoryByName(String nameCategory);

    @Query("SELECT * FROM Category WHERE AccountId = (:accountId) AND IsDeleted=0")
    List<Category> getAllByUserId(int accountId);


}
