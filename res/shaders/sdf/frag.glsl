#version 330 core

uniform sampler2D tex;

uniform vec4 in_color;

in vec2 tex_pass;

out vec4 color;

void main()
{
    // grab distance field color from texture
    vec4 pass_color = texture(tex, tex_pass);

    // two of the three channels must be greater than the threshold
    // try all possible combinations and use * as an "and" operator
    // use += as an "or" operator
    float alpha  = step(0.5, pass_color.x) * step(0.5, pass_color.y);
          alpha += step(0.5, pass_color.y) * step(0.5, pass_color.z);
          alpha += step(0.5, pass_color.z) * step(0.5, pass_color.x);

    // the output uses alpha to determine if it's shown
    color = vec4(in_color.x, in_color.y, in_color.z, alpha);
}
