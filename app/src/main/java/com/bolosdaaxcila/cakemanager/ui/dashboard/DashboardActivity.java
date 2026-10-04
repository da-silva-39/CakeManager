package com.bolosdaaxcila.cakemanager.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.ui.auth.LoginActivity;
import com.bolosdaaxcila.cakemanager.utils.SessionManager;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SessionManager session = new SessionManager(this);
        TextView welcome = findViewById(R.id.textWelcome);
        welcome.setText(getString(R.string.welcome_user, session.getUserName()));

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }
}
