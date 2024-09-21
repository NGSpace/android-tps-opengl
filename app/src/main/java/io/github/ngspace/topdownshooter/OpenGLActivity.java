package io.github.ngspace.topdownshooter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OpenGLActivity extends Activity {

    private GLSurfaceView gLView;

    public static int realWidth;
    public static int realHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// TODO Remove this when done debugging
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);// | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

        Rect rectangle = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            realWidth = getWindow().getWindowManager().getMaximumWindowMetrics().getBounds().width();//displayMetrics.widthPixels;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            realHeight = getWindow().getWindowManager().getMaximumWindowMetrics().getBounds().height();//displayMetrics.widthPixels;
        }
//        realHeight = displayMetrics.heightPixels;

        Log.i("NGSPACEly", "     " + realWidth + "  " + realHeight);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}