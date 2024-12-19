package io.github.ngspace.topdownshooter.utils;

import android.util.Log;

public class Logcat {
    public static void log(Object... o) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i<o.length;i++) {
            stringBuilder.append(o[i]).append(i + 1 < o.length ? ", " : "");
        }
        Log.i("NGSPACEly", stringBuilder.toString());
    }
}
