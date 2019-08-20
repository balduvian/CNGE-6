#version 330 core

layout (location = 0) in vec3 vertex;

uniform mat4 model;
uniform mat4 projection;

uniform vec4 color_1;
uniform vec4 color_2;

out vec4 slide_color;

void main()
{
    slide_color = vertex.y * color_1 + (1 - vertex.y) * color_2;

    gl_Position = (projection * model) * vec4(vertex, 1);
}
