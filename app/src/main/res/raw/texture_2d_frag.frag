precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

void main() {
    vec4 texColor = texture2D(u_Texture, v_TexCoordinate);
    gl_FragColor = texColor;
}