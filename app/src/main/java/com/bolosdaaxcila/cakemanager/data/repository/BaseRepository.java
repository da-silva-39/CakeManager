package com.bolosdaaxcila.cakemanager.data.repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseRepository {

    protected static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public interface Callback<T> {
        void onResult(T result);
    }

    protected <T> void runAsync(java.util.concurrent.Callable<T> task, Callback<T> callback) {
        executor.execute(() -> {
            try {
                T result = task.call();
                if (callback != null) {
                    android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
                    handler.post(() -> callback.onResult(result));
                }
            } catch (Exception e) {
                if (callback != null) {
                    android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
                    handler.post(() -> callback.onResult(null));
                }
            }
        });
    }

    protected void runAsync(Runnable task) {
        executor.execute(task);
    }
}
