package com.codebyfelipe.appinventarios.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.codebyfelipe.appinventarios.data.local.dao.ProductoDao;
import com.codebyfelipe.appinventarios.data.local.entity.ProductoEntity;

@Database(entities = {ProductoEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {  //clase que define la base de datos

    public abstract ProductoDao productoDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "inventario_db"
                            )
                            .fallbackToDestructiveMigration() // borra y recrea si cambia el esquema
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}