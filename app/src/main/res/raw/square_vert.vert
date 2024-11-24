#version 300 es
uniform mat4 uMVPMatrix;
in vec4 vPosition;

out vec4 position;
void main() {
    position = uMVPMatrix * vPosition;
    gl_Position = uMVPMatrix * vPosition;
}