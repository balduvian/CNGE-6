package com.ebet.cnge.engine;

import org.lwjgl.opengl.GL32;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer
{
	private int framebuffer;
	
	private int width;
	private int height;
	
	private boolean has_color_texture;
	private boolean has_depth_buffer;
	
	private Texture color_texture;
	private Texture depth_texture;
	private int depth_buffer;
	
	public Framebuffer(boolean has_color_texture, boolean has_depth_buffer)
	{
		this.has_color_texture = has_color_texture;
		this.has_depth_buffer = has_depth_buffer;
		
		framebuffer = glGenFramebuffers();
		
		// now initialize our attachments
		if(has_color_texture)
			this.color_texture = new Texture();
		
		if(has_depth_buffer)
			this.depth_texture = new Texture();
			//this.depth_buffer = glGenBuffers();
	}
	
	/*                        */
	/*        resizers        */
	/*                        */
	
	public void size(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		bind();
		
		if(has_color_texture)
			size_color_texture();
		
		if(has_depth_buffer)
			size_depth_buffer();
		
		use_default();
	}
	
	private void size_color_texture()
	{
		color_texture.resize(width, height);
		
		GL32.glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, color_texture.get(), 0);
	}
	
	private void size_depth_buffer()
	{
		depth_texture.resize_depth(width, height);
		
		GL32.glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depth_texture.get(), 0);
		//glBindRenderbuffer(GL_RENDERBUFFER, depth_buffer);
		//glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		
		//glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depth_buffer);
	}
	
	/*                        */
	/*        enablers        */
	/*                        */
	
	/**
	 * uses this framebuffer,
	 * and doesn't change the size of the camera
	 */
	public void enable()
	{
		bind();
		glViewport(0, 0, width, height);
	}
	
	/**
	 * uses this framebuffer,
	 * and sets the camera's dims to be the same size as this framebuffer
	 */
	public void enable(Camera camera)
	{
		bind();
		camera.set_orthographic(width, height);
		glViewport(0, 0, width, height);
	}
	
	/**
	 * uses this framebuffer,
	 * and you can set a custom size of the camera
	 */
	public void enable(Camera camera, float camera_width, float camera_height)
	{
		bind();
		camera.set_orthographic(camera_width, camera_height);
		glViewport(0, 0, width, height);
	}
	
	/**
	 * lets you bind the texture independently
	 */
	public Texture get_texture()
	{
		return color_texture;
	}
	
	public Texture get_depth_texture()
	{
		return depth_texture;
	}
	
	/*                        */
	/*        enablers        */
	/*                        */
	
	/**
	 * used internally
	 */
	private void bind()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
	}
	
	/**
	 * go back to using the glfw default framebuffer on the window
	 */
	public static void use_default()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	/**
	 * NOTE: binds the read and write opengl framebuffers
	 *
	 * will copy all framebuffer data from this framebuffer into another
	 *
	 * different size? no problem, bilinear scaling
	 */
	public void resolve(Framebuffer write_to)
	{
		glBindFramebuffer(GL_READ_FRAMEBUFFER, framebuffer);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, write_to.framebuffer);
		glBlitFramebuffer(0, 0, width, height, 0, 0, write_to.width, write_to.height, GL_COLOR_BUFFER_BIT, GL_LINEAR);
	}
	
}
