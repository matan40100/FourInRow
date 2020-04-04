import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Main {
	private static final int HUMAN_VS_HUMAN = 0;
	private static final int HUMAN_VS_COMPUTER = 1;
	
	private static final int DEFAULT_ROWS = 6;
	private static final int DEFAULT_COLUMNS = 7;
	private static final int DEFAULT_SEQUENCE = 4;


	private static final int EASY = 0;
	private static final int MEDIUM = 1;
	private static final int HARD = 2;
	private static int gameTypeID;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 14));
		UIManager.put("OptionPane.buttonPadding",15);
		

		ImageIcon icon = new ImageIcon("images/icon.png");
		String[] options = { "Normal game", "Human Vs Computer", "Custom game" };

		
		int response = JOptionPane.showOptionDialog(null, "Choose game type:", "Game Settings",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);
		
		switch (response) {
			case -1:
				System.out.println("Option Dialog Window Was Closed");
				System.exit(0);

			case 0:
				new Game(DEFAULT_ROWS, DEFAULT_COLUMNS, DEFAULT_SEQUENCE, HUMAN_VS_HUMAN,0);
				break;
			case 1:
				new Game(DEFAULT_ROWS, DEFAULT_COLUMNS, DEFAULT_SEQUENCE, HUMAN_VS_COMPUTER,0);
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
		String[] gameType = { "Human VS Human", "Human VS Computer" };
		JComboBox<String> gameTypeList = new JComboBox<>(gameType);

		final JComponent[] inputs = new JComponent[] { new JLabel("Type the number of row:"), numOfRowfField,
				new JLabel("Type the number of column:"), numOfColumnfField,
				new JLabel("Type the number of sequence(4 and up):"), numOfSequencefField,
				new JLabel("Choose game mode:"), gameTypeList };

		gameTypeList.addActionListener(e -> {
			gameTypeID = gameTypeList.getSelectedIndex();
			
		});

		int result = JOptionPane.showConfirmDialog(null, inputs, "Custom game", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			if (gameTypeID == HUMAN_VS_HUMAN) {
				new Game(Integer.parseInt(numOfRowfField.getText()), Integer.parseInt(numOfColumnfField.getText()),
						Integer.parseInt(numOfSequencefField.getText()), gameTypeID, 0);
			} else if (gameTypeID == HUMAN_VS_COMPUTER) {
				String[] difficultyList = { "Easy", "Medium", "Hard" };

				int response = JOptionPane.showOptionDialog(null, "Choose difficulty level:", "Game Settings",
						JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, difficultyList,
						difficultyList[0]);
				switch (response) {
					case -1:
						System.out.println("Option Dialog Window Was Closed");
						System.exit(0);

					case 0:
						new Game(Integer.parseInt(numOfRowfField.getText()),
								Integer.parseInt(numOfColumnfField.getText()),
								Integer.parseInt(numOfSequencefField.getText()), gameTypeID, EASY);
						break;
					case 1:
						new Game(Integer.parseInt(numOfRowfField.getText()),
								Integer.parseInt(numOfColumnfField.getText()),
								Integer.parseInt(numOfSequencefField.getText()), gameTypeID, MEDIUM);
						break;

					case 2:
						new Game(Integer.parseInt(numOfRowfField.getText()),
								Integer.parseInt(numOfColumnfField.getText()),
								Integer.parseInt(numOfSequencefField.getText()), gameTypeID, HARD);
						break;

					default:
						break;
				}

			}

		} else {
			System.out.println("User canceled / closed the dialog, result = " + result);
		}
	}

}
