import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		ImageIcon icon = new ImageIcon("icon.png");
		String[] options = { "Normal game", "Human Vs Computer", "Custom game", "Create local game", "Connect to server" };

		int response = JOptionPane.showOptionDialog(null, "Choose game type:", "Game Settings",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);

		switch (response) {
		case -1:
			System.out.println("Option Dialog Window Was Closed");
			System.exit(0);

		case 0:
			new Game();
			break;
		case 1:
			System.exit(0);
			break;

		case 2:
			createCustomGame();
			break;
		case 3:
			createLocalGame();
			break;

		default:
			break;
		}

	}
	
	public static void createLocalGame() {
		boolean connect = false;
		try {
			String ip = String.valueOf(Inet4Address.getLocalHost());
			ip = ip.substring(ip.indexOf("/")+1);
			
			JOptionPane.showMessageDialog(null,"find \n d"
					);
			ServerSocket serverSocket = new  ServerSocket(9999);
			Socket socket = serverSocket.accept();
			
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	        

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			System.err.println("sas");
		}
	}
	

	public static void createCustomGame() {
		JTextField numOfRowfField = new JTextField();
		JTextField numOfColumnfField = new JTextField();
		JTextField numOfSequencefField = new JTextField();
		final JComponent[] inputs = new JComponent[] { new JLabel("Type the number of row:"), numOfRowfField,
				new JLabel("Type the number of column:"), numOfColumnfField, new JLabel("Type the number of sequence:"),
				numOfSequencefField };
		int result = JOptionPane.showConfirmDialog(null, inputs, "Custom game", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			new Game(Integer.parseInt(numOfRowfField.getText()), Integer.parseInt(numOfColumnfField.getText()),
					Integer.parseInt(numOfSequencefField.getText()));
		} else {
			System.out.println("User canceled / closed the dialog, result = " + result);
		}
	}

}
