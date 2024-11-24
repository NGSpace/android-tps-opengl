#version 300 es
precision mediump float;
uniform vec4 vColor;
out vec4 fragColor;

in vec4 position;
void main()
{
    fragColor = vColor;
    float posx = (position.x + 3.0)/ 6.0;
    float posy1 = (position.y + 3.0)/ 6.0;
    fragColor = vec4(posx,0.0,posy1,1.0);

}