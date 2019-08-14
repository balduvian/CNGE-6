package game;

import com.ebet.cnge.core.Block_Parts;
import com.ebet.cnge.core.Cube;
import com.ebet.cnge.core.Rect;
import com.ebet.cnge.core.Up_Rect;
import com.ebet.cnge.engine.*;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Text;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.Map;

import static com.ebet.cnge.engine.Transform.matrify;
import static com.ebet.cnge.engine.Util.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.glCopyTexSubImage3D;

public class Scene
{
	private static Camera camera;
	private static Camera_3D camera_3d;
	private static Window window;
	private static Framebuffer game_buffer;
	
	private static Transform_3D transform_3d;
	private static Transform transform;
	
	private static Color_Shader color_shader;
	private static SDF_Shader sdf_shader;
	private static Rect rect;
	private static Up_Rect up_rect;
	private static Texture texture;
	private static Game_Font game_font;
	
	private static Texture esh_texture;
	
	private static Block_Map map;
	
	private static Texture_Sheet purple_parts;
	
	private static Tile_Shader tile_shader;
	private static Texture_3D_Shader texture_3D_shader;
	private static Fade_3D_Shader fade_3D_shader;
	private static Cube cube;
	
	private static Framebuffer block_buffer;
	
	private static float[][][] buildings;
	
	public Scene(Framebuffer game_buffer, Window window, Camera camera, Camera_3D camera_3d)
	{
		this.camera = camera;
		this.camera_3d = camera_3d;
		this.window = window;
		this.game_buffer = game_buffer;
		
		transform_3d = new Transform_3D();
		transform_3d.translation.set(0, 0, 2);
		transform_3d.scale.set(1, 1, 1);
		
		transform = new Transform();
		transform.translation.set(0, 0);
		transform.scale.set(32, 18);
		//transform.rotation = 0.5f * (float)Math.PI;
		
		rect = new Rect();
		cube = new Cube();
		up_rect = new Up_Rect();
		
		color_shader = new Color_Shader();
		sdf_shader = new SDF_Shader();
		tile_shader = new Tile_Shader();
		
		Texture.set_parameters(GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE, GL_NEAREST, GL_NEAREST);
		block_buffer = new Framebuffer(true, false);
		block_buffer.size(16, 16);
		
		Texture.set_parameters(GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE, GL_LINEAR, GL_LINEAR);
		texture = new Texture("res/images/a.png");
		esh_texture = new Texture("res/images/esh.png");
		
		Texture.set_parameters(GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE, GL_NEAREST, GL_NEAREST);
		purple_parts = new Texture_Sheet("res/images/purple_parts.png", 5, 4);
		
		game_font = new Game_Font(camera, sdf_shader, rect);
		texture_3D_shader = new Texture_3D_Shader();
		fade_3D_shader = new Fade_3D_Shader();
		
		buildings = create_buildings(50, 2, 100);
		
		var rand_map = Block_Map.from_image("res/images/level.png", rgb ->
		{
			switch(rgb)
			{
				case 0xff0c0022:
					return 1;
				case 0xff3abc00:
					return 2;
			}
			
			return 0;
		});
		
		map = new Block_Map
        (
			rand_map,
			(block, x, y) ->
			{
				if(block == 1)
				{
					var parts = Block_Parts.get(map.get_surround((s_block, s_x, s_y) ->
						{
							return s_block == 1;
						},
						map::access_repeat,
						x, y
					));
					
					// start building the block in the block buffer;
					block_buffer.enable(camera, 16, 16);
					
					purple_parts.bind();
					
					// top left
					tile_shader.enable(matrify(0, 0, 8, 8), camera.get_projection());
					tile_shader.give(purple_parts.get_tile(parts[0], 0), 1, 1, 1, 1);
					rect.render();
					
					// top right
					tile_shader.enable(matrify(8, 0, 8, 8), camera.get_projection());
					tile_shader.give(purple_parts.get_tile(parts[1], 1), 1, 1, 1, 1);
					rect.render();
					
					// bottom right
					tile_shader.enable(matrify(8, 8, 8, 8), camera.get_projection());
					tile_shader.give(purple_parts.get_tile(parts[2], 2), 1, 1, 1, 1);
					rect.render();
					
					// bottom left
					tile_shader.enable(matrify(0, 8, 8, 8), camera.get_projection());
					tile_shader.give(purple_parts.get_tile(parts[3], 3), 1, 1, 1, 1);
					rect.render();
					
					// go back to game buffer and
					// render block buffer to a cube
					game_buffer.enable(camera, 16, 9);
					
					block_buffer.get_texture().bind();
					
					transform_3d.translation.set(x * 2, y * 2, 8);
					transform_3d.scale.set(2, 2, 2);
					
					texture_3D_shader.enable(transform_3d.matrify(), camera_3d.get_projection_view());
					texture_3D_shader.give(1, 1, 1, 1);
					
					cube.render();
				}
				else if(block == 2)
				{
					esh_texture.bind();
					
					transform_3d.translation.set(x * 2, y * 2, 8);
					transform_3d.scale.set(2, 2, 2);
					
					color_shader.enable(transform_3d.matrify(), camera_3d.get_projection_view());
					color_shader.give(1, 1, 1, 1);
					
					cube.render();
				}
			}
        );
		
		map.transform.scale.set(2, 2);
	}
	
	float rot = 0;
	
	public void update(double time)
	{
		var speed = (float)(4 * time);
		
		if(window.get_key_down(0))
		{
			camera_3d.transform.translation.add(0, speed, 0);
		}
		if(window.get_key_down(1))
		{
			camera_3d.transform.translation.add(-speed, 0, 0);
		}
		if(window.get_key_down(2))
		{
			camera_3d.transform.translation.add(0, -speed, 0);
		}
		if(window.get_key_down(3))
		{
			camera_3d.transform.translation.add(speed, 0, 0);
		}
	
		//rot += (float)(time * Math.PI);
		//rot %= Math.PI * 2;
		//transform_3d.rotation.set(rot, 0, 0);
		
		camera.update();
		camera_3d.update();
	}
	
	public float[][][] create_buildings(int density, int rows, float base_height)
	{
		// height, row_offset, density_offset, base_width, base_depth
		var ret = new float[rows][density][5];
		
		for(int i = 0; i < rows; ++i)
		{
			for(int j = 0; j < density; ++j)
			{
				ret[i][j][0] = (float)(base_height * Math.random() * (1 + (float)i / rows));
				for(int k = 1; k < 5; ++k)
				{
					ret[i][j][k] = (float)(Math.random());
				}
			}
		}
		
		return ret;
	}
	
	public void render(int fps)
	{
		clear(0.1f, 0.2f, 0.75f, 1f);
		
		cull_face(false, true);
		
		depth_test();
		
		// render the background
		esh_texture.bind();
		
		var back_size = 250f;
		var start_line = 50f;
		
		transform_3d.translation.set(-back_size / 2, 0, 0);
		transform_3d.scale.set(back_size, back_size, 1);
		transform_3d.rotation.set((float)(Math.PI * 0.5), 0, 0);
		
		fade_3D_shader.enable(transform_3d.matrify(), camera_3d.get_projection_view());
		fade_3D_shader.give(0.5f, 0.5f, 0.5f, 1, start_line, back_size);
		
		transform_3d.rotation.set(0, 0, 0);
		
		up_rect.render();
		
		var rows = buildings.length;
		var dens = buildings[0].length;
		
		var w_space = back_size / dens;
	
		for(int i = 0; i < rows; ++i)
		{
			for(int j = 0; j < dens; ++j)
			{
				var z = start_line + (back_size - start_line) * ((float)i / rows);
				var x = ((float)j / dens) * back_size - (back_size / 2);
				
				transform_3d.translation.set(x, 0, z);
				transform_3d.scale.set(w_space * buildings[i][j][3], buildings[i][j][0], w_space * buildings[i][j][4]);
				
				fade_3D_shader.enable(transform_3d.matrify(), camera_3d.get_projection_view());
				fade_3D_shader.give(1, 0.25f, 0.1f, 1, start_line, back_size);
				
				cube.render();
			}
		}
		
		transform_3d.rotation.set(0, 0, 0);
		
		// render the map
		var width = 32 * 1.5f;
		var height = 18 * 1.5f;
		
		var x = camera_3d.transform.translation.x - width / 2f;
		var y = camera_3d.transform.translation.y - height / 2f;
		
		map.render(x, x + width, y, y + height, map::access_repeat);
		
		// render fps
		no_depth();
		
		Game_Font.set_world_view(false);
		Game_Font.set_color(1, 1, 1, 1);
		game_font.render(1, 1, 1, Integer.toString(fps));
	}
}
