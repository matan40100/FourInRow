import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

import javax.swing.*;

public class Game extends JFrame {
	// VERSION: 1.6
	// LAST EDIT: 22.01.20

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

	private int infinity = 100;

	private int numOfRow;
	private int numOfColumn;
	private int sequence;
	private Button[][] graphicsBoard;
	private int[][] logicalBoard;
	private int[] currentRowIndex;

	// Builder for normal game(Human VS. Human / Human VS. Computer)
	public Game(int gameType) {

		Random rand = new Random();

		this.numOfRow = 6;
		this.numOfColumn = 7;
		this.sequence = 4;
		this.turn = rand.nextInt(2 - 1 + 1) + 1;
		this.gameType = gameType;

		setTitle("FourInRow");
		initBoard();
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	// Builder for custom game.
	public Game(int numOfRow, int numOfCoulmn, int sequence) {

		Random rand = new Random();

		this.numOfRow = numOfRow;
		this.numOfColumn = numOfCoulmn;
		this.sequence = sequence;
		this.turn = rand.nextInt(2 - 1 + 1) + 1;

		setTitle("FourInRow");
		initBoard();
		setSize(numOfRow * 83, numOfCoulmn * 71);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

	}

	// Initializes the board
	public void initBoard() {
		ImageIcon icon = new ImageIcon("free_cell.jpg"); // Icon of free cell
		Image img = icon.getImage();

		this.currentRowIndex = new int[this.numOfColumn];
		Arrays.fill(this.currentRowIndex, this.numOfRow); // Initialize the array with the number of rows
		System.out.println(Arrays.toString(this.currentRowIndex));

		this.graphicsBoard = new Button[this.numOfRow][this.numOfColumn];
		this.logicalBoard = new int[this.numOfRow + 2][this.numOfColumn + 2];

		setLayout(new GridLayout(this.numOfRow, this.numOfColumn));

		for (int row = 0; row < this.numOfRow; row++) {
			for (int column = 0; column < this.numOfColumn; column++) {

				this.graphicsBoard[row][column] = new Button(img);
				this.graphicsBoard[row][column].addActionListener(new AL(row, column));

				add(this.graphicsBoard[row][column]);
			}
		}

		createBorder();

	}

	// Open dialog that show who win the game with options to reset or exit.
	public void winnerDialog(String winnerName) {
		ImageIcon icon = new ImageIcon("trophy.png");
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

	// Numbering the matrix row and column
	public void createBorder() {
		for (int borderUpdDown = 0; borderUpdDown < this.numOfColumn + 2; borderUpdDown++) {
			logicalBoard[0][borderUpdDown] = 9;
			logicalBoard[this.numOfRow + 1][borderUpdDown] = 9;

		}

		for (int borderRightLeft = 0; borderRightLeft < this.numOfRow + 2; borderRightLeft++) // Numbering the matrix
		// row and column
		{
			logicalBoard[borderRightLeft][0] = 9;
			logicalBoard[borderRightLeft][this.numOfColumn + 1] = 9;

		}
	}

	// Check if the board is full. False - return, True - Open dialog with options
	// to reset / exit.
	// Check if the board is full. False - exit, True - open dialog.
	public void isBoardFull() {

		for (int i = 1; i < this.numOfColumn + 1; i++) {
			if (this.logicalBoard[1][i] == this.FREE_SPACE) {
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
		for (int funcColumn = column; funcColumn < this.numOfColumn + 2; funcColumn++) {

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
		return false;
	}

	// Left.
	public boolean checkLeft(int row, int column, int player, String name) {
		int count = 0;

		for (int funcColumn = column + 2; funcColumn > 0; funcColumn--) {

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
		return false;
	}

	// Down.
	public boolean checkDown(int row, int column, int player, String name) {
		int count = 0;
		for (int funcRow = row; this.logicalBoard[funcRow][column + 1] == player; funcRow++) {
			count++;
			if (count == this.sequence) {
				System.out.println(name + " WIN(Down)");
				return true;
			}

		}
		return false;
	}

	// (/) Up.
	public boolean checkSlashUp(int row, int column, int player, String name) {
		int count = 0;
		for (int funcRow = row + 1, funcColumn = column; funcRow > 0
				&& funcColumn < this.numOfColumn + 1; funcRow--, funcColumn++) {

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
		return false;
	}

	// (/) Down.
	public boolean checkSlashDown(int row, int column, int player, String name) {
		int count = 0;
		for (int funcRow = row - 1, funcColumn = column + 2; funcRow < this.numOfRow + 1
				&& funcColumn > 0; funcRow++, funcColumn--) {

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
		return false;
	}

	// (\) Up.
	public boolean checkBackSlashUp(int row, int column, int player, String name) {
		int count = 0;

		for (int funcRow = row + 1, funcColumn = column + 2; funcRow > 0 && funcColumn > 0; funcRow--, funcColumn--) {

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
		return false;
	}

	// (\) Down.
	public boolean checkBackSlashDown(int row, int column, int player, String name) {
		int count = 0;

		for (int funcRow = row - 1, funcColumn = column; funcRow < this.numOfRow + 2
				&& funcColumn < this.numOfColumn + 2; funcRow++, funcColumn++) {
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
		return false;
	}

	// Group all the functions to check who win.
	public boolean checkWinner(int column, int player, String name) {
		if (checkRight(currentRowIndex[column], column, player, name)
				|| checkRight(currentRowIndex[column], column, player, name)
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
		private int row;
		private int col;

		public AL(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public void actionPerformed(ActionEvent e) {
			ImageIcon icon;
			Image img;
			if (gameType == HUMAN_VS_HUMAN) {

				if (turn == PLAYER_ONE && currentRowIndex[this.col] > 0) {
					icon = new ImageIcon("player_one_cell.jpg");
					img = icon.getImage();

					logicalBoard[currentRowIndex[this.col]][this.col + 1] = PLAYER_ONE;
					graphicsBoard[currentRowIndex[this.col] - 1][this.col].setImage(img);
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

				} else if (turn == PLAYER_TWO && currentRowIndex[this.col] > 0) {
					icon = new ImageIcon("player_two_cell.jpg");
					img = icon.getImage();

					logicalBoard[currentRowIndex[this.col]][this.col + 1] = PLAYER_TWO;
					graphicsBoard[currentRowIndex[this.col] - 1][this.col].setImage(img);
					repaint();

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
				}

			} else {
				turn = COMPUTER;
				if (turn == HUMAN && currentRowIndex[this.col] > 0) {
					icon = new ImageIcon("player_one_cell.jpg");
					img = icon.getImage();

					logicalBoard[currentRowIndex[this.col]][this.col + 1] = HUMAN;
					graphicsBoard[currentRowIndex[this.col] - 1][this.col].setImage(img);
					repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(this.col, HUMAN, "Human")) {

						winnerDialog("Human");
						return;
					}

					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 3 - turn;
				} else if (turn == COMPUTER && currentRowIndex[this.col] > 0) {

				}

			}
		}

	}

}
