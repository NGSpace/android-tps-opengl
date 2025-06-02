package io.github.ngspace.topdownshooter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class EmailAuthActivity extends AppCompatActivity {
    private static final String TAG = "EmailAuthActivity";
    private EditText etEmail, etPassword;
    private Button btnSignIn, btnSignUp;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.USE_EXACT_ALARM)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.USE_EXACT_ALARM}, 1);
            }
        }
        scheduleNotification(86_400_000);
        setContentView(R.layout.activity_email_auth);

        // Ensure FirebaseApp is initialized
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(v -> signIn());
        btnSignUp.setOnClickListener(v -> signUp());
    }

    private void signIn() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (isInvalidateInputs(email, password)) return;

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        onSignedIn();
                    } else {
                        Exception e = task.getException();
                        Log.e(TAG, "signInWithEmail:failure", e);
                        Toast.makeText(EmailAuthActivity.this,
                                e != null ? e.getMessage() : "Sign-in failed",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void signUp() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (isInvalidateInputs(email, password)) return;

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        onSignedIn();
                    } else {
                        Exception e = task.getException();
                        Log.e(TAG, "createUserWithEmail:failure", e);
                        Toast.makeText(EmailAuthActivity.this,
                                e != null ? e.getMessage() : "Sign-up failed",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isInvalidateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required");
            return true;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("6+ characters");
            return true;
        }
        return false;
    }

    private void onSignedIn() {
        Intent intent = new Intent(this, LoadUserActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ScheduleExactAlarm")
    public void scheduleNotification(long delayMillis) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        long triggerAtMillis = System.currentTimeMillis() + delayMillis;

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }

}