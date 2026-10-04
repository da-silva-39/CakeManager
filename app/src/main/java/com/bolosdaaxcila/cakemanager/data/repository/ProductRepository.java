package com.bolosdaaxcila.cakemanager.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bolosdaaxcila.cakemanager.data.local.AppDatabase;
import com.bolosdaaxcila.cakemanager.data.local.ProductDao;
import com.bolosdaaxcila.cakemanager.data.model.Product;

import java.util.List;

public class ProductRepository extends BaseRepository {

    private final ProductDao productDao;

    public ProductRepository(Context context) {
        productDao = AppDatabase.getInstance(context).productDao();
    }

    public void insert(Product product, Callback<Long> callback) {
        runAsync(() -> productDao.insert(product), callback);
    }

    public void update(Product product) {
        runAsync(() -> productDao.update(product));
    }

    public void delete(Product product) {
        runAsync(() -> productDao.delete(product));
    }

    public LiveData<List<Product>> getAll() {
        return productDao.getAll();
    }

    public LiveData<List<Product>> search(String query) {
        return productDao.search(query);
    }

    public void findById(long id, Callback<Product> callback) {
        runAsync(() -> productDao.findById(id), callback);
    }

    public void countByCategory(long categoryId, Callback<Integer> callback) {
        runAsync(() -> productDao.countByCategory(categoryId), callback);
    }
}
