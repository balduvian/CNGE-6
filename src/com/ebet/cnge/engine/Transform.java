package com.ebet.cnge.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import static com.ebet.cnge.engine.Util.m16;

public class Transform
{
	/*                                              */
	/*           default matrices for shaders       */
	/*                                              */
	
	/**
	 * default model doesn't change the projection in any way
	 */
	public static float[] default_model = new Matrix4f().get(m16());
	
	/**
	 * default projeection will take up the entire NDC coordinate system
	 */
	public static float[] default_projection = new Matrix4f().translation(-1, -1, 0).scale(2, 2, 1).get(m16());
	
	/*                                              */
	/*           shared matrix equipment            */
	/*                                              */
	
	public static Matrix4f matrix = new Matrix4f();
	public static float[] matrix_values = m16();
	
	public Vector2f translation;
	public float rotation;
	public Vector2f scale;
	
	public Transform()
	{
		translation = new Vector2f(0, 0);
		rotation = 0;
		scale = new Vector2f(1, 1);
	}
	
	/**
	 * use this for getting model matrix values
	 * when passing into a shader,
	 * will use all values from this transform
	 */
	public float[] matrify()
	{
		matrix.translation(translation.x, translation.y, 0);
		matrix.rotateZ(rotation);
		matrix.scale(scale.x, scale.y, 1);
		
		matrix.get(matrix_values);
		return matrix_values;
	}
	
	/**
	 * makes a model matrix without having to have an instance,
	 *
	 * NOTE: doesn't have rotation
	 */
	public static float[] matrify(float x, float y, float width, float height)
	{
		matrix.translation(x, y, 0);
		matrix.scale(width, height, 1);
		
		matrix.get(matrix_values);
		return matrix_values;
	}
	
	/**
	 * modifies an existing matrix into this transform's matrix
	 */
	public Matrix4f matrify(Matrix4f mat)
	{
		mat.translation(translation.x, translation.y, 0);
		mat.rotateZ(rotation);
		mat.scale(scale.x, scale.y, 1);
		
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
		mat.rotateZ(-rotation);
		mat.translate(-translation.x, -translation.y, 0);
		
		return mat;
	}
}
