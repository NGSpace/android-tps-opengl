precision mediump float;
uniform float vAlpha;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

void main() {
//    vec4 texColor = texture2D(u_Texture, v_TexCoordinate);
//    gl_FragColor = texColor;

    // 1. Sample the texture color
    vec4 texColor = texture2D(u_Texture, v_TexCoordinate);

    // 2. Apply vAlpha to the alpha component of the texture color
    //    You can either replace the texture's alpha entirely,
    //    or multiply it with vAlpha if the texture itself has varying alpha.

    // Option A: Replace the texture's alpha with vAlpha
    // vec4 finalColor = vec4(texColor.rgb, vAlpha);

    // Option B: Multiply the texture's alpha with vAlpha
    // This allows the texture's own transparency to interact with vAlpha.
    // If vAlpha is 1.0, the texture's original alpha is used.
    // If vAlpha is 0.5, the texture becomes at most 50% opaque.
    vec4 finalColor = vec4(texColor.rgb, texColor.a * vAlpha);

    // 3. Set the final fragment color
    gl_FragColor = finalColor;
}