package com.ebet.cnge.engine;

import org.joml.Matrix4f;

import static com.ebet.cnge.engine.Util.m16;

public class Camera
{
	private static float[] projection_values = m16();
	private static float[] projection_view_values = m16();
	
	public float width;
	public float height;
	
	public Transform transform;
	
	public Matrix4f projection;
	public Matrix4f projection_view;
	
	public Camera()
	{
		transform = new Transform();
		projection = new Matrix4f();
		projection_view = new Matrix4f();
	}
	
	public void set_orthographic_posup(float width, float height)
	{
		this.width = width;
		this.height = height;
		projection.setOrtho2DLH(0, width, 0, height);
		
		// set value array for base projection
		projection.get(projection_values);
	}
	
	public void set_orthographic(float width, float height)
	{
		this.width = width;
		this.height = height;
		projection.setOrtho2DLH(0, width, height, 0);
		
		// set value array for base projection
		projection.get(projection_values);
	}
	
	public void update()
	{
		projection_view.identity();
		projection_view.mul(projection);
		transform.matrify_view(projection_view);
		
		// set value array for proj view
		projection_view.get(projection_view_values);
	}
	
	public float[] get_projection()
	{
		return projection_values;
	}
	
	public float[] get_projection_view()
	{
		return projection_view_values;
	}
}
