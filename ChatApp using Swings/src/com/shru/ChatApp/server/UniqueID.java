package com.shru.ChatApp.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueID 
{
	private static List<Integer> ids= new ArrayList<Integer>();
	private static final int range= 1000;
	private static int index=0;
	
	static
	{
		for(int i=0;i<range; i++)
			ids.add(i);
		Collections.shuffle(ids);
	}
	public UniqueID()
	{
		
	}
	
	public int getID()
	{
		if(index>ids.size())
			index=0;
		return ids.get(index++);
	}

}
