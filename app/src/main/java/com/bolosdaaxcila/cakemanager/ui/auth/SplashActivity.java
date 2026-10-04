package com.bolosdaaxcila.cakemanager.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.ui.dashboard.DashboardActivity;
import com.bolosdaaxcila.cakemanager.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SessionManager session = new SessionManager(this);
            Intent next = new Intent(this, session.isLoggedIn() ? DashboardActivity.class : LoginActivity.class);
            startActivity(next);
            finish();
        }, 1500);
    }
}
