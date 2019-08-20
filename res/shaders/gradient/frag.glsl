#version 330 core

uniform vec4 in_color;

in vec4 slide_color;

out vec4 color;

void main()
{
    color = slide_color;
}
