package com.shru.ChatApp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;


public class Server implements Runnable
{
	private List<ServerClient> clients = new ArrayList<ServerClient>();
 	
	private int port;
	private DatagramSocket socket;	
	private Thread run, manage, send, receive;
	private boolean running =false, start;
	
	public Server(int port)
	{
		this.port=port;
		try {
			socket = new DatagramSocket(port);
		} 
		catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		run= new Thread(this, "Server");
		run.start();
	}	
	
	public void run() 
	{
		running = true;
		System.out.println("Server started on port: " +port);
		manageClients();
		receive();		
	}
	
	private void receive() 
	{
		start=true;
		receive = new Thread("Receive")
		{
			public void run()
			{
				while(running)
				{
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
					//String string = new String(packet.getData());
					process(packet); //process the received data first to find out whether its a connection packet
					
					clients.add(new ServerClient("Shru", packet.getAddress(), packet.getPort(), 50));
					if(start)	//prints address and port only at the start of the app
					{
						System.out.println(clients.get(0).address.toString() + ":" + clients.get(0).port);
						start=false;
					
					}
					
					//System.out.println(string);
				}
			}

			
		};
		receive.start();
	}
	
	private void sendToAll(String message)
	{
		for (int i = 0; i < clients.size(); i++)
		{
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}
	
	private void send(final byte[] data,final InetAddress address,final int port)
	{					//final as its being used in the inner class of thread
		send = new Thread("SEND") {
			public void run()
			{
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try
				{
					socket.send(packet);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	private void process(DatagramPacket packet)
	{
		String string = new String(packet.getData());
		if(string.startsWith("con:"))
		{
			int id= UniqueID.getID();
			clients.add(new ServerClient(string.substring(5, string.length()), packet.getAddress(), packet.getPort(), id));
			System.out.println(string.substring(4, string.length()));
			System.out.println(id);
		}
		else if(string.startsWith("all:"))
		{
			sendToAll(string);
		}
		else
		{
			System.out.println(string);
		}
	}
	
	private void manageClients()	//keeps sending ping to make sure the clients are responding
	{
		manage = new Thread("Manage")
		{
			public void run()
			{
				while(running)
				{
						//todo
				}
			}
		};
		manage.start();
	}
	
}
