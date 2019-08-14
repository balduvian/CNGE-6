package cnfe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import static cnfe.Glyph.Segment.*;

public class Curve
{

    public static void main(String[] args)
    {
        curve_init();
    }
    
    public static void curve_init()
    {
        var image = Image.create_image(100, 100);
        
        Glyph glyph = new Glyph(new Glyph.Segment[]
        {
            create_curve(3.5f, 2.25f, 7.3f, 3.2f, 8.3f, 7.2f),
            create_curve(8.3f, 7.2f, 6.136f, 9.364f, 0.764f, 7.349f),
            create_curve(0.764f, 7.349f, 0.267f, 3.867f, 3.5f, 2.25f),
        });
        
        glyph.draw(10, 10, 5, 5, image);
        
        // save
        var j_image = image.j_convert();
        
        try
        {
            ImageIO.write(j_image, "png", new File("kek.png"));
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static class Vector
    {
        // use this to construct
        public static Vector vec(float x, float y)
        {
            return new Vector(x, y);
        }
        
        private Vector(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
    
        /**
         * modifies the vector being passed in
         * with the first variable being the smallest
         * and the last being the biggest
         */
        public static Vector min_max(Vector v)
        {
            // if x needs to be in the last place
            // then switch
            if(v.x > v.y)
            {
                var temp = v.x;
                v.x = v.y;
                v.y = temp;
            }
            
            return v;
        }
        
        public float length()
        {
            return (float)Math.sqrt(x*x + y*y);
        }
        
        public float x, y;
    }
    
    public static class Image
    {
        public static final int    COLOR_BACK = 0x000000ff;
        public static final int   COLOR_WHITE = 0xffffffff;
    
        private Image(int w, int h, int canvas_co)
        {
            width = w;
            height = h;
            pixels = new int[w][h];
            iterate(() -> { return canvas_co; });
        }
        
        public static Image create_image(int w, int h, int canvas_co)
        {
            return new Image(w, h, canvas_co);
        }
    
        public static Image create_image(int w, int h)
        {
            return new Image(w, h, COLOR_WHITE);
        }
        
        /**
         * gives you the position in the array
         * as well as the current color
         * returns the color to set
         */
        public interface Iter
        {
            int iter(int x, int y, int c);
        }
    
        /**
         * only to set, no other info given
         */
        public interface Iter_S
        {
            int iter();
        }
        
        public void iterate(Iter iter)
        {
            for(var i = 0; i < width; ++i)
            {
                for(var j = 0; j < height; ++j)
                {
                    pixels[i][j] = iter.iter(i, j, pixels[i][j]);
                }
            }
        }
    
        public void iterate(Iter_S iter)
        {
            for(var i = 0; i < width; ++i)
            {
                for(var j = 0; j < height; ++j)
                {
                    pixels[i][j] = iter.iter();
                }
            }
        }
    
        /**
         * draws a line between the endpoints of two vectors
         */
        public void draw_line(float x0, float y0, float x1, float y1, int co)
        {
            var x_poss = Vector.min_max(Vector.vec(x0, x1));
            var y_poss = Vector.min_max(Vector.vec(y0, y1));
            
            var left = x_poss.x;
            var right = x_poss.y;
            var up = y_poss.x;
            var down = y_poss.y;
    
            var slope = (y1 - y0) / (x1 - x0);
            
            // put a pixel on each column
            if(right - left > down - up)
            {
                for(var i = (int)Math.round(left); i <= (int)Math.round(right); ++i)
                {
                    var y_val = (int)Math.round(slope * (i - x0) + y0);
                    pixels[i][y_val] = co;
                }
            }
            // put a pixel on each row
            else
            {
                for(var j = (int)Math.round(up); j <= (int)Math.round(down); ++j)
                {
                    var x_val = (int)Math.round((j - y0) / slope + x0);
                    pixels[x_val][j] = co;
                }
            }
        }
    
        public static int c_alph(int color, byte alph)
        {
            return (color << 8) & alph;
        }
        
        public BufferedImage j_convert()
        {
            var b = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            
            for(int i = 0; i < height; ++i)
            {
                b.setRGB(0, i, width, 1, pixels[i], 0, width);
            }
            
            return b;
        }
        
        private int width, height;
        private int[][] pixels;
    }

}
