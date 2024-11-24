package io.github.ngspace.topdownshooter.engine.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import io.github.ngspace.topdownshooter.MainActivity;
import io.github.ngspace.topdownshooter.R;

public class Shaders {

    public static final String TEXTURE_2D_VERT_SHADER = readShader(R.raw.texture_2d_vert);
    public static final String TEXTURE_2D_FRAG_SHADER = readShader(R.raw.texture_2d_frag);
    public static final String SQUARE_VERT_SHADER = readShader(R.raw.square_vert);
    public static final String SQUARE_FRAG_SHADER = readShader(R.raw.square_frag);

    public static String readShader(int id) {
        try (InputStream is = MainActivity.globalContext.getResources().openRawResource(id)) {
            var reader = new Scanner(is);
            StringBuilder result = new StringBuilder();
            while (reader.hasNext()) result.append(reader.nextLine()).append("\n");
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}