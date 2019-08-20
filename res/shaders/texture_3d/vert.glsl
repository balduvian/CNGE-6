#version 330 core

layout (location = 0) in vec3 vertex;
layout (location = 1) in vec2 tex_coord;
layout (location = 2) in vec3 normal;

uniform mat4 model;
uniform mat4 projection;
uniform vec3 lighting_angle;
uniform float intensity;
uniform float ambient;

out vec2 tex_pass;
out float light_hit;

void main()
{
    tex_pass = tex_coord;
    gl_Position = (projection * model) * vec4(vertex, 1);

    light_hit = dot(normal, lighting_angle) * intensity + ambient;
}
