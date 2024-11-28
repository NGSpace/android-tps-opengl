precision mediump float;
uniform float vAlpha;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

void main() {
    vec4 texColor = texture2D(u_Texture, v_TexCoordinate);
//    gl_FragColor = vec4(v_TexCoordinate.x, v_TexCoordinate.y, texColor.b, 1);
    gl_FragColor = texColor;
}