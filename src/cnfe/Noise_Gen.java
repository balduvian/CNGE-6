package cnfe;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Noise_Gen
{
	
	public static void main(String[] args)
	{
		int w = 1000;
		int h = 1000;
		
		int[][] mm = new int[w][h];
		
		gen_noise(mm, (int)(Math.random()*100), (int)(Math.random()*100), 45, 45);
		
		try
		{
			BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			
			for (int i = 0; i < w; ++i)
			{
				b.setRGB(i, 0, 1, h, mm[i], 0, 1);
			}
			
			ImageIO.write(b, "png", new File("kek.png"));
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	//public static double random(double x, double y)
	//{
	//	var a = Math.abs(Math.sin(x + y + 3243.2313) * 10923.324123 + Math.cos(y * (x - 213.21344) * 9330.3223456));
	//	return a - (int) a;
	//}
	
	public static double random(double x, double y)
	{
		var r = new Random();
		var pair = 0.5 * (x + y) * (x + y + 1) + y;
		r.setSeed((long)pair);
		return r.nextDouble();
	}
	
	public static int[][] gen_noise(int[][] map, int x, int y, int width, int height)
	{
		var m_w = map.length;
		var m_h = map[0].length;
		
		// actually do map;
		for (int i = 0; i < m_w; ++i)
		{
			for (int j = 0; j < m_h; ++j)
			{
				float inside_x = (i % width) / (float)width;
				float inside_y = (j % height) / (float)height;
				
				int middle_x = i / width;
				int middle_y = j / height;
				
				var total = new double[3];
				
				for (int k = -1; k < 2; ++k)
				{
					for (int l = -1; l < 2; ++l)
					{
						for (int c = 0; c < 3; ++c)
						{
							var c_num = (c * 3244.4325) - c + 33.432;
							//System.out.println(c_num);
							
							float dot_x = (float) random((middle_x + k + x)                 , (middle_y + l + y)                );
							float dot_y = (float) random((middle_x + k + x) * 231.213324 * c_num, (middle_y + l + y) * 3243.3422 * c_num);
							float dot_z = (float) random((middle_x + k + x) * 32423.2134 * c_num, (middle_y + l + y) * 323.12983 * c_num);
							
							float dist_x = (inside_x - (k + dot_x));
							float dist_y = (inside_y - (l + dot_y));
							float dist_z = ((dot_z));
							
							var temp_total = 1 - Math.sqrt(dist_x * dist_x + dist_y * dist_y + dist_z * dist_z);
							if (temp_total < 0)
							{
								temp_total = 0;
							}
							
							total[c] += temp_total;
						}
					}
				}

				total[0] /= 3;
				total[1] /= 3;
				total[2] /= 3;
				
				if(gate(total, 0.35))
				{
					map[i][j] = 0xffffffff;
				}
				else if(gate(total, 0.325))
				{
					map[i][j] = 0xffaaaaaa;
				}
				else if(gate(total, 0.30))
				{
					map[i][j] = 0xff666666;
				}
				else
				{
					map[i][j] = 0xff000000;
				}
				
				//var red = (int) (total[0] * 255);
				//var green = (int) (total[1] * 255);
				//var blue = (int) (total[2] * 255);
				
				//convert brightness to an ARGB value
				
				//map[i][j] = (0xff << 24) | ((red & 0xff) << 16) | ((green & 0xff) << 8) | ((blue & 0xff));
		
			}
		}
		
		return map;
	}
	
	public static boolean gate(double[] total, double gate)
	{
		int count_pass = 0;
		
		if(total[0] > gate)
			++count_pass;
		if(total[1] > gate)
			++count_pass;
		if(total[2] > gate)
			++count_pass;
		
		return count_pass > 1;
	}
	
	public static float along(int value, float width)
	{
		return ((value % width) / width);
	}
	
	public static float distance(float x0, double x1, float y0, double y1)
	{
		var x = x1 - x0;
		var y = y1 - y0;
		
		return (float)Math.sqrt(x * x + y * y);
	}
}
