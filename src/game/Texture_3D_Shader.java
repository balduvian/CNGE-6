package game;

import com.ebet.cnge.engine.Shader;

public class Texture_3D_Shader extends Shader.M_VP_Shader
{
	public Texture_3D_Shader()
	{
		super(
			"res/shaders/texture_3d/vert.glsl", "res/shaders/texture_3d/frag.glsl",
			"in_color"
		);
	}
	
	public void give(float r, float g, float b, float a)
	{
		give_vec4(r, g, b, a);
	}
	
}
