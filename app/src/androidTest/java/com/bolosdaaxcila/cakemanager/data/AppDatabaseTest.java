package com.bolosdaaxcila.cakemanager.data;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bolosdaaxcila.cakemanager.data.local.AppDatabase;
import com.bolosdaaxcila.cakemanager.data.model.Category;
import com.bolosdaaxcila.cakemanager.data.model.Product;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {

    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndCountCategory() {
        db.categoryDao().insert(new Category("Bolos", "Bolos variados"));
        assertEquals(1, db.categoryDao().count());
    }

    @Test
    public void insertProductAndCountByCategory() {
        long catId = db.categoryDao().insert(new Category("Bolos", ""));
        db.productDao().insert(new Product("Bolo de Chocolate", "", 1000.0, catId));
        assertEquals(1, db.productDao().countByCategory(catId));
    }
}
