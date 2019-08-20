package com.ebet.cnge.engine;

import org.joml.Matrix4f;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.ebet.cnge.engine.Util.error;
import static com.ebet.cnge.engine.Util.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

abstract public class Shader
{
	/*                                                             */
	/*                      types of shader                        */
	/*                                                             */
	
	/**
	 * A Shader that takes an mvp
	 */
	public static class MVP_Shader extends Shader
	{
		public static String mvp_name = "mvp";
		
		protected int mvp_loc;
		
		public static void set_default_names(String mvp_name)
		{
			MVP_Shader.mvp_name = mvp_name;
		}
		
		protected MVP_Shader(String vertex_path, String fragment_path, String... uniforms)
		{
			super(vertex_path, fragment_path, uniforms);
			
			error("you don't have a mvp uniform in this shader", () ->
			{
				mvp_loc = glGetUniformLocation(program, mvp_name);
			});
		}
		
		public void enable(float[] mvp)
		{
			super.enable();
			glUniformMatrix4fv(mvp_loc, false, mvp);
		}
	}
	
	/**
	 * A shader that takes a model and a view projection matrix separately
	 */
	public static class M_VP_Shader extends Shader
	{
		public static String model_name = "model";
		public static String projection_name = "projection";
		
		protected int model_loc;
		protected int projection_loc;
		
		public static void set_default_names(String model_name, String projection_name)
		{
			M_VP_Shader.model_name = model_name;
			M_VP_Shader.projection_name = projection_name;
		}
		
		protected M_VP_Shader(String vertex_path, String fragment_path, String... uniforms)
		{
			super(vertex_path, fragment_path, uniforms);
			
			error("you don't have a model and projection uniform in this shader", () ->
			{
				model_loc = glGetUniformLocation(program, model_name);
				projection_loc = glGetUniformLocation(program, projection_name);
			});
		}
		
		public void enable(float[] model, float[] projection)
		{
			super.enable();
			glUniformMatrix4fv(model_loc, false, model);
			glUniformMatrix4fv(projection_loc, false, projection);
		}
	}
	
	protected int program;
	
	private int location_pointer;
	protected int[] locations;
	
	/*                                                             */
	/*                          creation                           */
	/*                                                             */
	
	/**
	 * doesn't put in an mvp or m and vp
	 */
	private Shader(String vertex_path, String fragment_path, String... uniforms)
	{
		program = glCreateProgram();
		
		// load both shaders
		var vert = load_shader(vertex_path, GL_VERTEX_SHADER);
		var frag = load_shader(fragment_path, GL_FRAGMENT_SHADER);
		
		// then do this sequence to attach them
		glAttachShader(program, vert);
		glAttachShader(program, frag);
		
		glLinkProgram(program);
		
		glDetachShader(program, vert);
		glDetachShader(program, frag);
		
		glDeleteShader(vert);
		glDeleteShader(frag);
		
		var len = uniforms.length;
		
		locations = new int[len];
		location_pointer = 0;
		
		for(int i = 0; i < len; ++i)
		{
			locations[i] = glGetUniformLocation(program, uniforms[i]);
		}
	}
	
	/**
	 * actually parses a file for glsl shader code
	 */
	private int load_shader(String path, int type)
	{
		var src_str = new Capture<String>();
		
		// build the file
		error(() ->
			src_str.set(new String(Files.readAllBytes(Paths.get(path))))
		);
		
		// create the shader
		int shader = glCreateShader(type);
		
		// and compile
		glShaderSource(shader, src_str.release());
		glCompileShader(shader);
		
		// check if it actually compiled
		error(() ->
		{
			if(glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE)
				throw new Exception(
					"failed to compile shader | "
					+ path + " | " + type + " | "
					+ glGetShaderInfoLog(shader)
				);
		});
		
		return shader;
	}
	
	/*                                                             */
	/*                         Usablility                          */
	/*                                                             */
	
	/**
	 * begin to use this shader for any renders
	 */
	private void enable()
	{
		glUseProgram(program);
		location_pointer = 0;
	}
	
	// givers
	
	protected void give_float(float x)
	{
		glUniform1f(locations[location_pointer], x);
		++location_pointer;
	}
	
	protected void give_vec2(float x, float y)
	{
		glUniform2f(locations[location_pointer], x, y);
		++location_pointer;
	}
	
	protected void give_vec3(float x, float y, float z)
	{
		glUniform3f(locations[location_pointer], x, y, z);
		++location_pointer;
	}
	
	protected void give_vec4(float x, float y, float z, float w)
	{
		glUniform4f(locations[location_pointer], x, y, z, w);
		++location_pointer;
	}
}
