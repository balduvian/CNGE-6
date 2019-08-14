package game;

import com.ebet.cnge.core.Aspect;
import com.ebet.cnge.core.Up_Rect;
import com.ebet.cnge.engine.*;
import org.lwjgl.opengl.GL30;

import static com.ebet.cnge.engine.Transform.*;
import static com.ebet.cnge.engine.Util.clear;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

public class Game
{
	public static void main(String[] args)
	{
		Window.init();
		
		var window = new Window(3, 3, true, true, "CNGE Game Test");
		
		var monitors = Window.monitor_list();
		
		window.set_context(true);
		
		createCapabilities();

		window.show();
		
		var aspect = new Aspect(16, 9);
		var aspect_buffer = new Framebuffer(true, true);
		
		// set up camera
		var camera = new Camera();
		camera.set_orthographic(16, 9);
		
		var camera_3d = new Camera_3D();
		camera_3d.set_perspective((float)(Math.PI * 0.5), 16f / 9f);
		
		window.set_resize((width, height) ->
		{
			aspect.update(width, height);
			aspect_buffer.size(aspect.screen_width, aspect.screen_height);
		});
		
		window.init_key_register(4);
		window.register_key(GLFW_KEY_W);
		window.register_key(GLFW_KEY_A);
		window.register_key(GLFW_KEY_S);
		window.register_key(GLFW_KEY_D);
		
		window.set_monitor(monitors[monitors.length - 1], false);
		
		var scene = new Scene(aspect_buffer, window, camera, camera_3d);
		
		var loop = new Loop(window.get_refresh());
		
		var up_rect = new Up_Rect();
		var vhs_shader = new VHS_Shader();
		var color_shader = new Color_Shader();
		
		// enable alotta stuff
		
		glEnable(GL30.GL_CLIP_DISTANCE0);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		loop.loop(
			window::get_close,
			(frames, nanos, time) ->
			{
				window.update();
				
				scene.update(time);
				
				// sset up the aspect
				clear(0, 0, 0, 0);
				aspect_buffer.enable();
				
				// render inside aspect buffer
				scene.render(frames);
				
				// now we're rendering in the aspect space
				Framebuffer.use_default();
				aspect.set_viewport();
				
				// render the texture of the aspect buffer
				aspect_buffer.get_texture().bind();
				//vhs_shader.enable(default_model, default_projection);
				//vhs_shader.give(aspect_buffer.get_texture().width, aspect_buffer.get_texture().height, 50, 5, (float)Math.random());
				color_shader.enable(default_model, default_projection);
				color_shader.give(1, 1, 1, 1);
				
				
				up_rect.render();
				
				window.swap();
			}
		);
	}
	
}
