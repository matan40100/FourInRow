import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

import javax.swing.*;

public class Game extends JFrame {
	// VERSION: 1.8
	// LAST EDIT: 25.01.20

	// Free space - 0, Player one(RED) - 1, Player two(BLUE) - 2, Computer - 3
	// HUMAN - 1, COMPUTER - 2
	private static final int HUMAN_VS_HUMAN = 1;
	private static final int HUMAN_VS_COMPUTER = 2;
	private int gameType;
	private int turn;

	private final int FREE_SPACE = 0;
	private final int PLAYER_ONE = 1;
	private final int PLAYER_TWO = 2;

	private final int COMPUTER = 3;
	private final int HUMAN = 4;

	private int numOfRow;
	private int numOfColumn;
	private int sequence;
	private Button[][] graphicsBoard;
	private int[][] logicalBoard;
	private int[] currentRowIndex;

	private Panel topPanel;
	private Panel mainPanel;
	private GridLayout gridLayout;
	private JLabel turnIcon;

	// Builder for normal game(Human VS. Human / Human VS. Computer).
	public Game(int gameType) {

		Random rand = new Random();

		this.numOfRow = 6;
		this.numOfColumn = 7;
		this.sequence = 4;
		this.turn = rand.nextInt(2 - 1 + 1) + 1;
		this.gameType = gameType;

		initBoard();
		setTitle("FourInRow");
		setSize(this.numOfRow * 115, this.numOfColumn * 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		changeTurnIcon(turn);
	}

	// Builder for custom game.
	public Game(int numOfRow, int numOfColumn, int sequence) {

		Random rand = new Random();

		this.numOfRow = numOfRow;
		this.numOfColumn = numOfColumn;
		this.sequence = sequence;
		this.turn = rand.nextInt(2 - 1 + 1) + 1;
		this.gameType = HUMAN_VS_HUMAN;

		initBoard();
		setTitle("FourInRow");
		setSize(this.numOfRow * 115, this.numOfColumn * 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		changeTurnIcon(turn);
	}

	// Change the icon turn every turn.
	public void changeTurnIcon(int turn) {
		ImageIcon icon;
		Image img;
		if (turn == 1) {
			icon = new ImageIcon("images/player_one_icon.png");
			img = icon.getImage().getScaledInstance(60, 60, icon.getImage().SCALE_SMOOTH);
			this.turnIcon.setIcon(new ImageIcon(img));

		} else {
			icon = new ImageIcon("images/player_two_icon.png");
			img = icon.getImage().getScaledInstance(60, 60, icon.getImage().SCALE_SMOOTH);
			this.turnIcon.setIcon(new ImageIcon(img));

		}
	}

	// Initializes the board
	public void initBoard() {
		ImageIcon icon;
		Image img;

		// Top panel
		this.topPanel = new Panel();
		this.topPanel.setBackground(Color.WHITE);
		add(topPanel, BorderLayout.NORTH);

		JLabel turnJLabel = new JLabel("Turn:");
		turnJLabel.setFont(new Font("Arial", Font.BOLD, 40));
		this.topPanel.add(turnJLabel);

		this.turnIcon = new JLabel();
		this.topPanel.add(turnIcon);

		// Main panel
		this.mainPanel = new Panel();
		this.gridLayout = new GridLayout(this.numOfRow, this.numOfColumn);
		add(mainPanel, BorderLayout.CENTER);
		this.mainPanel.setLayout(gridLayout);
		
		this.currentRowIndex = new int[this.numOfColumn];
		Arrays.fill(this.currentRowIndex, this.numOfRow - 1); // Initialize the array with the number of rows
		System.out.println(Arrays.toString(this.currentRowIndex));

		this.graphicsBoard = new Button[this.numOfRow][this.numOfColumn];
		this.logicalBoard = new int[this.numOfRow][this.numOfColumn];

		icon = new ImageIcon("images/free_cell.png");
		img = icon.getImage();
		
		
		//Fill the graphicsBoard with buttons.
		for (int row = 0; row < this.numOfRow; row++) {
			for (int column = 0; column < this.numOfColumn; column++) {

				this.graphicsBoard[row][column] = new Button(img);
				this.graphicsBoard[row][column].addActionListener(new AL(column));
				this.mainPanel.add(this.graphicsBoard[row][column]);
			}
		}

	}

	// Open dialog that show who win the game with options to reset or exit.
	public void winnerDialog(String winnerName) {
		ImageIcon icon = new ImageIcon("images/trophy.png");
		String[] options = { "Play again", "Exit" };

		int response = JOptionPane.showOptionDialog(null, winnerName + " WIN.", "Game Settings",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);

		switch (response) {
		case -1:
			System.out.println("Option Dialog Window Was Closed");
			System.exit(0);

		case 0:
			dispose();
			new Game(this.numOfRow, this.numOfColumn, this.sequence);
			break;
		case 1:
			System.exit(0);
			break;

		default:
			break;
		}
	}

	// Check if the board is full. False - exit, True - open dialog.
	public void isBoardFull() {

		for (int i = 0; i < this.numOfColumn; i++) {
			if (this.logicalBoard[0][i] == this.FREE_SPACE) {
				return;
			}
		}
		
		String[] options = { "Play again", "Exit" };
		int response = JOptionPane.showOptionDialog(null, "Tie", "The board is full", JOptionPane.WARNING_MESSAGE,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		switch (response) {
		case -1:
			System.out.println("Option Dialog Window Was Closed");
			System.exit(0);

		case 0:
			dispose();
			new Game(this.gameType);
			break;
		case 1:
			System.exit(0);
			break;

		default:
			break;
		}

	}

	// Right.
	public boolean checkRight(int row, int column, int player, String name) {

		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcColumn = column - testRange; funcColumn < this.numOfColumn; funcColumn++) {
			if (funcColumn >= 0) {
				if (this.logicalBoard[row][funcColumn] == player) {
					count++;
					if (count == this.sequence) {
						System.out.println(name + " WIN(Right)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		return false;
	}

	// Left.
	public boolean checkLeft(int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcColumn = column + testRange; funcColumn >= 0; funcColumn--) {
			if (funcColumn < this.numOfColumn) {

				if (this.logicalBoard[row][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Left)");
						return true;
					}

				} else {
					count = 0;
				}
			}
		}
		return false;
	}

	// Down.
	public boolean checkDown(int row, int column, int player, String name) {
		int count = 0;
		for (int funcRow = row; funcRow < this.numOfRow; funcRow++) {
			if (this.logicalBoard[funcRow][column] == player) {
				count++;

				if (count == this.sequence) {
					System.out.println(name + " WIN(Down)");
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	// (/) Up.
	public boolean checkSlashUp(int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcRow = row + testRange, funcColumn = column - testRange; funcRow >= 0
				&& funcColumn < this.numOfColumn; funcRow--, funcColumn++) {
			if (funcRow < this.numOfRow && funcColumn >= 0) {
				if (this.logicalBoard[funcRow][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Slash up)");
						return true;

					}
				} else {
					count = 0;
				}
			}
		}
		return false;
	}

	// (/) Down.
	public boolean checkSlashDown(int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcRow = row - testRange, funcColumn = column + testRange; funcRow < this.numOfRow
				&& funcColumn >= 0; funcRow++, funcColumn--) {

			if (funcRow >= 0 && funcColumn < this.numOfColumn) {
				if (this.logicalBoard[funcRow][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Slash down)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		return false;
	}

	// (\) Up.
	public boolean checkBackSlashUp(int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcRow = row + testRange, funcColumn = column + testRange; funcRow >= 0
				&& funcColumn >= 0; funcRow--, funcColumn--) {

			if (funcRow < this.numOfRow && funcColumn < this.numOfColumn) {
				if (this.logicalBoard[funcRow][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Back Slash up)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		return false;
	}

	// (\) Down.
	public boolean checkBackSlashDown(int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcRow = row - testRange, funcColumn = column - testRange; funcRow < this.numOfRow
				&& funcColumn < this.numOfColumn; funcRow++, funcColumn++) {
			if (funcRow >= 0 && funcColumn >= 0) {
				if (this.logicalBoard[funcRow][funcColumn] == player) {
					count++;

					if (count == this.sequence) {
						System.out.println(name + " WIN(Back Slash down)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		return false;
	}

	// Group all the functions to check who win.
	public boolean checkWinner(int column, int player, String name) {
		if (checkRight(currentRowIndex[column], column, player, name)
				|| checkLeft(currentRowIndex[column], column, player, name)
				|| checkDown(currentRowIndex[column], column, player, name)
				|| checkSlashUp(currentRowIndex[column], column, player, name)
				|| checkSlashDown(currentRowIndex[column], column, player, name)
				|| checkBackSlashUp(currentRowIndex[column], column, player, name)
				|| checkBackSlashDown(currentRowIndex[column], column, player, name)) {

			return true;
		}
		return false;

	}

	class AL implements ActionListener {
		private int col;

		public AL(int col) {
			this.col = col;
		}

		public void actionPerformed(ActionEvent e) {
			ImageIcon icon;
			Image img;
			if (gameType == HUMAN_VS_HUMAN) {

				if (turn == PLAYER_ONE && currentRowIndex[this.col] > -1) {
					icon = new ImageIcon("images/player_one.png");
					img = icon.getImage();

					logicalBoard[currentRowIndex[this.col]][this.col] = PLAYER_ONE;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(img);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(this.col, PLAYER_ONE, "Player one")) {

						winnerDialog("Player one");
						return;
					}
					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 3 - turn;
					changeTurnIcon(turn);

				} else if (turn == PLAYER_TWO && currentRowIndex[this.col] > -1) {

					icon = new ImageIcon("images/player_two.png");
					img = icon.getImage();

					logicalBoard[currentRowIndex[this.col]][this.col] = PLAYER_TWO;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(img);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(this.col, PLAYER_TWO, "Player two")) {

						winnerDialog("Player two");
						return;
					}

					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 3 - turn;
					changeTurnIcon(turn);
				}

			} else {
				turn = COMPUTER;
				if (turn == HUMAN && currentRowIndex[this.col] > -1) {
					icon = new ImageIcon("images/player_one.png");
					img = icon.getImage();

					logicalBoard[currentRowIndex[this.col]][this.col] = PLAYER_ONE;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(img);
					repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(this.col, PLAYER_ONE, "Player one")) {

						winnerDialog("Player one");
						return;
					}
					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 3 - turn;

				} else if (turn == COMPUTER && currentRowIndex[this.col] > -1) {

				}

			}
		}

	}

}
