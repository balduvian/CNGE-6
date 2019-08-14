package game;

import com.ebet.cnge.core.Font;
import com.ebet.cnge.core.Rect;
import com.ebet.cnge.engine.Camera;
import com.ebet.cnge.engine.Texture;
import com.ebet.cnge.engine.Texture_Sheet;
import com.ebet.cnge.engine.Transform;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class Game_Font extends Font
{
	private static Camera camera;
	private static SDF_Shader sdf_shader;
	private static Rect rect;
	
	private static Texture_Sheet texture;
	
	private static boolean world_view;
	
	private static float r, g, b, a;
	
	public static void set_world_view(boolean world_view)
	{
		Game_Font.world_view = world_view;
	}
	
	public static void set_color(float r, float g, float b, float a)
	{
		Game_Font.r = r;
		Game_Font.g = g;
		Game_Font.b = b;
		Game_Font.a = a;
	}
	
	public Game_Font(Camera camera, SDF_Shader sdf_shader, Rect rect)
	{
		super(4, 32, 32, 48, (character, x, y, width, height) ->
		{
			texture.bind();
			
			sdf_shader.enable(Transform.matrify(x, y, width, height), world_view ? camera.get_projection_view() : camera.get_projection());
			sdf_shader.give(texture.get_tile(character - 32), r, g, b, a);
			
			rect.render();
		});
		
		this.camera = camera;
		this.sdf_shader = sdf_shader;
		this.rect = rect;
		
		Texture.set_parameters(GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE, GL_LINEAR, GL_LINEAR);
		texture = new Texture_Sheet("res/images/characters.png", 127 - 32);
	}
}
