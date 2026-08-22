package com.codebyfelipe.appinventarios.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.codebyfelipe.appinventarios.data.local.entity.ProductoEntity;
import java.util.List;

@Dao
public interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductoEntity> productos);

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    List<ProductoEntity> getAllSync();

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    LiveData<List<ProductoEntity>> getAllLiveData();

    @Query("DELETE FROM productos")
    void deleteAll();
}