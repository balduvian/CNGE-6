#version 330 core

uniform sampler2D tex;

uniform vec4 in_color;
uniform vec2 range;

in vec2 tex_pass;
in float depth;

out vec4 color;

void main()
{
    color = texture(tex, tex_pass) * in_color;
    color.w = (1 - smoothstep(range.x, range.y, depth));
}
