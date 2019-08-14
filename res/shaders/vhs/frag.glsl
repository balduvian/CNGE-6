#version 330 core

uniform sampler2D tex;

in vec2 tex_pass;
in float pass_scan_lines;
in float pass_noise_lines;
in float scan_height;
in float noise_height;

in vec2 red_off;
in vec2 green_off;
in vec2 blue_off;

in vec2 pass_radius;
in float pass_random;

in vec2 vhs_position;

out vec4 color;

float rand(float seed)
{
    return fract(sin(seed - pass_random) * 1034234.562 * cos(seed * pass_random) + pass_random * seed);
}

float rand(vec2 seed)
{
    return fract(sin(seed.x - pass_random) * 1034234.562 * cos(seed.y - seed.x * pass_random) + pass_random * seed.y);
}

// returns 0 or 1 depending on if a line
// should be a noise line or not
float determine_noise_line(float line, float out_of)
{
    return step(1 - (line / (10 * out_of)), rand(line));
}

// returns a curve from 0 to 1 with a peak at 0.5 at 1, and ends at 0
float parabolic(float along)
{
    return 1 - 4 * (along - 0.5) * (along - 0.5);
}

// returns how musch noise will be applied
// this far along into the line
float noise_wave(float line, float along)
{
    //shift along left or right
    along += rand(line - 0.3232) * 2 - 1;

    float t = along + (sin(6.2 * along) / (5 + rand(line + 0.2))) + rand(line);

    return smoothstep(0, 0.9, sin(6.2 * t * (11 + rand(line + 32.3272))));
}

void main()
{
    // which line we are on
    // use this to get the same random for each pixel
    // of a given line
    float line_on = floor(tex_pass.y / scan_height);

    // how far into the line we are
    float line_in = mod(tex_pass.y, scan_height) / scan_height;

    // same thing for noise lines
    float noise_line_on = floor(tex_pass.y / noise_height);
    float noise_line_in = mod(tex_pass.y, noise_height) / noise_height;

    // returns 0 if we're on an odd line, 1 if we're on an even line
    float odd_even = step(scan_height / 2, mod(tex_pass.y, scan_height));

    // how far we shift the line (only for even lines)
    // goes from -radius to radius potentially
    float shift = parabolic(line_in) * ((rand(line_on) * (pass_radius.x * 2)) - pass_radius.x);

    vec2 shifter = vec2(shift, 0);

    // separate channels
    float   red_color = texture(tex, tex_pass + red_off + shifter).x;
    float green_color = texture(tex, tex_pass + green_off + shifter).y;
    float  blue_color = texture(tex, tex_pass + blue_off + shifter).z;

    // our output color is going to be full alpha
    color = vec4(red_color, green_color, blue_color, 1);

    // desaturate the color by cutting the range we can express in 2
    // then raising back so our midpoint is the same
    color *= 0.75;
    color += 0.1875;

    // if our line is a noise line or not
    // weighted for how far into the line we are in
    float noise_line = determine_noise_line(pass_noise_lines - noise_line_on, pass_noise_lines) * parabolic(noise_line_in) * noise_wave(noise_line_on, tex_pass.x);

    // if our line happens to be a noise line, this is the noise color would be
    //per this pixel
    vec3 noise = vec3(rand(tex_pass), rand(tex_pass + 3.2329), rand(tex_pass + 0.3523));

    //if we our a noise line, discard all color at this point
    color *= (1 - noise_line);
    //then add noise and it will only get added if it is a noise line
    color += vec4(noise, 1) * noise_line;
}
