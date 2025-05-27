package io.github.ngspace.topdownshooter.renderer.renderer;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import io.github.ngspace.topdownshooter.R;

public class Shaders {

    public static Shaders shaders;


    public final String TEXTURE_2D_VERT_SHADER;
    public final String TEXTURE_2D_FRAG_SHADER;

    public static String readShader(Context context, int id) {
        try (InputStream is = context.getResources().openRawResource(id)) {
            var reader = new Scanner(is);
            StringBuilder result = new StringBuilder();
            while (reader.hasNext()) result.append(reader.nextLine()).append("\n");
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Shaders(Context context) {
        TEXTURE_2D_VERT_SHADER = readShader(context, R.raw.texture_2d_vert);
        TEXTURE_2D_FRAG_SHADER = readShader(context, R.raw.texture_2d_frag);
    }
}