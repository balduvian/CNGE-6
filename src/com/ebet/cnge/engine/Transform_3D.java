package com.ebet.cnge.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static com.ebet.cnge.engine.Util.m16;

public class Transform_3D
{
	/*                                              */
	/*           shared matrix equipment            */
	/*                                              */
	
	public static Matrix4f matrix = new Matrix4f();
	public static float[] matrix_values = m16();
	
	public Vector3f translation;
	public Vector3f rotation;
	public Vector3f scale;
	
	public Transform_3D()
	{
		translation = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
	}
	
	/**
	 * use this for getting model matrix values
	 * when passing into a shader,
	 * will use all values from this transform
	 */
	public float[] matrify()
	{
		matrix.translation(translation);
		matrix.rotateX(rotation.x);
		matrix.rotateY(rotation.y);
		matrix.rotateZ(rotation.z);
		matrix.scale(scale);
		
		return matrix.get(matrix_values);
	}
	
	/**
	 * modifies an existing matrix into this transform's matrix
	 */
	public Matrix4f matrify(Matrix4f mat)
	{
		mat.translation(translation);
		mat.rotateX(rotation.x);
		mat.rotateY(rotation.y);
		mat.rotateZ(rotation.z);
		mat.scale(scale);
		
		return mat;
	}
	
	/**
	 * modifies an existing matrix into this transform's matrix
	 *
	 * but inverted from normal (for cameras)
	 */
	public Matrix4f matrify_view(Matrix4f mat)
	{
		mat.scale(scale.x, scale.y, 1);
		mat.rotateX(-rotation.x);
		mat.rotateY(-rotation.y);
		mat.rotateZ(-rotation.z);
		mat.translate(-translation.x, -translation.y, 0);
		
		return mat;
	}
}
