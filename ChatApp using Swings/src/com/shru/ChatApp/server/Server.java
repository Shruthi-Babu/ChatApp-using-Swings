package com.shru.ChatApp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;


public class Server implements Runnable
{
	private List<ServerClient> clientlist = new ArrayList<ServerClient>();
 	
	private int port;
	private DatagramSocket socket;	
	private Thread run, send, receive;
	private boolean running = false, start;
	
	private UniqueID uni;
	
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
		uni= new UniqueID();
		run= new Thread(this, "Server");
		run.start();
	}	
	
	public void run() 
	{
		running = true;
		System.out.println("Server started on port: " +port);
		//manageClients();
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
					try
					{
						socket.receive(packet);
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
					//String string = new String(packet.getData());

					process(packet); //process the received data first to find out whether its a connection packet					
					
					//System.out.println(string);
				}
			}

			
		};
		receive.start();
	}
	
	private void sendToAll(String message)
	{
		for (int i = 0; i < clientlist.size(); i++)
		{
			ServerClient client = clientlist.get(i);
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
	
	private void process(DatagramPacket packet) //process datapacket recvd to find whether to connect/disconnect/etc
	{
		String string = new String(packet.getData());
		if(string.startsWith("/c/"))
		{
			int id= uni.getID();
			
			clientlist.add(new ServerClient(string.substring(3, string.length()), packet.getAddress(), packet.getPort(), id));
			
			System.out.println("Client added:" + string.substring(3, string.length()));
			System.out.println("Client ID: "+ id);			

			String ID= "/c/" + id;	
			send(ID.getBytes(), packet.getAddress(), packet.getPort());		
		}
		else if(string.startsWith("/m/"))
		{
			sendToAll(string);
		}
		else if(string.startsWith("/d/"))
			{
			String id= string.split("/d/")[1].trim();
			disconnect(Integer.parseInt(id), true);
			}
		else
		{
			System.out.println(string);
		}
	}
	
	private void disconnect(int id, boolean status)
	{	String msg= "";
		ServerClient c = null;
		for(int i=0;i<clientlist.size();i++)
		{

			if(clientlist.get(i).getId() == id)
			{
				c= clientlist.get(i);
				clientlist.remove(i);
				break;
			}
		}
		if(status)
		{
			msg= "Client disconnected: " + c.name ;
			System.out.println(msg);

		}
		

	}

//	private void manageClients()	//keeps sending ping to make sure the clients are responding
//	{
//		manage = new Thread("Manage")
//		{
//			public void run()
//			{
//				while(running)
//				{
//						//todo
//				}
//			}
//		};
//		manage.start();
//	}
	
}
