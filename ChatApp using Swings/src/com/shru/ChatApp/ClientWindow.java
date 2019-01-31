package com.shru.ChatApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class ClientWindow  extends JFrame
{
	
	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;
	
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
		
		String trial= "con:" + name;
		client.send(trial.getBytes());
	}	
	
	private void createWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
					sendbutton(txtMessage.getText());
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
			 sendbutton(txtMessage.getText());
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
	}
	
	private void sendbutton(String msg)
	{
		if(msg.equals(""))
			return;
		String name= client.getname();
		msg= name + ": " + msg;
		console(msg);					//displays message data in the text area
		
		client.send(msg.getBytes());		//sending message data through sockets
		
		txtMessage.setText("");
		
	}
	public void console(String msg)
	{
		history.append(msg + "\n");
		history.setCaretPosition(history.getDocument().getLength());
	}
	

}
