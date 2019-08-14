package com.ebet.cnge.engine;

import org.joml.Matrix4f;

import static com.ebet.cnge.engine.Util.m16;

public class Camera_3D
{
	private static float[] projection_values = m16();
	private static float[] projection_view_values = m16();
	
	public float fov_y;
	public float aspect;
	
	public Transform_3D transform;
	
	public Matrix4f projection;
	public Matrix4f projection_view;
	
	public Camera_3D()
	{
		transform = new Transform_3D();
		projection = new Matrix4f();
		projection_view = new Matrix4f();
	}
	
	public void set_perspective(float fov_y, float aspect)
	{
		this.fov_y = fov_y;
		this.aspect = aspect;
		
		projection.setPerspectiveLH(fov_y, aspect, 0.5f, 250f);
		
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
