package com.shru.ChatApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class ClientWindow  extends JFrame implements Runnable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;
	
	private boolean running = false;
	private Thread listen, run; 
	
	private Client client;
	
	public ClientWindow(String name, String address, int port)
	{
		client = new Client(name, address, port);
		
		boolean connect = client.openConnection(address);
		if(!connect)
		{
			System.err.println("Connection Failed!");
			console("connection Failed!!");
		}
		
		createWindow();
		console("Connected to " + address + ":" + port + "\t User:" +name);	
		
		String connection = name + " connected from " + address +":" + port; //stuffs entered in login window
		client.send(connection.getBytes());
		
		run = new Thread(this, "Running");
		run.start();
		running = true;
		
		
		String trial= "/c/" + name;			//connects to the server
		client.send(trial.getBytes());
	}	
	
	private void createWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//DISPOSE_ON_CLOSE
		setSize(600,400);
		setLocationRelativeTo(null);		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{10, 550,30, 10};
		gbl_contentPane.rowHeights = new int[]{15, 375, 0, 10};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE, 0.0};
		contentPane.setLayout(gbl_contentPane);
		
		history = new JTextArea();
		history.setEditable(false);
		caret = (DefaultCaret)history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scroll = new JScrollPane(history);

		GridBagConstraints scrollConstraints = new GridBagConstraints();		
		scrollConstraints.insets = new Insets(5, 5, 5, 10);		
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 1;
		scrollConstraints.gridy = 1;
		scrollConstraints.gridwidth=2;
		//scrollConstraints.gridheight=3;

		
		contentPane.add(scroll, scrollConstraints);
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter()
		{			
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				{
					sendbutton(txtMessage.getText(), true);
				}
			}
		});
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 5, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 1;
		gbc_txtMessage.gridy = 2;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
			 sendbutton(txtMessage.getText(), true);
			}
		});		
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);		
		
		
		txtMessage.requestFocusInWindow();
		setVisible(true);	
		setTitle("Client Window");
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{

				String disconnect = "/d/" + client.getID();	
				sendbutton(disconnect , false);			
				running=false;
				client.close();
				setVisible(false);
				dispose();
			}
		});	
	}	
	
	public void run()
	{
		listen();
	}
	
	private void sendbutton(String msg , boolean type)
	{
		if(msg.equals(""))
			return;
		if(type)
		{
			String name= client.getname();		
			msg= name + ": " + msg;		
			msg="/m/" + msg;
		}
		client.send(msg.getBytes());		//sending message data through sockets
		if(!type)
		{
			dispose();
		}
		txtMessage.setText("");		
	}
	
	public void listen()
	{
		listen = new Thread("LISTEN")
			{
				public void run()
				{
					
					while(running)
					{
						String message = client.receive();
						if(message.startsWith("/c/"))
						{
							//client.setID(Integer.parseInt(message.substring(4,message.length())));
							client.setID(Integer.parseInt(message.split("/c/")[1].trim()));
							console("Successfully connected to Server! \t ID: "+ client.getID());
						}
						else if(message.startsWith("/m/"))
						{
							String text= message.split("/m/")[1].trim();
							console(text);	//displays message data in the text area
						}
					}
				}
			};
			listen.start();
	}
	
	public void console(String msg)
	{
		history.append(msg + "\n");
		history.setCaretPosition(history.getDocument().getLength());
	}
	

}
