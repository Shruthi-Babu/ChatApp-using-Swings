package com.shru.ChatApp.server;

import java.net.InetAddress;

public class ServerClient
{
	public String name;
	public InetAddress address;
	public int port;
	public final int ID;
	public int attempt=0;
	
	public ServerClient(String name, InetAddress address, int port, int ID)
	{
		this.name=name;
		this.address=address;
		this.port=port;
		this.ID=ID;
	}
	
	public int getId()
	{
		return ID;
	}
		

}
