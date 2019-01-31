package com.shru.ChatApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JLabel lblIpAddress;
	private JLabel lblPort;
	private JTextField txtPort;
	private JLabel lblAddressdesc;
	private JLabel lblPortDesc;
	
	public Login() 
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 300, 380);
		setSize(300,380);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(37, 71, 70, 15);
		contentPane.add(lblName);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(110, 133, 140, 25);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(12, 135, 95, 15);
		contentPane.add(lblIpAddress);
		
		txtName = new JTextField();
		txtName.setBounds(110, 69, 140, 25);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(52, 205, 57, 15);
		contentPane.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setBounds(110, 200, 140, 25);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		lblAddressdesc = new JLabel("eg  198.127.9.5");
		lblAddressdesc.setBounds(122, 157, 140, 15);
		contentPane.add(lblAddressdesc);
		
		lblPortDesc = new JLabel("eg 3011");
		lblPortDesc.setBounds(146, 224, 100, 15);
		contentPane.add(lblPortDesc);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				String name= txtName.getText();
				String address= txtAddress.getText();
				int port= Integer.parseInt(txtPort.getText());

				login(name, address, port);
			}

			
		});
		btnLogin.setBounds(97, 308, 117, 25);
		contentPane.add(btnLogin);
		
		setResizable(false);
		setTitle("Login");
	}
	
	/**
	 * Login stuff here
	 */
	private void login(String name, String address, int port)
	{
		dispose();
		new ClientWindow(name, address, port);
	}
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run() 
			{
				try 
				{
					Login frame = new Login();
					frame.setVisible(true);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
