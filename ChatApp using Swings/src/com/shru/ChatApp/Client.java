package com.shru.ChatApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client 
{

	private static final long serialVersionUID = 1L;

	private String name, address;
	private int port;	
	private DatagramSocket socket;
	private InetAddress ip;
	private Thread send;	
	private int ID=-1;

	
	public Client(String name, String address, int port)
	{
		this.name= name;
		this.address=address;
		this.port=port;
	}
		
	
	public boolean openConnection(String address)  
	{
		try 
		{
			socket = new DatagramSocket();
			ip=InetAddress.getByName(address);
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			return false;
		} catch (SocketException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;		
	}
	
	public String receive()
	{
		byte data[]= new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		
		return message;
	}
	
	public void send(final byte[] data)
	{
		send= new Thread("Send"){		//anonymous inner class, so "data" has to be final
			public void run()
			{
			DatagramPacket packet = new DatagramPacket(data, data.length, ip, port); 
			try {
				socket.send(packet);		//currently displays stuff on server(console)
				} 
			catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		};
		send.start();
	}


	public String getname()
	{
		return name;
	}
	
	public int getport()
	{
		return port;
	}

	public String getAddress()
	{
		return address;
	}


	public void setID(int ID) 
	{
		this.ID=ID;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public void close()
	{
		synchronized(socket) {
			socket.close();

		}
	}
	

}
