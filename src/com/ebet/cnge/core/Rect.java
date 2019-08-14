package com.ebet.cnge.core;

import com.ebet.cnge.engine.VAO;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class Rect extends VAO
{
	/*       0, 0 - - - 1, 0                 */
	/*         | 2      1 |                  */
	/*         |          |                  */
	/*         | 3      0 |                  */
	/*       0, 1 - - - 1, 1                 */
	
	public Rect()
	{
		super(
			GL_TRIANGLES,
			new float[]
			{
				1, 1, 0,
				1, 0, 0,
				0, 0, 0,
				0, 1, 0,
			},
			new int[]
			{
				0, 1, 2,
				2, 3, 0
			},
			new VAO.Attribute[]
			{
				// texture coordinates
				new VAO.Attribute(
					2,
					new float[]
					{
						1, 1,
						1, 0,
						0, 0,
						0, 1
					}
				)
			}
		);
	}
}
