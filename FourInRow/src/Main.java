import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
	private static final int  HUMAN_VS_HUMAN =1;
	private static final int  HUMAN_VS_COMPUTER=2;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImageIcon icon = new ImageIcon("images/icon.png");
		String[] options = { "Normal game", "Human Vs Computer", "Custom game" };

		int response = JOptionPane.showOptionDialog(null, "Choose game type:", "Game Settings",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);

		switch (response) {
		case -1:
			System.out.println("Option Dialog Window Was Closed");
			System.exit(0);

		case 0:
			new Game(HUMAN_VS_HUMAN);
			break;
		case 1:
			new Game(HUMAN_VS_COMPUTER);
			break;

		case 2:
			createCustomGame();
			break;

		default:
			break;
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
