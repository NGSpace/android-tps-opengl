#version 300 es
precision mediump float;
uniform vec4 vColor;
out vec4 fragColor;
void main()
{
    if (fragColor.r>0.0) discard;
    fragColor = vec4(1.0,0.0,.0,1);
}