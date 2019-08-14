package com.ebet.cnge.core;

public class Block_Parts
{
	/*                        */
	/* order of block return  */
	/*                        */
	/*        0  ->  1        */
	/*               |        */
	/*               v        */
	/*        3  <-  2        */
	/*                        */
	
	private static int[] block_return = new int[4];
	
	public static int[] get(boolean[] spaces)
	{
		return get(spaces[0], spaces[1], spaces[2], spaces[3], spaces[4], spaces[5], spaces[6], spaces[7]);
	}
	
	public static int[] get(boolean l, boolean lu, boolean u, boolean ur, boolean r, boolean rd, boolean d, boolean dl)
	{
		//top left
		if (!l && !u)
			insert(0, 0);
		else if(l && !lu && u)
			insert(0, 1);
		else if(!l && u)
			insert(0, 2);
		else if(l && !u)
			insert(0, 3);
		else
			insert(0, 4);
		
		//top right
		if (!u && !r)
			insert(1, 0);
		else if(u && !ur && r)
			insert(1, 1);
		else if(u && !r)
			insert(1, 2);
		else if(!u && r)
			insert(1, 3);
		else
			insert(1, 4);
		
		//bottom right
		if (!r && !d)
			insert(2, 0);
		else if(r && !rd && d)
			insert(2, 1);
		else if(!r && d)
			insert(2, 2);
		else if(r && !d)
			insert(2, 3);
		else
			insert(2, 4);
		
		//bottom left
		if (!d && !l)
			insert(3, 0);
		else if(d && !dl && l)
			insert(3, 1);
		else if(d && !l)
			insert(3, 2);
		else if(!d && l)
			insert(3, 3);
		else
			insert(3, 4);
		
		return block_return;
	}
	
	private static void insert(int corner, int x)
	{
		block_return[corner] = x;
	}
}
