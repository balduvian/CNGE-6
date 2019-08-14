package com.ebet.cnge.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;

public class Texture
{
	private static int default_horz_warp = GL_CLAMP_TO_EDGE;
	private static int default_vert_warp = GL_CLAMP_TO_EDGE;
	private static int default_min_filter = GL_LINEAR;
	private static int default_mag_filter = GL_LINEAR;
	
	public static void set_parameters(int hor_war, int ver_war, int min_fil, int mag_fil)
	{
		default_horz_warp = hor_war;
		default_vert_warp = ver_war;
		default_min_filter = min_fil;
		default_mag_filter = mag_fil;
	}
	
	public int width;
	public int height;
	
	public int horz_warp;
	public int vert_warp;
	public int mag_filter;
	public int min_filter;
	
	private int texture;
	
	public Texture(String path)
	{
		Util.error(() ->
		{
			BufferedImage b = ImageIO.read(new File(path));
			
			var width = b.getWidth();
			var height = b.getHeight();
			
			var len = width * height;
			
			int[] rgb_arr = new int[len];
			
			b.getRGB(0, 0, width, height, rgb_arr, 0, width);
			
			// we have to re order the rgb components to match RGBA
			for(var i = 0; i < len; ++i)
			{
				var rgb = rgb_arr[i];
				var red = (rgb >> 16) & 0xff;
				var gre = (rgb >>  8) & 0xff;
				var blu = (rgb      ) & 0xff;
				var alp = (rgb >> 24) & 0xff;
				rgb_arr[i] = (red << 0) | (gre << 8) | (blu << 16) | (alp << 24);
			}
			
			init(rgb_arr, width, height, default_horz_warp, default_vert_warp, default_min_filter, default_mag_filter);
		});
	}
	
	/**
	 * a texture that can't really be used yet,
	 * for framebuffer init
	 */
	public Texture()
	{
		this.horz_warp = default_horz_warp;
		this.vert_warp = default_vert_warp;
		this.min_filter = default_min_filter;
		this.mag_filter = default_mag_filter;
		
		texture = glGenTextures();
	}
	
	
	public Texture(int width, int height)
	{
		var rgb_arr = new int[width * height];
		
		init(rgb_arr, width, height, default_horz_warp, default_vert_warp, default_min_filter, default_mag_filter);
	}
	
	/**
	 * creates the texture from an rgb array
	 */
	private void init(int[] rgb, int width, int height, int hor_war, int ver_war, int min_fil, int mag_fil)
	{
		this.width = width;
		this.height = height;
		this.horz_warp = hor_war;
		this.vert_warp = ver_war;
		this.min_filter = min_fil;
		this.mag_filter = mag_fil;
		
		texture = glGenTextures();
		
		bind();
		
		parameter_hint(hor_war, ver_war, min_fil, mag_fil);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, rgb);
	}
	
	/**
	 * re tex image 2d's a texture
	 *
	 * used in resizing because parameters don't change
	 */
	private void init_resize(int[] rgb, int width, int height)
	{
		this.width = width;
		this.height = height;
		
		bind();
		
		parameter_hint(horz_warp, vert_warp, min_filter, mag_filter);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, rgb);
	}
	
	private void parameter_hint(int hor_war, int ver_war, int min_fil, int mag_fil)
	{
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, hor_war);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, ver_war);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min_fil);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag_fil);
	}
	
	public void resize(int width, int height)
	{
		init_resize(new int[width * height], width, height);
	}
	
	public void resize_depth(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		bind();
		
		parameter_hint(horz_warp, vert_warp, min_filter, mag_filter);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public int get()
	{
		return texture;
	}
}
