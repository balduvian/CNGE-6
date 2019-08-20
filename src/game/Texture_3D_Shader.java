package game;

import com.ebet.cnge.engine.Shader;

public class Texture_3D_Shader extends Shader.M_VP_Shader
{
	public Texture_3D_Shader()
	{
		super(
			"res/shaders/texture_3d/vert.glsl", "res/shaders/texture_3d/frag.glsl",
			"in_color",
			"lighting_angle",
			"intensity",
			"ambient"
		);
	}
	
	public void give(float r, float g, float b, float a, float x, float y, float z, float i, float m)
	{
		give_vec4(r, g, b, a);
		give_vec3(x, y, z);
		give_float(i);
		give_float(m);
	}
	
}
