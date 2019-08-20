#version 330 core

uniform vec4 in_color;
uniform vec2 range;

in float depth;

out vec4 color;

void main()
{
    color = in_color;
    color.w = smoothstep(range.x, range.y, depth);
}
