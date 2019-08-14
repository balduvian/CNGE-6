package game;

import com.ebet.cnge.engine.Transform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.ebet.cnge.engine.Util.*;

public class Block_Map
{
	public static final int OUT_OF_BOUNDS = -1;
	
	private static boolean[] block_surround_values = new boolean[8];
	private static int[] block_coords = new int[2];
	private static float[] world_coords = new float[2];
	
	private int[][] map;
	public int width;
	public int height;
	
	private Renderer renderer;
	
	public Transform transform;
	
	public interface Renderer
	{
		void render(int block, int x, int y);
	}
	
	public interface Accessor
	{
		int access(int x, int y);
	}
	
	public interface Surround
	{
		boolean check(int block, int x, int y);
	}
	
	public Block_Map(int[][] map, Renderer renderer)
	{
		this.map = map;
		this.renderer = renderer;
		
		width = map.length;
		height = map[0].length;
		
		transform = new Transform();
	}
	
	public void render(float _left, float _right, float _up, float _down, Accessor accessor)
	{
		int left = wtb_x(_left);
		int right = wtb_x(_right);
		int up = wtb_x(_up);
		int down = wtb_x(_down);
		
		for(var i = left; i <= right; ++i)
		{
			for(var j = up; j <= down; ++j)
			{
				renderer.render(accessor.access(i, j), i, j);
			}
		}
	}
	
	public int wtb_x(float x)
	{
		return (int)Math.floor((x - transform.translation.x) / transform.scale.x);
	}
	
	public int wtb_y(float y)
	{
		return (int)Math.floor((y - transform.translation.y) / transform.scale.y);
	}
	
	public float btw_x(int x)
	{
		return x * transform.scale.x + transform.translation.x;
	}
	
	public float btw_y(int y)
	{
		return y * transform.scale.y + transform.translation.y;
	}
	
	/**
	 * gets the blocks surrounding this block
	 * for use in a Block_Parts,
	 *
	 * you may use one of the accessor methods in this class
	 */
	public boolean[] get_surround(Surround surround, Accessor accessor, int x, int y)
	{
		block_surround_values[0] = surround.check(accessor.access(x - 1, y    ), x - 1, y    );
		block_surround_values[1] = surround.check(accessor.access(x - 1, y - 1), x - 1, y - 1);
		block_surround_values[2] = surround.check(accessor.access(x    , y - 1), x    , y - 1);
		block_surround_values[3] = surround.check(accessor.access(x + 1, y - 1), x + 1, y - 1);
		block_surround_values[4] = surround.check(accessor.access(x + 1, y    ), x + 1, y    );
		block_surround_values[5] = surround.check(accessor.access(x + 1, y + 1), x + 1, y + 1);
		block_surround_values[6] = surround.check(accessor.access(x    , y + 1), x    , y + 1);
		block_surround_values[7] = surround.check(accessor.access(x - 1, y + 1), x - 1, y + 1);
		
		return block_surround_values;
	}
	
	public interface Block_Match
	{
		int match(int rgb);
	}
	
	/**
	 * makes an int array to pass into a block map
	 * from reading a file
	 */
	public static int[][] from_image(String path, Block_Match block_match)
	{
		int[][][] map = new int[1][][];
		
		error(() ->
		{
			var img = ImageIO.read(new File(path));
			var width = img.getWidth();
			var height = img.getHeight();
			var len = width * height;
			
			var ret = new int[width][height];
			
			var rgb_arr = new int[len];
			for(var i = 0; i < height; ++i)
			{
				img.getRGB(0, height - i - 1, width, 1, rgb_arr, i * width, width);
			}
			
			for(var i = 0; i < len; ++i)
			{
				ret[i % width][i / width] = block_match.match(rgb_arr[i]);
			}
			
			map[0] = ret;
		});
		
		return map[0];
	}
	
	/*                                  */
	/*            accessors             */
	/*                                  */
	
	/**
	 * accesses the map and gets the block at x, y;
	 *
	 * if that's outside the map you get returned:
	 * OUT_OF_BOUNDS
	 */
	public int access(int x, int y)
	{
		return (x < 0 || y < 0 || x >= width || y >= height) ?
	       OUT_OF_BOUNDS : map[x][y];
	}
	
	/**
	 * the maps repeats if you go out of bounds
	 */
	public int access_wrap(int x, int y)
	{
		return map[mod(x, width)][mod(y, height)];
	}
	
	/**
	 * the edge block repeats if you go out of bounds
	 */
	public int access_repeat(int x, int y)
	{
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (x >= width)
			x = width - 1;
		if (y >= height)
			y = height - 1;
		
		return map[x][y];
	}
}
