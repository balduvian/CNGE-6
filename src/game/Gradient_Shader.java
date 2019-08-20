package game;

import com.ebet.cnge.engine.Shader;

public class Gradient_Shader extends Shader.M_VP_Shader
{
	public Gradient_Shader()
	{
		super(
			"res/shaders/gradient/vert.glsl", "res/shaders/gradient/frag.glsl",
			"color_1",
			"color_2"
		);
	}
	
	public void give(float r, float g, float b, float a, float r1, float g1, float b1, float a1)
	{
		give_vec4(r, g, b, a);
		give_vec4(r1, g1, b1, a1);
	}
	
}
