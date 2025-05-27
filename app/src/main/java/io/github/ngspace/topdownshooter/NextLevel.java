package io.github.ngspace.topdownshooter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.firebase.database.FirebaseDatabase;

import io.github.ngspace.topdownshooter.utils.UserData;

public class NextLevel extends AppCompatActivity {

    private boolean saved;
    UserData usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_next_level);



        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        usr = (UserData) getIntent().getSerializableExtra("UserData");
        boolean success = getIntent().getBooleanExtra("Success",false);

        if (success) {
            usr.run++;
            ((TextView) findViewById(R.id.NextLevelText)).setText("Survived!");
            FirebaseDatabase.getInstance().getReference(usr.firebaseUid).setValue(usr).addOnCompleteListener(r -> {
                ((Button) findViewById(R.id.button2)).setText("Next");
                saved = true;
            });
            return;
        }

        ((TextView) findViewById(R.id.NextLevelText)).setText("Game over!");

        // Delete data

        var temp = usr.firebaseUid;
        usr = new UserData();
        usr.firebaseUid = temp;

        FirebaseDatabase.getInstance().getReference(usr.firebaseUid).setValue(usr).addOnCompleteListener(r -> {
            ((Button) findViewById(R.id.button2)).setText("Next");
            saved = true;
        });
    }

    public void next(View view) {
        if (saved) {
            Intent intent = new Intent(this, HomeScreen.class);
            intent.putExtra("UserData", usr);
            startActivity(intent);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {}
}