package com.ebet.cnge.core;

public class Font
{
	/*                                          */
	/*                                          */
	/*                                          */
	/*                                          */
	/*                                          */
	
	private float x_offset;
	private float baseline;
	private float tile_width;
	private float tile_height;
	
	private Render_Character renderer;
	
	public interface Render_Character
	{
		void render_character(char character, float x, float y, float width, float height);
	}
	
	public Font(float x_offset, float baseline, float tile_width, float tile_height, Render_Character renderer)
	{
		this.x_offset = x_offset;
		this.baseline = baseline;
		this.tile_width = tile_width;
		this.tile_height = tile_height;
		
		this.renderer = renderer;
	}
	
	public void render(float x, float y, float width, String sequence)
	{
		var len = sequence.length();
		
		var using_height = (tile_height / tile_width) * width;
		var using_x_offset = (x_offset / tile_width) * width;
		var using_baseline = (baseline / tile_height) * using_height;
		
		for(var i = 0; i < len; ++i)
		{
			renderer.render_character(sequence.charAt(i), x - using_x_offset, y - using_baseline, width, using_height);
			x += width;
		}
	}
}
