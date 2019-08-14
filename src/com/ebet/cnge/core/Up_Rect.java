package com.ebet.cnge.core;

import com.ebet.cnge.engine.VAO;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

/**
 * this rect is upside down from the normal rect
 * used with a posup projection matrix
 */
public class Up_Rect extends VAO
{
	/*       0, 1 - - - 1, 1                 */
	/*         | 2      1 |                  */
	/*         |          |                  */
	/*         | 3      0 |                  */
	/*       0, 0 - - - 1, 0                 */
	
	public Up_Rect()
	{
		super(
			GL_TRIANGLES,
			new float[]
			{
				1, 0, 0,
				1, 1, 0,
				0, 1, 0,
				0, 0, 0,
			},
			new int[]
			{
				0, 1, 2,
				2, 3, 0
			},
			new Attribute[]
			{
				// texture coordinates
				new Attribute(
					2,
					new float[]
					{
						1, 0,
						1, 1,
						0, 1,
						0, 0
					}
				)
			}
		);
	}
}
