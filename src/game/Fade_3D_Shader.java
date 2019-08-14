package game;

import com.ebet.cnge.engine.Shader;

public class Fade_3D_Shader extends Shader.M_VP_Shader
{
	public Fade_3D_Shader()
	{
		super(
			"res/shaders/fade_3d/vert.glsl", "res/shaders/fade_3d/frag.glsl",
			"in_color",
			"range"
		);
	}
	
	public void give(float r, float g, float b, float a, float visible, float invisible)
	{
		give_vec4(r, g, b, a);
		give_vec2(visible, invisible);
	}
	
}
