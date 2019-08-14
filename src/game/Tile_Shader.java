package game;

import com.ebet.cnge.engine.Shader;

public class Tile_Shader extends Shader.M_VP_Shader
{
	public Tile_Shader()
	{
		super(
			"res/shaders/tile/vert.glsl", "res/shaders/tile/frag.glsl",
			"tex_modif",
			"in_color"
		);
	}
	
	public void give(float[] tex_modif, float r, float g, float b, float a)
	{
		give_vec4(tex_modif[0], tex_modif[1], tex_modif[2], tex_modif[3]);
		give_vec4(r, g, b, a);
	}
	
}
