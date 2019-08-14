package game;

import com.ebet.cnge.engine.Camera;
import com.ebet.cnge.engine.Transform;

public class Player
{
	private static Camera camera;
	private static Color_Shader color_shader;
	private static Transform transform;
	
	public Player(Camera camera, Color_Shader color_shader)
	{
		this.camera = camera;
		this.color_shader = color_shader;
		
		transform = new Transform();
		transform.translation.set(0, 1);
	}
	
	public void update(double time)
	{
	
	}
}
