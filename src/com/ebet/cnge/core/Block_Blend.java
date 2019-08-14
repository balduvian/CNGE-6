package com.ebet.cnge.core;

public class Block_Blend
{
	public static final int NOT = 0;
	public static final int YES = 1;
	public static final int MAY = 2;
	
	// the places in the tilemap where the block is
	int x[];
	int y[];
	
	int l[];
	int lu[];
	int u[];
	int ur[];
	int r[];
	int rd[];
	int d[];
	int dl[];
	
	private int width;
	private int num_blocks;
	private int block_pointer;
	
	private static final int[] output_values = new int[2];
	
	public Block_Blend(int num_blocks, int width)
	{
		this.num_blocks = num_blocks;
		this.width = width;
		
		x  = new int[num_blocks];
		y  = new int[num_blocks];
		l  = new int[num_blocks];
		lu = new int[num_blocks];
		u  = new int[num_blocks];
		ur = new int[num_blocks];
		r  = new int[num_blocks];
		rd = new int[num_blocks];
		d  = new int[num_blocks];
		dl = new int[num_blocks];
		
		block_pointer = 0;
	}
	
	public void register(int _l, int _lu, int _u, int _ur, int _r, int _rd, int _d, int _dl)
	{
		var p = block_pointer;
		var x_pos = block_pointer % width;
		var y_pos = block_pointer / width;
		++block_pointer;
		
		 x[p] =  x_pos;
		 y[p] =  y_pos;
		 l[p] =  _l;
		lu[p] = _lu;
		 u[p] =  _u;
		ur[p] = _ur;
		 r[p] =  _r;
		rd[p] = _rd;
		 d[p] =  _d;
		dl[p] = _dl;
	}
	
	/**
	 * place in true for each block that the current block will connect with,
	 *
	 * we don't neccessarily guarantee that this will find you a good block,
	 *
	 * please don't try and modify the array that this returns
	 */
	public int[] get(boolean _l, boolean _lu, boolean _u, boolean _ur, boolean _r, boolean _rd, boolean _d, boolean _dl)
	{
		for(int i = 0; i < block_pointer; ++i)
		{
			// do a sub check for every block position relative to this
			if
			(
				  sub_check( _l,  l[i])
				& sub_check(_lu, lu[i])
				& sub_check( _u,  u[i])
				& sub_check(_ur, ur[i])
				& sub_check( _r,  r[i])
				& sub_check(_rd, rd[i])
				& sub_check( _d,  d[i])
				& sub_check(_dl, dl[i])
			)
			{
				output_values[0] = x[i];
				output_values[1] = y[i];
				
				return output_values;
			}
		}
		
		// if we didn't find anything just go for the first block value
		output_values[0] = 0;
		output_values[1] = 0;
		
		return output_values;
	}
	
	/**
	 * may blocks are the equivilent of not caring
	 */
	private boolean sub_check(boolean block_there, int test)
	{
		return test == MAY | block_there ? test == YES : test == NOT;
	}
}
