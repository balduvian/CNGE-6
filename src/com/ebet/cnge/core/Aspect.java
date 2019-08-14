package com.ebet.cnge.core;

import static org.lwjgl.opengl.GL11.glViewport;

public class Aspect
{
	public int screen_left;
	public int screen_top;
	public int screen_width;
	public int screen_height;
	
	private float width;
	private float height;
	
	public Aspect(float width, float height)
	{
		this.width = width;
		this.height = height;
	}
	
	/**
	 * changes internal screen dim values
	 * based on the size of a window
	 */
	public void update(int window_width, int window_height)
	{
		var window_aspect = ((float)window_width / window_height);
		var aspect = width / height;
		
		// if we are too wide
		if(window_aspect > aspect)
		{
			screen_height = window_height;
			screen_width = (int)(aspect * screen_height);
			
			screen_top = 0;
			screen_left = (window_width - screen_width) / 2;
		}
		
		// too tall / perfectly aligned
		else
		{
			screen_width = window_width;
			screen_height = (int)(1 / (aspect / screen_width));
			
			screen_left = 0;
			screen_top = (window_height - screen_height) / 2;
		}
	}
	
	public void set_viewport()
	{
		glViewport(screen_left, screen_top, screen_width, screen_height);
	}
}
