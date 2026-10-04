package com.bolosdaaxcila.cakemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.ui.auth.LoginActivity;
import com.bolosdaaxcila.cakemanager.ui.dashboard.DashboardActivity;
import com.bolosdaaxcila.cakemanager.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SessionManager session = new SessionManager(this);
            Intent next = new Intent(this, session.isLoggedIn() ? DashboardActivity.class : LoginActivity.class);
            startActivity(next);
            finish();
        }, 1500);
    }
}
