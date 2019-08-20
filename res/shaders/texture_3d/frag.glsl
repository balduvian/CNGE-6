#version 330 core

uniform sampler2D tex;
uniform vec4 in_color;

in vec2 tex_pass;
in float light_hit;

out vec4 color;

void main()
{
    color = texture(tex, tex_pass) * in_color * vec4(light_hit,light_hit,light_hit, 1);
}
