#version 330 core

layout (location = 0) in vec3 vertex;
layout (location = 1) in vec2 tex_coord;

uniform mat4 model;
uniform mat4 projection;

uniform vec2 screen_size;
uniform float scan_lines;
uniform float radius;
uniform float random;

out vec2 tex_pass;
out float pass_scan_lines;
out float pass_noise_lines;
out float scan_height;
out float noise_height;

out vec2 red_off;
out vec2 green_off;
out vec2 blue_off;

out vec2 pass_radius;
out float pass_random;

out vec2 vhs_position;

vec2 rand(vec2 seed, vec2 p_r)
{
    vec2 value = vec2(fract(sin(seed.x) * 15.12229 + random), fract(sin(seed.y) * 9.413567 + random));
    value *= p_r * 2;
    value -= p_r;

    return value;
}

void main()
{
    vec2 pixel_size = 1 / screen_size;

    scan_height = 1 / scan_lines;
    pass_scan_lines = scan_lines;

    pass_noise_lines = scan_lines * 2.35;
    noise_height = 1 / pass_noise_lines;

    tex_pass = tex_coord;

    gl_Position = (projection * model) * vec4(vertex, 1);

    pass_radius = radius * pixel_size;
    pass_random = random;

    vhs_position = tex_pass * screen_size;

    // calculate color offsets
      red_off = rand(vec2(12, 14), pass_radius);
    green_off = rand(vec2(16, 15), pass_radius);
     blue_off = rand(vec2(19, 21), pass_radius);
}
