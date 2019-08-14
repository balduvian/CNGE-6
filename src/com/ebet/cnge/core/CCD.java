package com.ebet.cnge.core;

public class CCD
{
	public static final boolean NORMAL = true;
	public static final boolean ANTI_NORMAL = false;
	
	public static class Line
	{
		public float x0, y0, x1, y1;
		
		public Line(float x0, float y0, float x1, float y1)
		{
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;
		}
		
		public Line()
		{
			x0 = 0;
			y0 = 0;
			x1 = 0;
			y1 = 0;
		}
	}
	
	/**
	 * returns a t value that is along the on line
	 * where it hits the other line
	 *
	 * a 0 on the t value represents (x0, y0) on the on line;
	 * a 1 represents (x1, y1);
	 *
	 * any value that is not inline 0-1 will mean that the intersection is outside
	 * the on line
	 */
	private static double intersection(Line on_li, Line other)
	{
		return
			((other.y1 - other.y0) * (on_li.x0 - other.x0) - (other.x1 - other.x0) * (on_li.y0 - other.y0))/
			
			((other.x1 - other.x0) * (on_li.y1 - on_li.y0) - (other.y1 - other.y0) * (on_li.x1 - on_li.x0));
	}
	
	private static boolean inline(double t)
	{
		return !(t > 1 || t < 0);
	}
	
	/**
	 * returns the which side of the line the point (x, y) is
	 * NORMAL or ANTI_NORMAL
	 */
	private static boolean side_of(float x, float y, Line line)
	{
		return (line.y1 - line.y0) * (line.x0 - x) + (line.x1 - line.x0) * (y - line.y0) > 0;
	}
	
	public static double closestPoint(float x0, float y0, Line wall) {
		return (
			Math.pow(wall.x0 - x0, 2) + Math.pow(wall.y0 - y0, 2) + Math.pow(wall.x1 - wall.x0, 2) + Math.pow(wall.y1 - wall.y0, 2) - Math.pow(wall.x1 - x0, 2) - Math.pow(wall.y1 - y0, 2)
		) / (
			2 * (Math.pow(wall.x1 - wall.x0, 2) + Math.pow(wall.y1 - wall.y0, 2))
		);
	}
}
