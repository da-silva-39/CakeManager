package com.bolosdaaxcila.cakemanager.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.repository.UserRepository;
import com.bolosdaaxcila.cakemanager.ui.auth.LoginActivity;
import com.bolosdaaxcila.cakemanager.utils.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SessionManager session = new SessionManager(this);
        UserRepository userRepository = new UserRepository(this);

        TextView textName = findViewById(R.id.textProfileName);
        TextView textEmail = findViewById(R.id.textProfileEmail);

        userRepository.findById(session.getUserId()).observe(this, user -> {
            if (user != null) {
                textName.setText(user.getName());
                textEmail.setText(user.getEmail());
            }
        });

        Button btnLogout = findViewById(R.id.btnProfileLogout);
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }
}
