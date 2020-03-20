import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Game extends JFrame {
	// VERSION: 2.2
	// LAST EDIT: 20.03.20

	// Free space - 0, Player one(RED) - 1, Player two(BLUE) - 2, Computer - 3
	// HUMAN - 1, COMPUTER - 2
	private static final int HUMAN_VS_HUMAN = 1;
	private static final int HUMAN_VS_COMPUTER = 2;
	private static final int CUSTOM_GAME = 3;

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

	private File[] fileArray;

	private JPanel topPanel;
	private JPanel mainPanel;
	private GridLayout gridLayout;
	private JLabel turnIcon;
	private Timer timer;

	// Builder for normal game(Human VS. Human / Human VS. Computer).
	public Game(int gameType) {

		Random rand = new Random();

		this.numOfRow = 6;
		this.numOfColumn = 7;
		this.sequence = 4;

		if (gameType == HUMAN_VS_HUMAN) {
			this.turn = rand.nextInt(2 - 1 + 1) + 1;
			System.out.println("HUMAN_VS_HUMAN");
		} else if (gameType == HUMAN_VS_COMPUTER) {
			this.turn = rand.nextInt(4 - 3 + 1) + 3;
			System.out.println("HUMAN_VS_COMPUTER");
		}
		this.gameType = gameType;

		initBoard();
		setTitle("FourInRow");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		pack();

		changeTurnIcon(turn);
	}

	// Builder for custom game.
	public Game(int numOfRow, int numOfColumn, int sequence, int gameType) {

		Random rand = new Random();

		this.numOfRow = numOfRow;
		this.numOfColumn = numOfColumn;
		this.sequence = sequence;
		this.turn = rand.nextInt(2 - 1 + 1) + 1;
		this.gameType = gameType;

		initBoard();
		setTitle("FourInRow");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setVisible(true);

		changeTurnIcon(turn);
	}

	// Initializes the board
	public void initBoard() {
		arrayOfImageFiles();
		Image img = null;
		Font customFont = new Font("Arial", Font.PLAIN, 18);

		// Top panel
		this.topPanel = new JPanel();
		this.topPanel.setBackground(Color.WHITE);
		add(topPanel, BorderLayout.NORTH);
		this.topPanel.setLayout(new GridLayout(1, 3));

		// Create panel + reset button
		JPanel resetJPanel = new JPanel();

		JButton resetButton = new JButton("Reset");

		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameType == HUMAN_VS_HUMAN || gameType == HUMAN_VS_COMPUTER) {
					new Game(gameType);
				} else {
					new Game(numOfRow, numOfColumn, sequence, CUSTOM_GAME);
				}
				dispose();
			}
		});

		resetJPanel.setBackground(Color.WHITE);
		resetJPanel.setBorder(new TitledBorder(null, "Reset", TitledBorder.CENTER, TitledBorder.CENTER, customFont));
		resetJPanel.add(resetButton);
		this.topPanel.add(resetJPanel);

		// Create turn panel
		JPanel turnJPanel = new JPanel();
		turnJPanel.setBackground(Color.WHITE);
		turnJPanel.setBorder(new TitledBorder(null, "Turn:", TitledBorder.CENTER, TitledBorder.CENTER,
				new Font("Arial", Font.BOLD, 18)));
		this.turnIcon = new JLabel();
		turnJPanel.add(this.turnIcon);
		this.topPanel.add(turnJPanel);

		// Create Timer + Panel
		JPanel timerJPanel = new JPanel();
		timerJPanel.setBackground(Color.WHITE);
		timerJPanel.setBorder(new TitledBorder(null, "Timer", TitledBorder.CENTER, TitledBorder.CENTER, customFont));
		JLabel timerJLabel = new JLabel("0:00");
		timerJLabel.setFont(new Font("Arial", Font.BOLD, 30));
		timerJPanel.add(timerJLabel);
		this.timer = new Timer(1000, new ActionListener() {
			int secOne = 0;
			int secTwo = 0;
			int min = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				secOne++;
				if (secOne == 10) {

					secOne = 0;
					secTwo++;
				}

				if (secTwo == 6) {
					min++;
					secOne = 0;
					secTwo = 0;
				}
				timerJLabel.setText(min + ":" + secTwo + secOne);
			}
		});
		this.timer.start();
		this.topPanel.add(timerJPanel);

		// Main panel
		this.mainPanel = new JPanel();
		this.gridLayout = new GridLayout(this.numOfRow, this.numOfColumn);
		add(mainPanel, BorderLayout.CENTER);
		this.mainPanel.setLayout(gridLayout);

		this.currentRowIndex = new int[this.numOfColumn];
		Arrays.fill(this.currentRowIndex, this.numOfRow - 1); // Initialize the array with the number of rows
		this.graphicsBoard = new Button[this.numOfRow][this.numOfColumn];
		this.logicalBoard = new int[this.numOfRow][this.numOfColumn];

		try {
			img = ImageIO.read(fileArray[0]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);

		} catch (IOException e1) {

			e1.printStackTrace();
		}

		// Fill the graphicsBoard with buttons.
		for (int row = 0; row < this.numOfRow; row++) {
			for (int column = 0; column < this.numOfColumn; column++) {

				this.graphicsBoard[row][column] = new Button(img);
				this.graphicsBoard[row][column].setPreferredSize(new Dimension(90, 90));
				this.graphicsBoard[row][column].addActionListener(new AL(column));
				this.mainPanel.add(this.graphicsBoard[row][column]);
			}
		}

	}

	// Make array with player icons
	public void arrayOfImageFiles() {
		this.fileArray = new File[3];
		fileArray[0] = new File("Images/free_cell.png");
		fileArray[1] = new File("Images/player_one.png");
		fileArray[2] = new File("Images/player_two.png");
	}

	// Change the icon turn every turn.
	public void changeTurnIcon(int turn) {
		ImageIcon icon;
		Image img;
		if (turn == PLAYER_ONE || turn == COMPUTER) {
			icon = new ImageIcon("Images/player_one_icon.png");
			img = icon.getImage().getScaledInstance(60, 60, icon.getImage().SCALE_SMOOTH);
			this.turnIcon.setIcon(new ImageIcon(img));

		} else if (turn == PLAYER_TWO || turn == HUMAN) {
			icon = new ImageIcon("Images/player_two_icon.png");
			img = icon.getImage().getScaledInstance(60, 60, icon.getImage().SCALE_SMOOTH);
			this.turnIcon.setIcon(new ImageIcon(img));

		}
	}

	// Open dialog that show who win the game with options to reset or exit.
	public void winnerDialog(String winnerName) {
		ImageIcon icon = new ImageIcon("Images/trophy.png");
		String[] options = { "Play again", "Exit" };

		int response = JOptionPane.showOptionDialog(null, winnerName + " WIN.", "Game Settings",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);

		switch (response) {
			case -1:
				System.out.println("Option Dialog Window Was Closed");
				System.exit(0);

			case 0:
				dispose();
				if (gameType == HUMAN_VS_HUMAN || gameType == HUMAN_VS_COMPUTER) {
					new Game(this.gameType);
				} else {
					new Game(this.numOfRow, this.numOfColumn, this.sequence, CUSTOM_GAME);
				}
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
	public boolean checkRight(int[][] board, int row, int column, int player, String name) {

		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcColumn = column - testRange; funcColumn < this.numOfColumn; funcColumn++) {
			if (funcColumn >= 0) {
				if (board[row][funcColumn] == player) {
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
	public boolean checkLeft(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcColumn = column + testRange; funcColumn >= 0; funcColumn--) {
			if (funcColumn < this.numOfColumn) {

				if (board[row][funcColumn] == player) {
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
	public boolean checkDown(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		for (int funcRow = row; funcRow < this.numOfRow; funcRow++) {
			if (board[funcRow][column] == player) {
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
	public boolean checkSlashUp(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcRow = row + testRange, funcColumn = column - testRange; funcRow >= 0
				&& funcColumn < this.numOfColumn; funcRow--, funcColumn++) {
			if (funcRow < this.numOfRow && funcColumn >= 0) {
				if (board[funcRow][funcColumn] == player) {
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
	public boolean checkSlashDown(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcRow = row - testRange, funcColumn = column + testRange; funcRow < this.numOfRow
				&& funcColumn >= 0; funcRow++, funcColumn--) {

			if (funcRow >= 0 && funcColumn < this.numOfColumn) {
				if (board[funcRow][funcColumn] == player) {
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
	public boolean checkBackSlashUp(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcRow = row + testRange, funcColumn = column + testRange; funcRow >= 0
				&& funcColumn >= 0; funcRow--, funcColumn--) {

			if (funcRow < this.numOfRow && funcColumn < this.numOfColumn) {
				if (board[funcRow][funcColumn] == player) {
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
	public boolean checkBackSlashDown(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		for (int funcRow = row - testRange, funcColumn = column - testRange; funcRow < this.numOfRow
				&& funcColumn < this.numOfColumn; funcRow++, funcColumn++) {
			if (funcRow >= 0 && funcColumn >= 0) {
				if (board[funcRow][funcColumn] == player) {
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
	public boolean checkWinner(int[][] board, int column, int player, String name) {
		if (checkRight(board, currentRowIndex[column], column, player, name)
				|| checkLeft(board, currentRowIndex[column], column, player, name)
				|| checkDown(board, currentRowIndex[column], column, player, name)
				|| checkSlashUp(board, currentRowIndex[column], column, player, name)
				|| checkSlashDown(board, currentRowIndex[column], column, player, name)
				|| checkBackSlashUp(board, currentRowIndex[column], column, player, name)
				|| checkBackSlashDown(board, currentRowIndex[column], column, player, name)) {
			timer.stop();
			return true;

		}
		return false;

	}

	public int gradeRow() {
		int dupLogicalBoard[][] = this.logicalBoard.clone();
		int gradePlayer = 0, gradeComputer = 0;
		int countPlayer = 0, countComputer = 0, countFree = 0;
		int bestCol = 0, bestRow = 0;

		int[] gradeArray = new int[numOfColumn];
		int currentLevel = 0;
		for (int col = 0; col < this.numOfColumn; col++) {
			countComputer = 0;
			countFree = 0;

			dupLogicalBoard[this.currentRowIndex[col]][col] = COMPUTER;
			for (int i = 0; i < dupLogicalBoard.length; i++) {
				for (int j = 0; j < dupLogicalBoard[i].length; j++) {
					System.out.print(dupLogicalBoard[i][j] + " ");
				}
				System.out.println();
			}
			currentLevel = this.currentRowIndex[col];

			for (int secCol = col + 1; secCol <= col + 3 && secCol < this.numOfColumn; secCol++) {

				if (dupLogicalBoard[currentLevel][secCol] == HUMAN) {
					break;
				} else if (dupLogicalBoard[currentLevel][secCol] == FREE_SPACE) {
					countFree++;
				} else {
					countComputer++;
				}

			}
			gradeArray[col] = (int) Math.pow(10, countComputer);
			dupLogicalBoard[this.currentRowIndex[col]][col] = FREE_SPACE;
			System.out.println("Column: " + col);
			System.out.println("Computer: " + countComputer);
			System.out.println("Free space: " + countFree);
			System.out.println();

		}
		System.out.println(Arrays.toString(gradeArray));

		for (int i = 1; i < gradeArray.length; i++) {
			if (gradeArray[i] > gradeArray[bestCol])
				bestCol = i;
		}
		System.out.println(bestCol);
		return bestCol;
	}
	//Find if Computer has a potential column to win other check if the human can win and block
	public void findPotentialWinLose() {
		Image img = null;
		try {
			img = ImageIO.read(fileArray[1]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);

		} catch (IOException e1) {

			e1.printStackTrace();
		}
		int dupLogicalBoard[][] = this.logicalBoard.clone();
		for (int col = 0; col < this.numOfColumn; col++) {
			dupLogicalBoard[this.currentRowIndex[col]][col] = COMPUTER;
			if (checkWinner(dupLogicalBoard, col, COMPUTER, "Computer")) {
				System.out.println("test: " + col);
				logicalBoard[this.currentRowIndex[col]][col] = COMPUTER;
				graphicsBoard[currentRowIndex[col]][col].setImage(img);
				graphicsBoard[currentRowIndex[col]][col].repaint();
				winnerDialog("Computer");
				
			} else {
				dupLogicalBoard[this.currentRowIndex[col]][col] = HUMAN;
				if (checkWinner(dupLogicalBoard, col, HUMAN, "Human")) {
					logicalBoard[this.currentRowIndex[col]][col] = COMPUTER;
					graphicsBoard[currentRowIndex[col]][col].setImage(img);
					graphicsBoard[currentRowIndex[col]][col].repaint();
					
				}
			}
			dupLogicalBoard[this.currentRowIndex[col]][col] = FREE_SPACE;
		}

	}

	class AL implements ActionListener {
		private int col;

		public AL(int col) {
			this.col = col;
		}

		public void actionPerformed(ActionEvent e) {

			Image img = null;
			if (gameType == HUMAN_VS_HUMAN || gameType == CUSTOM_GAME) {

				if (turn == PLAYER_ONE && currentRowIndex[this.col] > -1) {

					try {
						img = ImageIO.read(fileArray[1]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);

					} catch (IOException e1) {

						e1.printStackTrace();
					}

					logicalBoard[currentRowIndex[this.col]][this.col] = PLAYER_ONE;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(img);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, PLAYER_ONE, "Player one")) {

						winnerDialog("Player one");
						return;
					}
					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 3 - turn;
					changeTurnIcon(turn);

				} else if (turn == PLAYER_TWO && currentRowIndex[this.col] > -1) {

					try {
						img = ImageIO.read(fileArray[2]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);

					} catch (IOException e1) {
						e1.printStackTrace();
					}

					logicalBoard[currentRowIndex[this.col]][this.col] = PLAYER_TWO;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(img);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, PLAYER_TWO, "Player two")) {

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
				// turn = HUMAN;
				if (turn == HUMAN && currentRowIndex[this.col] > -1) {
					try {
						img = ImageIO.read(fileArray[2]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);

					} catch (IOException e1) {

						e1.printStackTrace();
					}

					logicalBoard[currentRowIndex[this.col]][this.col] = HUMAN;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(img);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, HUMAN, "Human")) {

						winnerDialog("Human");
						return;
					}
					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 7 - turn;
					changeTurnIcon(turn);
					gradeRow();
					new Thread(new Runnable() {
					    public void run() {
					      try {
					          Thread.sleep(1500);
							  findPotentialWinLose();
							}
					      catch(InterruptedException ex) {}
					       }
					    }).start();	

				} else if (turn == COMPUTER && currentRowIndex[this.col] > -1) {
					try {
						img = ImageIO.read(fileArray[1]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);

					} catch (IOException e1) {

						e1.printStackTrace();
					}

					logicalBoard[currentRowIndex[this.col]][this.col] = COMPUTER;
					graphicsBoard[currentRowIndex[this.col]][this.col].setImage(img);
					graphicsBoard[currentRowIndex[this.col]][this.col].repaint();

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, HUMAN, "Computer")) {

						winnerDialog("Computer");
						return;
					}
					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					turn = 7 - turn;
					changeTurnIcon(turn);

				}

			}
		}

	}

}
