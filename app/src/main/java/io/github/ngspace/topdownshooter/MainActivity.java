package io.github.ngspace.topdownshooter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.github.ngspace.topdownshooter.renderer.OpenGLActivity;
import io.github.ngspace.topdownshooter.renderer.renderer.Shaders;

public class MainActivity extends AppCompatActivity {

    public static Context globalContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Shaders.loadShaders(this);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// TODO Remove this when done debugging
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        globalContext = getApplicationContext();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        var p = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(p);
        OpenGLActivity.realWidth = p.x;
        OpenGLActivity.realHeight = p.y;

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(()-> {
            Intent intent = new Intent(this, TestGameScene.class);
            startActivity(intent);
        }, 1);//TODO one day change this back to 3000

        findViewById(R.id.rotatinglogo).animate().setInterpolator(new LinearInterpolator()).setDuration(3500).rotation(410).start();
    }
}