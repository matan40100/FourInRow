import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

public class Game extends JFrame {
	// VERSION: 1.4
	// LAST EDIT: 17.01.20

	// Free space - 0, Player one(RED) - 1, Player two(BLUE) - 2, Computer - 3
	// HUMAN - 1, COMPUTER - 2
	private final int FREE_SPACE = 0;
	private final int PLAYER_ONE = 1;
	private final int PLAYER_TWO = 2;
	private final int COMPUTER = 3;
	private int turn = 1;

	private int numOfRow;
	private int numOfColumn;
	private int sequence;
	private Button[][] graphicsBoard;
	private int[][] logicalBoard;
	private int[] currentRowIndex;

	public Game() {
		this.numOfRow = 6;
		this.numOfColumn = 7;
		this.sequence = 4;
				
		setTitle("FourInRow");
		initBoard();
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

	}
	public Game(int numOfRow, int numOfCoulmn, int sequence) {
		this.numOfRow = numOfRow;
		this.numOfColumn = numOfCoulmn;
		this.sequence = sequence;
		
		setTitle("FourInRow");
		initBoard();
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

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
			new Game(this.numOfRow,this.numOfColumn,this.sequence);
			break;
		case 1:
			System.exit(0);
			break;

		default:
			break;
		}
	}

	public void createBorder() {
		for (int borderUpdDown = 0; borderUpdDown < this.numOfColumn + 2; borderUpdDown++) // Numbering the matrix row
		// and column
		{
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

	public void isBoardFull() {

		for (int i = 1; i < this.numOfColumn + 1; i++) {
			if (this.logicalBoard[1][i] == this.FREE_SPACE) {
				return;
			}
		}
		String[] options = { "Play again", "Exit" };

		int response = JOptionPane.showOptionDialog(null, "The board is full.", "Game Settings",
				JOptionPane.WARNING_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		switch (response) {
		case -1:
			System.out.println("Option Dialog Window Was Closed");
			System.exit(0);

		case 0:
			dispose();
			new Game();
			break;
		case 1:
			System.exit(0);
			break;

		default:
			break;
		}

	}

	// Right
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

	// Left
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

	// Down
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

	// (/) up
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

	// (/) down
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

	// (\) up
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

	// (\) down
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
			if (turn == 1) {
				icon = new ImageIcon("player_one_cell.jpg");
				img = icon.getImage();

				if (currentRowIndex[this.col] > 0) {
					logicalBoard[currentRowIndex[this.col]][this.col + 1] = PLAYER_ONE;
					graphicsBoard[currentRowIndex[this.col] - 1][this.col].setImage(img);
					repaint();
					

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkRight(currentRowIndex[this.col], this.col, PLAYER_ONE, "Player one")
							|| checkLeft(currentRowIndex[this.col], this.col, PLAYER_ONE, "Player one")
							|| checkDown(currentRowIndex[this.col], this.col, PLAYER_ONE, "Player one")
							|| checkSlashUp(currentRowIndex[this.col], this.col, PLAYER_ONE, "Player one")
							|| checkSlashDown(currentRowIndex[this.col], this.col, PLAYER_ONE, "Player one")
							|| checkBackSlashUp(currentRowIndex[this.col], this.col, PLAYER_ONE, "Player one")
							|| checkBackSlashDown(currentRowIndex[this.col], this.col, PLAYER_ONE, "Player one")) {

						winnerDialog("Player one");
						return;
					}

					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 3 - turn;
				}
			} else {
				icon = new ImageIcon("player_two_cell.jpg");
				img = icon.getImage();

				if (currentRowIndex[this.col] > 0) {
					logicalBoard[currentRowIndex[this.col]][this.col + 1] = PLAYER_TWO;
					graphicsBoard[currentRowIndex[this.col] - 1][this.col].setImage(img);
					repaint();
					

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkRight(currentRowIndex[this.col], this.col, PLAYER_TWO, "Player two")
							|| checkRight(currentRowIndex[this.col], this.col, PLAYER_TWO, "Player two")
							|| checkDown(currentRowIndex[this.col], this.col, PLAYER_TWO, "Player two")
							|| checkSlashUp(currentRowIndex[this.col], this.col, PLAYER_TWO, "Player two")
							|| checkSlashDown(currentRowIndex[this.col], this.col, PLAYER_TWO, "Player two")
							|| checkBackSlashUp(currentRowIndex[this.col], this.col, PLAYER_TWO, "Player two")
							|| checkBackSlashDown(currentRowIndex[this.col], this.col, PLAYER_TWO, "Player two")) {

						winnerDialog("Player two");
						return;
					}

					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 3 - turn;
				}
			}

		}

	}

}
