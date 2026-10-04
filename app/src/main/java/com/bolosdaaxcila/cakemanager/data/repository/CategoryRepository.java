package com.bolosdaaxcila.cakemanager.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bolosdaaxcila.cakemanager.data.local.AppDatabase;
import com.bolosdaaxcila.cakemanager.data.local.CategoryDao;
import com.bolosdaaxcila.cakemanager.data.model.Category;

import java.util.List;

public class CategoryRepository extends BaseRepository {

    private final CategoryDao categoryDao;

    public CategoryRepository(Context context) {
        categoryDao = AppDatabase.getInstance(context).categoryDao();
    }

    public void insert(Category category, Callback<Long> callback) {
        runAsync(() -> categoryDao.insert(category), callback);
    }

    public void update(Category category) {
        runAsync(() -> categoryDao.update(category));
    }

    public void delete(Category category) {
        runAsync(() -> categoryDao.delete(category));
    }

    public LiveData<List<Category>> getAll() {
        return categoryDao.getAll();
    }

    public LiveData<List<Category>> search(String query) {
        return categoryDao.search(query);
    }

    public void findById(long id, Callback<Category> callback) {
        runAsync(() -> categoryDao.findById(id), callback);
    }
}
