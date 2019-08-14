package cnfe;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Char_Gen
{
	public static void main(String[] args)
	{
		try
		{
			var width = 32;
			var height = 48;
			var shift_up = 16;
			var shift_right = 4;
			
			char start = 32;
			char end = 127;
			
			char c = start;
			while(c < end)
			{
				Runtime.getRuntime().exec("C:/Users/Emmet/Programming/programs/msdfgen/msdfgen.exe msdf -font arial.ttf " + (int)c + " -o " + (int)c + ".png -size " + width + " " + height + " -translate " + shift_right + " " + shift_up, null, new File("C:/Users/Emmet/Programming/programs/msdfgen/"));
				++c;
			}
			
			var dif = end - start;
			
			var img = new BufferedImage(width * dif, height, BufferedImage.TYPE_INT_ARGB);
			var graph = img.getGraphics();
			
			var offset = 0;
			
			c = start;
			while(c < end)
			{
				var sub_img = ImageIO.read(new File("C:/Users/Emmet/Programming/programs/msdfgen/" + (int)c + ".png"));
				
				graph.drawImage(sub_img, offset, 0, width, height, null);
				
				offset += width;
				++c;
			}
			
			ImageIO.write(img, "png", new File("C:/Users/Emmet/Programming/programs/msdfgen/full.png"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
