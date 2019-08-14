package com.ebet.cnge.engine;

import org.lwjgl.BufferUtils;

import java.nio.Buffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class VAO
{
	// vertex array object
	private int vao;
	
	// index buffer object
	private int ibo;
	
	private int vertex_count;
	private int attrib_count;
	
	private Attribute[] attributes;
	
	private int draw_mode;
	
	/**
	 * a container for storing a vertex attribute
	 */
	public static class Attribute
	{
		public int per_vertex;
		public float[] data;
		
		// the buffer will get filled in when the VAO is made
		public int buffer;
		
		public Attribute(int per_vertex, float[] data)
		{
			this.per_vertex = per_vertex;
			this.data = data;
			
			this.buffer = 0;
		}
	}
	
	public VAO(int draw_mode, float[] vertices, int[] indices, Attribute[] attributes)
	{
		this.draw_mode = draw_mode;
		this.attributes = attributes;
		this.attrib_count = attributes.length;
		this.vertex_count = indices.length;
		
		// create the vao
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		// our first attribute is the vertices
		add_attribute(vertices, 3, 0);
		
		// add each other attribute
		for (var i = 0; i < attrib_count; ++i)
		{
			var attrib = attributes[i];
			attrib.buffer = add_attribute(attrib.data, attrib.per_vertex, i + 1);
		}
		
		// now create the index buffer
		ibo = add_index(indices);
	}
	
	/**
	 * adds a vertex attribute to this VAO,
	 * returns it so you can store it
	 */
	private int add_attribute(float[] data, int per_vertex, int location)
	{
		// generate our buffer
		var buffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		
		// tell opengl we are storing however many values of float per vertex
		glVertexAttribPointer(location, per_vertex, GL_FLOAT, false, 0, 0);
		
		// activate this attribute
		glEnableVertexAttribArray(location);
		
		return buffer;
	}
	
	/**
	 * adds the index buffer
	 */
	private int add_index(int[] indices)
	{
		// much the same basic process of add attribute
		var buffer = glGenBuffers();
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		return buffer;
	}
	
	/**
	 * draws the vertices with attributes in this VAO!
	 */
	public void render()
	{
		glBindVertexArray(vao);
		glDrawElements(draw_mode, vertex_count, GL_UNSIGNED_INT, 0);
	}
}
