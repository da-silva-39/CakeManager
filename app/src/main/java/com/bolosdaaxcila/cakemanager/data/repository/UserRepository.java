package com.bolosdaaxcila.cakemanager.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bolosdaaxcila.cakemanager.data.local.AppDatabase;
import com.bolosdaaxcila.cakemanager.data.local.UserDao;
import com.bolosdaaxcila.cakemanager.data.model.User;

public class UserRepository extends BaseRepository {

    private final UserDao userDao;

    public UserRepository(Context context) {
        userDao = AppDatabase.getInstance(context).userDao();
    }

    public void insert(User user, Callback<Long> callback) {
        runAsync(() -> userDao.insert(user), callback);
    }

    public void findByEmail(String email, Callback<User> callback) {
        runAsync(() -> userDao.findByEmail(email), callback);
    }

    public LiveData<User> findById(long id) {
        return userDao.findById(id);
    }
}
