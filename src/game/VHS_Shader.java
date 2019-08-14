package game;

import com.ebet.cnge.engine.Shader;

public class VHS_Shader extends Shader.M_VP_Shader
{
	public VHS_Shader()
	{
		super(
			"res/shaders/vhs/vert.glsl", "res/shaders/vhs/frag.glsl",
			"screen_size",
			"scan_lines",
			"radius",
			"random"
		);
	}
	
	public void give(float screen_width, float screen_height, float scan_lines, float radius, float random)
	{
		give_vec2(screen_width, screen_height);
		give_float(scan_lines);
		give_float(radius);
		give_float(random);
	}
	
}
