package game;

import com.ebet.cnge.engine.Shader;

public class Color_Shader extends Shader.M_VP_Shader
{
	public Color_Shader()
	{
		super(
			"res/shaders/texture_tint/vert.glsl", "res/shaders/texture_tint/frag.glsl",
			"in_color"
		);
	}
	
	public void give(float r, float g, float b, float a)
	{
		give_vec4(r, g, b, a);
	}
	
}
