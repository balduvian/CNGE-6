package com.ebet.cnge.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.ebet.cnge.engine.Util.m4;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class Texture_Sheet extends Texture
{
	/*                                            */
	/*            0 - scale tex coord x           */
	/*            1 - scale tex coord y           */
	/*            2 - shift tex coord x           */
	/*            3 - shift tex coord y           */
	/*                                            */
	
	private static float[] tile_values = m4();
	
	private float width_fraction;
	private float height_fraction;
	
	public Texture_Sheet(String path, int tiles_wide, int tiles_tall)
	{
		super(path);
		
		width_fraction = 1f / tiles_wide;
		height_fraction = 1f / tiles_tall;
	}
	
	public Texture_Sheet(String path, int tiles_wide)
	{
		super(path);
		
		width_fraction = 1f / tiles_wide;
		height_fraction = 1;
		
		System.out.println(width_fraction);
	}
	
	/**
	 * pass into a shader to modify tex coordinates to get
	 * a specific tile in the sheet
	 */
	public float[] get_tile(int x, int y)
	{
		tile_values[0] = width_fraction;
		tile_values[1] = height_fraction;
		tile_values[2] = x * width_fraction;
		tile_values[3] = y * height_fraction;
		
		return tile_values;
	}
	
	/**
	 * pass into a shader to modify tex coordinates to get
	 * a specific tile in the sheet,
	 *
	 * for a 1d texture sheet along with tiles in the x direction
	 */
	public float[] get_tile(int x)
	{
		tile_values[0] = width_fraction;
		tile_values[1] = 1;
		tile_values[2] = x * width_fraction;
		tile_values[3] = 0;
		
		return tile_values;
	}
}
