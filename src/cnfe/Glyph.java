package cnfe;

import cnfe.Curve.Image;
import static cnfe.Curve.Vector.*;

public class Glyph
{
    public static class Segment
    {
        public static Segment create_straight
        (
            float x0, float y0, float x1, float y1
        )
        {
            return new Segment(new float[][]{{x0, y0}, {x1, y1}});
        }
    
        public static Segment create_curve
        (
            float x0, float y0, float p0, float p1, float x1, float y1
        )
        {
            return new Segment(new float[][]{{x0, y0}, {p0, p1}, {x1, y1}});
        }
        
        private Segment(float[][] ps)
        {
            points = ps;
            num_points = ps.length;
        }
    
        public interface Get { void get(int i, float[] f); }
        
        public void get(Get getter)
        {
            for(var i = 0; i < num_points; ++i)
            {
                getter.get(i, points[i]);
            }
        }
        
        private int num_points;
        private float[][] points;
    }
    
    private int num_curves;
    private Segment[] curves;
    
    public Glyph(Segment[] ps)
    {
        curves = ps;
    }
    
    public static float interp(float s, float e, float t)
    {
        return (e - s) * t + s;
    }
    
    public void draw(int x, int y, int sx, int sy, Image image)
    {
        for(var p : curves)
        {
            float[][] points = new float[3][2];
            var count = new int[]{0};
            
            p.get((var i, var point) ->
            {
                points[i][0] = point[0];
                points[i][1] = point[1];
                ++count[0];
            });
            
            if(count[0] == 3)
            {
                // create pretty names for our variables
                var x0 = points[0][0];
                var y0 = points[0][1];
                var px = points[1][0];
                var py = points[1][1];
                var x1 = points[2][0];
                var y1 = points[2][1];
                
                // find the distances of both the lines
                // that connect to the midpoint
                
                var length = vec(px - x0, py - y0).length();
                length += vec(x1 - px, y1 - py).length();
                
                // how many sub segments we create to approximate the curve
                var splits = (int)length;
                
                for(var i = 0; i < splits; ++i)
                {
                    var per = (float)i / splits;
                    var bz_x0 = interp(interp(x0, px, per), interp(px, x1, per), per);
                    var bz_y0 = interp(interp(y0, py, per), interp(py, y1, per), per);
                    per = (i + 1.0f) / splits;
                    var bz_x1 = interp(interp(x0, px, per), interp(px, x1, per), per);
                    var bz_y1 = interp(interp(y0, py, per), interp(py, y1, per), per);
    
                    image.draw_line(x + bz_x0 * sx, y + bz_y0 * sy, x + bz_x1 * sx, y + bz_y1 * sy, Image.COLOR_BACK);
                }
            }
            else
            {
                image.draw_line(x + points[0][0], y + points[0][1], x + points[1][0], y + points[1][1], Image.COLOR_BACK);
            }
        }
    }
}
