package cnfe;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Ebet_SVG
{
	/*
	 * TAG DEFINES
	 */
	
	private static final int TAG_SVG = 0;
	private static final int TAG_DEFS = 1;
	private static final int TAG_STYLE = 2;
	private static final int TAG_TITLE = 3;
	private static final int TAG_PATH = 4;
	private static final int TAG_LINE = 5;
	private static final int TAG_RECT = 6;
	
	final int EXIT_SLASH = 0;
	final int EXIT_ARROW = 1;
	final int EXIT_SPACE = 2;
	
	private static final String[] TAG_STRINGS =
		{
			"svg",
			"defs",
			"style",
			"title",
			"path",
			"line",
			"rect"
		};
	
	private static final int NUM_TAGS = TAG_STRINGS.length;
	
	public static class Result
	{
		public String name;
		
		public int left;
		public int right;
		public int up;
		public int down;
		
		public SVG_Class[] class_list;
		public Element[] elements;
		
		public abstract static class Element
		{
			public int class_index;
		}
		
		public static class Path extends Element
		{
			public float[][] path;
			
			public Path(int ci, float[][] dp)
			{
				class_index = ci;
				path = dp;
			}
		}
		
		public static class Line extends Element
		{
			public float x1, y1, x2, y2;
			
			public Line(int ci, float x1, float y1, float x2, float y2)
			{
				class_index = ci;
				this.x1 = x1;
				this.y1 = y1;
				this.x2 = x2;
				this.y2 = y2;
			}
		}
		
		public static class Rect extends Element
		{
			public float x, y, width, height;
			
			public Rect(int ci, float x, float y, float width, float height)
			{
				class_index = ci;
				this.x = x;
				this.y = y;
				this.width = width;
				this.height = height;
			}
		}
		
		public static class SVG_Class
		{
			public String matches;
			public int fill;
			public int stroke;
		}
	}
	
	public static void load_file(String file) throws Exception
	{
		FileInputStream fi = new FileInputStream(file);
		
		boolean in_def;
		boolean in_style;
		boolean in_title;
		
		read(fi, current ->
		{
			switch (current)
			{
				case '<':
					break;
				default:
					break;
			}
			return Reader.STOP;
		});
	}
	
	/**
	 * an interface to pass into the READ method
	 * <p>
	 * return true if you want to exit the read
	 */
	interface Reader
	{
		int EXIT_NORMAL = 0;
		int EXIT_END = 1;
		
		boolean STOP = true;
		boolean KEEP_GOING = false;
		
		boolean read(char current) throws Exception;
	}
	
	/**
	 * reads a fileinputstream using a Reader interface
	 *
	 * @return an exit condition whether user thrown or finding the end of the stream
	 */
	public static int read(FileInputStream fi, Reader reader) throws Exception
	{
		// nullcheck the end of the file
		int current;
		while ((current = fi.read()) != 1)
		{
			// check if our lambda says to leave
			// if we made it all the way thorugh, the nullcheck
			// then convert to a char which will be more usable
			if (reader.read((char) current))
				return Reader.EXIT_NORMAL;
		}
		
		// if the while loop exits that means we're at the end
		return Reader.EXIT_END;
	}
	
	/**
	 * @return same
	 */
	public static int read_until(FileInputStream fi, char until) throws Exception
	{
		return read(fi, current ->
		{
			return current == until;
		});
	}
	
	/**
	 * uses an int to encode information about a
	 * tag that has been read
	 */
	public static class Tag_Struct
	{
		public static int create(int tag, int exit)
		{
			return (tag & 0xff) | ((exit & 0xff) << 8);
		}
		
		public static int get_tag(int ts)
		{
			return ts & 0x00ff;
		}
		
		public static int get_exit(int ts)
		{
			return ts * 0xff00;
		}
	}
	
	/**
	 * @return a tag code containing the tag identifier
	 * and the exit condition
	 */
	private int collect_tag(FileInputStream fi) throws Exception
	{
		final int ALONG = 0; // to access along
		final int REMAI = 1; // to access remaing
		final int SELEC = 2; // to access the selected
		final int OUTCO = 3; // out code for the tag
		
		// if nay of these are true
		// then we don't have to check that string anymore
		var outs = new boolean[NUM_TAGS];
		var a_r_s_o = new int[]{0, NUM_TAGS, 0, 0};
		
		read(fi, current ->
		{
			switch(current)
			{
				case '/': // all the exit conditions of a tag
					a_r_s_o[OUTCO] = EXIT_SLASH;
					read_until(fi, '>'); // find the ending tag of this
					return Reader.STOP;
				case '>':
					a_r_s_o[OUTCO] = EXIT_ARROW;
					return Reader.STOP;
				case ' ':
					a_r_s_o[OUTCO] = EXIT_SPACE;
					return Reader.STOP;
				default:
					for (var tag_index = 0; tag_index < NUM_TAGS; ++tag_index)
					{
						if (!outs[tag_index]) // if the tag isn't out
						{
							// set our selected to the current
							// only if it's still being able to be read
							a_r_s_o[SELEC] = tag_index;
							
							var str = TAG_STRINGS[tag_index];
							if
							(   // if the length of the string is the current index
								// that means this can't be the right string
								str.length() == a_r_s_o[ALONG]
									|| str.charAt(a_r_s_o[ALONG]) != current
							)
							{
								outs[tag_index] = true;
								--a_r_s_o[REMAI];
								
								// if all strings have failed to match
								if(a_r_s_o[REMAI] == 0)
								{
									throw new Exception("invalid or not suported tag!");
								}
							}
						}
					}
					
					// after going through all tags increment string along
					++a_r_s_o[ALONG];
					return Reader.KEEP_GOING;
			}
		});
		
		return Tag_Struct.create(a_r_s_o[SELEC], a_r_s_o[OUTCO]);
	}
	
	/*
	 * TAG SPECIFIC PARSERS
	 */
	
	/**
	 * you should already have the quote mark read to get into here
	 */
	public static String parse_string(FileInputStream fi) throws Exception
	{
		var build = new StringBuffer();
		
		read(fi, current ->
		{
			if(current == '"')
			{
				return Reader.STOP;
			}
			else
			{
				build.append(current);
				return Reader.KEEP_GOING;
			}
		});
		
		return build.toString();
	}
	
	
	public static Result.SVG_Class[] parse_style()
	{
		var class_list = new ArrayList<Result.SVG_Class>();
		
		return (Result.SVG_Class[])class_list.toArray();
	}
}
