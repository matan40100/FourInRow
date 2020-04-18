import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;
import java.lang.Math;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class Game extends JFrame {
	// VERSION: 3.0
	// LAST EDIT: 18.4.20

	// Free space - 0, Player One(RED) - 1, Player Two(BLUE) - 2, Computer - 3
	// HUMAN - 1, COMPUTER - 2
	private final int HUMAN_VS_HUMAN = 0;
	private final int HUMAN_VS_COMPUTER = 1;

	private final int FREE_SPACE = 0;
	private final int PLAYER_ONE = 1;
	private final int PLAYER_TWO = 2;
	private final int COMPUTER = 3;
	private final int HUMAN = 4;

	private static final int MINMAX = 0;
	private static final int ALPHA_BETA = 1;

	private int level;
	private int algorithm;
	private int gameType;
	private int turn;
	private int startPlayer;

	private int numOfRow;
	private int numOfColumn;
	private int sequence;
	private Button[][] graphicsBoard;
	private int[][] logicalBoard;
	private int[] currentRowIndex;

	private File[] fileArray;

	private Image imgRed;
	private Image iconRed;
	private Image imgBlue;
	private Image iconBlue;
	private Image redTrophy;
	private Image blueTrophy;
	private Image imgFreeSpace;

	private JPanel topPanel;
	private JPanel mainPanel;
	private GridLayout gridLayout;
	private JLabel turnIcon;
	private Timer timer;
	Queue<Integer> winnerSequenceIndex;

	private int count = 0;
	private int bestCol;

	// Builder for game.
	public Game(int numOfRow, int numOfColumn, int sequence, int gameType, int level, int algorithm) {

		Random rand = new Random();
		this.winnerSequenceIndex = new LinkedList<>();

		this.numOfRow = numOfRow;
		this.numOfColumn = numOfColumn;
		this.sequence = sequence;
		this.turn = rand.nextInt(2 - 1 + 1) + 1;
		this.startPlayer = turn;
		this.gameType = gameType;
		this.level = level;
		this.algorithm = algorithm;
		System.out.println("Game Settings:");
		System.out.println("Rows: " + this.numOfRow);
		System.out.println("Columns: " + this.numOfColumn);
		System.out.println("Sequence: " + this.sequence);
		System.out.println("Game Type: " + this.gameType);
		System.out.println("Difficult Level: " + this.level);
		System.out.println("Algorithm:" + (this.algorithm == MINMAX ? "MinMax" : "Alpha-Beta"));

		createTopPanel();
		createButtomPanel();
		initBoard();
		setTitle("N In Row");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setVisible(true);
		changeTurnIcon(turn);
		if (gameType == HUMAN_VS_COMPUTER) {
			startPlayer = rand.nextBoolean() ? COMPUTER : HUMAN;
			turn = startPlayer;
			changeTurnIcon(turn);
			if (startPlayer == COMPUTER) {
				computerMove();
			}
		}
	}

	// Create the Top Panel - Buttons,Show turn,Timer, Game Settings
	public void createTopPanel() {
		this.topPanel = new JPanel(new BorderLayout());
		this.topPanel.setBackground(Color.WHITE);
		add(topPanel, BorderLayout.NORTH);

		JPanel gameInfoPanel = new JPanel(new GridLayout(1, 3));

		topPanel.add(gameInfoPanel, BorderLayout.NORTH);

		// Create Options panel - Menu / reset
		JPanel optionsJPanel = new JPanel();
		optionsJPanel.setBorder(new TitledBorder(null, "Options", TitledBorder.CENTER, TitledBorder.CENTER,
				new Font("Arial", Font.PLAIN, 18)));

		optionsJPanel.setBackground(Color.WHITE);
		// Menu button
		JButton menuButton = new JButton("Menu");
		menuButton.addActionListener(e -> {
			Main.main(null);
			dispose();
		});
		optionsJPanel.add(menuButton);
		// Menu rest button
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(e -> {
			new Game(numOfRow, numOfColumn, sequence, gameType, level, algorithm);
			dispose();
		});
		optionsJPanel.add(resetButton);

		gameInfoPanel.add(optionsJPanel);

		// Create turn panel
		JPanel turnJPanel = new JPanel();
		turnJPanel.setBackground(Color.WHITE);
		turnJPanel.setBorder(new TitledBorder(null, "Turn:", TitledBorder.CENTER, TitledBorder.CENTER,
				new Font("Arial", Font.BOLD, 18)));

		this.turnIcon = new JLabel();
		this.turnIcon.setHorizontalTextPosition(JLabel.CENTER);
		this.turnIcon.setVerticalTextPosition(JLabel.BOTTOM);
		this.turnIcon.setFont(new Font("Arial", Font.BOLD, 14));

		turnJPanel.add(this.turnIcon);
		gameInfoPanel.add(turnJPanel);

		// Create Timer panel
		JPanel timerJPanel = new JPanel();
		timerJPanel.setBackground(Color.WHITE);
		timerJPanel.setBorder(new TitledBorder(null, "Timer", TitledBorder.CENTER, TitledBorder.CENTER,
				new Font("Arial", Font.PLAIN, 18)));

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
		gameInfoPanel.add(timerJPanel);

		// Game Settings panel
		JPanel gameSettingPanel = new JPanel(new GridLayout(1, 5));
		gameSettingPanel.add(new CustomJlabel("Row", this.numOfRow, Color.BLACK, Color.WHITE, true,
				new Font("Arial", Font.PLAIN, 18)));
		gameSettingPanel.add(new CustomJlabel("Column", this.numOfColumn, Color.BLACK, Color.WHITE, true,
				new Font("Arial", Font.PLAIN, 18)));
		gameSettingPanel.add(new CustomJlabel("Sequence", this.sequence, Color.BLACK, Color.WHITE, true,
				new Font("Arial", Font.PLAIN, 18)));
		gameSettingPanel.add(
				new CustomJlabel("Mode", gameType, Color.BLACK, Color.WHITE, true, new Font("Arial", Font.PLAIN, 18)));
		gameSettingPanel.add(
				new CustomJlabel("Level", level, Color.BLACK, Color.WHITE, true, new Font("Arial", Font.PLAIN, 18)));

		topPanel.add(gameSettingPanel, BorderLayout.CENTER);
	}

	public void createButtomPanel() {
		CustomJlabel numberOfColumn;
		JPanel butoomJPanel = new JPanel(new GridLayout(1, this.numOfColumn));
		add(butoomJPanel, BorderLayout.SOUTH);
		for (int i = 0; i < this.numOfColumn; i++) {
			numberOfColumn = new CustomJlabel(null, i + 1, Color.WHITE, new Color(63, 63, 63), false,
					new Font("Arial", Font.BOLD, 24));
			numberOfColumn.setBorder(new EmptyBorder(4, 0, 4, 0));
			butoomJPanel.add(numberOfColumn);
		}
	}

	// Initializes the board
	public void initBoard() {
		try {
			createImages();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.mainPanel = new JPanel();
		this.gridLayout = new GridLayout(this.numOfRow, this.numOfColumn);
		this.mainPanel.setLayout(gridLayout);
		add(mainPanel, BorderLayout.CENTER);

		this.currentRowIndex = new int[this.numOfColumn];
		Arrays.fill(this.currentRowIndex, this.numOfRow - 1); // Initialize the array with the number of rows
		this.graphicsBoard = new Button[this.numOfRow][this.numOfColumn];
		this.logicalBoard = new int[this.numOfRow][this.numOfColumn];

		// Fill the graphicsBoard with buttons and add action listener.
		for (int row = 0; row < this.numOfRow; row++) {
			for (int column = 0; column < this.numOfColumn; column++) {
				this.graphicsBoard[row][column] = new Button(imgFreeSpace);
				this.graphicsBoard[row][column].setPreferredSize(new Dimension(90, 90));
				this.graphicsBoard[row][column].addActionListener(new AL(column));
				this.mainPanel.add(this.graphicsBoard[row][column]);
			}
		}
	}

	// Make array with player icons
	public void createImages() throws IOException {
		this.fileArray = new File[7];
		fileArray[0] = new File("Images/free_cell.png");
		fileArray[1] = new File("Images/player_one.png");
		fileArray[2] = new File("Images/player_two.png");
		fileArray[3] = new File("Images/player_one_icon.png");
		fileArray[4] = new File("Images/player_two_icon.png");
		fileArray[5] = new File("Images/red_trophy.png");
		fileArray[6] = new File("Images/blue_trophy.png");
		this.imgFreeSpace = ImageIO.read(fileArray[0]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		this.imgRed = ImageIO.read(fileArray[1]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		this.imgBlue = ImageIO.read(fileArray[2]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		this.iconRed = ImageIO.read(fileArray[3]).getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		this.iconBlue = ImageIO.read(fileArray[4]).getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		this.redTrophy = ImageIO.read(fileArray[5]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		this.blueTrophy = ImageIO.read(fileArray[6]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);

	}

	// Return a duplicate of matrix
	public int[][] dupBoard(int[][] board) {
		int dup[][] = new int[this.numOfRow][this.numOfColumn];
		for (int row = 0; row < this.numOfRow; row++) {
			for (int col = 0; col < this.numOfColumn; col++) {
				dup[row][col] = board[row][col];
			}
		}
		return dup;
	}

	// Change the icon turn every turn.
	public void changeTurnIcon(int turn) {
		if (turn == PLAYER_ONE || turn == COMPUTER) {
			this.turnIcon.setIcon(new ImageIcon(iconRed));
			this.turnIcon.setText((gameType == HUMAN_VS_HUMAN ? "Player One" : "Computer"));
		} else if (turn == PLAYER_TWO || turn == HUMAN) {
			this.turnIcon.setIcon(new ImageIcon(iconBlue));
			this.turnIcon.setText((gameType == HUMAN_VS_HUMAN ? "Player Two" : "Human"));
		}
	}

	// Open dialog that show who win the game with options to reset or exit.
	public void winnerDialog(String winnerName) {
		ImageIcon icon = new ImageIcon("Images/trophy.png");
		String[] options = { "Play again", "Exit" };

		if (winnerName.equals("Player One") || winnerName.equals("Computer")) {
			UIManager.put("OptionPane.messageForeground", Color.RED);
			UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
		} else {
			UIManager.put("OptionPane.messageForeground", Color.BLUE);
			UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
		}

		int response = JOptionPane.showOptionDialog(null, winnerName + " WIN.", "Game Settings",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);

		switch (response) {
			case -1:
				System.out.println("Option Dialog Window Was Closed");
				System.exit(0);
			case 0:
				dispose();
				new Game(numOfRow, numOfColumn, sequence, gameType, level, algorithm);
				break;
			case 1:
				System.exit(0);
				break;
			default:
				break;
		}
	}

	// Change the image to show the sequence win
	public void showWinnerSequence(Image tropyImage) {
		System.out.println(winnerSequenceIndex);
		int row, column;
		while (!this.winnerSequenceIndex.isEmpty()) {
			row = winnerSequenceIndex.remove();
			column = winnerSequenceIndex.remove();
			graphicsBoard[row][column].setImage(tropyImage);
			graphicsBoard[row][column].repaint();
		}

	}

	// Check if the board is full. False exit, True - open dialog.
	public void isBoardFull() {
		for (int i = 0; i < this.numOfColumn; i++) {
			if (this.logicalBoard[0][i] == FREE_SPACE) {
				return;
			}
		}
		UIManager.put("OptionPane.messageForeground", new Color(255, 204, 0));

		String[] options = { "Play again", "Exit" };
		int response = JOptionPane.showOptionDialog(null, "Tie", "The board is full", JOptionPane.WARNING_MESSAGE,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		switch (response) {
			case -1:
				System.out.println("Option Dialog Window Was Closed");
				System.exit(0);

			case 0:
				dispose();
				new Game(numOfRow, numOfColumn, sequence, gameType, level, algorithm);
				break;
			case 1:
				System.exit(0);
				break;

			default:
				break;
		}
	}

	// Update board(GUI & Logical)
	public void updateBoard(int column, int player, int gameType, Image image) {
		logicalBoard[currentRowIndex[column]][column] = player;
		graphicsBoard[currentRowIndex[column]][column].setImage(image);
		graphicsBoard[currentRowIndex[column]][column].repaint();
		if (gameType == HUMAN_VS_COMPUTER) {
			turn = 7 - turn;
		} else {
			turn = 3 - turn;
		}
		changeTurnIcon(turn);
	}

	public boolean checkRow(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;
		// Right.
		for (int funcColumn = column - testRange; funcColumn < this.numOfColumn; funcColumn++) {
			if (funcColumn >= 0) {
				if (board[row][funcColumn] == player) {
					count++;
					this.winnerSequenceIndex.add(row);
					this.winnerSequenceIndex.add(funcColumn);
					if (count == this.sequence) {
						System.out.println(name + " WIN(Right)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}

		// Left.
		count = 0;
		this.winnerSequenceIndex.clear();
		for (int funcColumn = column + testRange; funcColumn >= 0; funcColumn--) {
			if (funcColumn < this.numOfColumn) {

				if (board[row][funcColumn] == player) {
					count++;
					this.winnerSequenceIndex.add(row);
					this.winnerSequenceIndex.add(funcColumn);
					if (count == this.sequence) {
						System.out.println(name + " WIN(Left)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		this.winnerSequenceIndex.clear();
		return false;
	}

	public boolean checkColumn(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		for (int funcRow = row; funcRow < this.numOfRow; funcRow++) {
			if (board[funcRow][column] == player) {
				count++;
				this.winnerSequenceIndex.add(funcRow);
				this.winnerSequenceIndex.add(column);
				if (count == this.sequence) {
					System.out.println(name + " WIN(Down)");
					return true;
				}
			} else {
				return false;
			}
		}
		this.winnerSequenceIndex.clear();
		return false;
	}

	public boolean checkDiagonal(int[][] board, int row, int column, int player, String name) {

		int count = 0;
		int testRange = this.sequence - 3;

		// Up.
		for (int funcRow = row + testRange, funcColumn = column - testRange; funcRow >= 0
				&& funcColumn < this.numOfColumn; funcRow--, funcColumn++) {
			if (funcRow < this.numOfRow && funcColumn >= 0) {
				if (board[funcRow][funcColumn] == player) {
					count++;
					this.winnerSequenceIndex.add(funcRow);
					this.winnerSequenceIndex.add(funcColumn);

					if (count == this.sequence) {
						System.out.println(name + " WIN(Slash up)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}

		// Down.
		count = 0;
		this.winnerSequenceIndex.clear();
		for (int funcRow = row - testRange, funcColumn = column + testRange; funcRow < this.numOfRow
				&& funcColumn >= 0; funcRow++, funcColumn--) {

			if (funcRow >= 0 && funcColumn < this.numOfColumn) {
				if (board[funcRow][funcColumn] == player) {
					count++;
					this.winnerSequenceIndex.add(funcRow);
					this.winnerSequenceIndex.add(funcColumn);
					if (count == this.sequence) {
						System.out.println(name + " WIN(Slash down)");
						return true;
					}
				} else {
					count = 0;

				}
			}
		}
		this.winnerSequenceIndex.clear();
		return false;
	}

	public boolean checkBackDiagonal(int[][] board, int row, int column, int player, String name) {
		int count = 0;
		int testRange = this.sequence - 3;

		// Up.
		for (int funcRow = row + testRange, funcColumn = column + testRange; funcRow >= 0
				&& funcColumn >= 0; funcRow--, funcColumn--) {

			if (funcRow < this.numOfRow && funcColumn < this.numOfColumn) {
				if (board[funcRow][funcColumn] == player) {
					count++;
					this.winnerSequenceIndex.add(funcRow);
					this.winnerSequenceIndex.add(funcColumn);
					if (count == this.sequence) {
						System.out.println(name + " WIN(Back Slash up)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}

		// Down.
		count = 0;
		this.winnerSequenceIndex.clear();
		for (int funcRow = row - testRange, funcColumn = column - testRange; funcRow < this.numOfRow
				&& funcColumn < this.numOfColumn; funcRow++, funcColumn++) {
			if (funcRow >= 0 && funcColumn >= 0) {
				if (board[funcRow][funcColumn] == player) {
					count++;
					this.winnerSequenceIndex.add(funcRow);
					this.winnerSequenceIndex.add(funcColumn);
					if (count == this.sequence) {
						System.out.println(name + " WIN(Back Slash down)");
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		this.winnerSequenceIndex.clear();
		return false;
	}

	// Group all the functions to check who win.
	public boolean checkWinner(int[][] board, int column, int player, String name) {
		if (checkRow(board, currentRowIndex[column], column, player, name)
				|| checkColumn(board, currentRowIndex[column], column, player, name)
				|| checkDiagonal(board, currentRowIndex[column], column, player, name)
				|| checkBackDiagonal(board, currentRowIndex[column], column, player, name)) {
			timer.stop();
			return true;
		}
		return false;
	}

	public int gradeRow(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		// Right.
		for (i = 0; i < this.sequence && column + i < this.numOfColumn; i++) {
			if (this.logicalBoard[row][column + i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row][column + i] == HUMAN) {
				countHuman++;
			}

		}

		if (i == this.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column + this.sequence < this.numOfColumn && this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row][column + (this.sequence - 1)] != HUMAN
				&& this.logicalBoard[row][column + this.sequence] != HUMAN && countHuman == (this.sequence - 2)) {
			gradeHuman += 900;
		}

		// Left
		countComputer = countHuman = 0;
		for (i = 0; i < this.sequence && column - i > -1; i++) {
			if (this.logicalBoard[row][column - i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row][column - i] == HUMAN) {
				countHuman++;
			}
		}

		if (i == this.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column - this.sequence > -1 && this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row][column - (this.sequence - 1)] != HUMAN
				&& this.logicalBoard[row][column - this.sequence] != HUMAN && countHuman == (this.sequence - 2)) {
			gradeHuman += 900;
		}

		return gradeComputer - gradeHuman;
	}

	public int gradeColumn(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		for (i = 0; i < this.sequence && row + i < this.numOfRow; i++) {
			if (this.logicalBoard[row + i][column] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row + i][column] == HUMAN) {
				countHuman++;
			}
		}

		if (i == this.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		return gradeComputer - gradeHuman;
	}

	public int gradeDiagonal(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		// Up.
		for (i = 0; i < this.sequence && column + i < this.numOfColumn && row - i > -1; i++) {

			if (this.logicalBoard[row - i][column + i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row - i][column + i] == HUMAN) {
				countHuman++;
			}
		}
		if (i == this.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column + this.sequence < this.numOfColumn && row - this.sequence > -1
				&& this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row - (this.sequence - 1)][column + (this.sequence - 1)] != HUMAN
				&& this.logicalBoard[row - this.sequence][column + this.sequence] != HUMAN
				&& countHuman == (this.sequence - 2)) {
			gradeHuman += 900;
		}

		// Down.
		countComputer = countHuman = 0;
		for (i = 0; i < this.sequence && column - i > -1 && row + i < this.numOfRow; i++) {
			if (this.logicalBoard[row + i][column - i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row + i][column - i] == HUMAN) {
				countHuman++;
			}
		}

		if (i == this.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column - this.sequence > -1 && row + this.sequence < this.numOfRow
				&& this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row + (this.sequence - 1)][column - (this.sequence - 1)] != HUMAN
				&& this.logicalBoard[row + this.sequence][column - this.sequence] != HUMAN
				&& countHuman == (this.sequence - 2)) {
			gradeHuman += 900;
		}
		return gradeComputer - gradeHuman;
	}

	public int gradeBackDiagonal(int row, int column) {
		int gradeHuman = 0, gradeComputer = 0;
		int countHuman = 0, countComputer = 0;
		int i = 0;

		// Up.
		for (i = 0; i < this.sequence && column - i > -1 && row - i > -1; i++) {
			if (this.logicalBoard[row - i][column - i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row - i][column - i] == HUMAN) {
				countHuman++;
			}
		}

		if (i == this.sequence) {
			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}

		}
		// #1-Smart if
		if (column - this.sequence > -1 && row - this.sequence > -1 && this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row - (this.sequence - 1)][column - (this.sequence - 1)] != HUMAN
				&& this.logicalBoard[row - this.sequence][column - this.sequence] != HUMAN
				&& countHuman == (this.sequence - 2)) {
			gradeHuman += 900;
		}

		// Down.
		countComputer = countHuman = 0;
		for (i = 0; i < this.sequence && column + i < this.numOfColumn && row + i < this.numOfRow; i++) {

			if (this.logicalBoard[row + i][column + i] == COMPUTER) {
				countComputer++;
			} else if (this.logicalBoard[row + i][column + i] == HUMAN) {
				countHuman++;
			}
		}
		if (i == this.sequence) {

			if (countHuman > 0 && countComputer == 0) {
				gradeHuman += (int) Math.pow(10, countHuman);
			}

			if (countComputer > 0 && countHuman == 0) {
				gradeComputer += (int) Math.pow(10, countComputer);
			}
		}

		// #1-Smart if
		if (column + this.sequence < this.numOfColumn && row + this.sequence < this.numOfRow
				&& this.logicalBoard[row][column] == FREE_SPACE
				&& this.logicalBoard[row + (this.sequence - 1)][column + (this.sequence - 1)] != HUMAN
				&& this.logicalBoard[row + this.sequence][column + this.sequence] != HUMAN
				&& countHuman == (this.sequence - 2)) {
			gradeHuman += 900;
		}
		return gradeComputer - gradeHuman;
	}

	// Give grade to the whole board by checking every space
	public int gradeBoard() {
		int grade = 0;
		for (int row = 0; row < this.numOfRow; row++) {
			for (int column = 0; column < this.numOfColumn; column++) {
				grade += gradeRow(row, column);
				grade += gradeColumn(row, column);
				grade += gradeDiagonal(row, column);
				grade += gradeBackDiagonal(row, column);
			}
		}
		return grade;
	}

	// Find if Computer has a potential to win, if true - win
	public boolean findPotentialWin(int row, int column) {
		if (row == -1) {
			return false;
		}
		int dupLogicalBoard[][] = dupBoard(this.logicalBoard);

		dupLogicalBoard[row][column] = COMPUTER;
		return (checkWinner(dupLogicalBoard, column, COMPUTER, "Computer"));
	}

	// Find if Human has a potential to win, if true - block
	public boolean findPotentialLose(int row, int column) {
		if (row == -1) {
			return false;
		}
		int dupLogicalBoard[][] = dupBoard(this.logicalBoard);

		dupLogicalBoard[row][column] = HUMAN;
		return (checkWinner(dupLogicalBoard, column, HUMAN, "Human"));
	}

	public int recCheckRow(int row, int column, int startColumn) {
		if ((column - startColumn == this.sequence) || (row > this.numOfRow - 1 || column > this.numOfColumn - 1)
				|| (this.logicalBoard[row][column] == HUMAN)) {
			return 0;
		}

		for (int i = this.sequence - 1; i > 0; i--) {
			if (column - i < 0) {
				return 0 + recCheckRow(row, column + 1, startColumn);
			}
			if (this.logicalBoard[row][column - i] == HUMAN) {
				return 0 + recCheckRow(row, column + 1, startColumn);
			}
		}
		return 1 + recCheckRow(row, column + 1, startColumn);
	}

	public int recCheckCol(int row, int column, int startRow) {
		if ((row - startRow == this.sequence) || (row > this.numOfRow - 1 || column > this.numOfColumn - 1)
				|| (this.logicalBoard[row][column] == HUMAN)) {
			return 0;
		}

		for (int i = this.sequence - 1; i > 0; i--) {
			if (row - i < 0) {
				return 0 + recCheckCol(row + 1, column, startRow);
			}
			if (this.logicalBoard[row - i][column] == HUMAN) {
				return 0 + recCheckCol(row + 1, column, startRow);
			}
		}
		return 1 + recCheckCol(row + 1, column, startRow);
	}

	public int recCheckDiagonal(int row, int column, int startRow, int startColumn) {
		if ((column - startColumn == this.sequence) || (row > this.numOfRow - 1 || column > this.numOfColumn - 1)
				|| row < 0 || (this.logicalBoard[row][column] == HUMAN)) {
			return 0;
		}

		for (int i = this.sequence - 1; i > 0; i--) {

			if (column - i < 0 || row + i > this.numOfRow - 1) {
				return 0 + recCheckDiagonal(row - 1, column + 1, startRow, startColumn);
			}
			if (this.logicalBoard[row + i][column - i] == HUMAN) {
				return 0 + recCheckDiagonal(row - 1, column + 1, startRow, startColumn);
			}
		}
		return 1 + recCheckDiagonal(row - 1, column + 1, startRow, startColumn);
	}

	public int recCheckBackDiagonal(int row, int column, int startRow, int startColumn) {
		if ((column - startColumn == this.sequence) || (row > this.numOfRow - 1 || column > this.numOfColumn - 1)
				|| row < 0 || (this.logicalBoard[row][column] == HUMAN)) {
			return 0;
		}

		for (int i = this.sequence - 1; i > 0; i--) {

			if (column - i < 0 || row - i < 0) {
				return 0 + recCheckBackDiagonal(row + 1, column + 1, startRow, startColumn);
			}
			if (this.logicalBoard[row - i][column - i] == HUMAN) {
				return 0 + recCheckBackDiagonal(row + 1, column + 1, startRow, startColumn);
			}
		}
		return 1 + recCheckBackDiagonal(row + 1, column + 1, startRow, startColumn);
	}

	// Group the function -
	// recCheckRow,recCheckCol,recCheckDiagonal,recCheckBackDiagonal
	public int findPotentialSpace() {
		int bestValue = 0;
		int bestIndex = 0;
		int[][] potentialSpace = new int[numOfRow][numOfColumn];
		for (int row = 0; row < numOfRow; row++) {
			for (int column = 0; column < numOfColumn; column++) {

				if (logicalBoard[row][column] == COMPUTER || logicalBoard[row][column] == HUMAN) {
					potentialSpace[row][column] = 0;
					continue;
				}

				potentialSpace[row][column] += recCheckRow(row, column, column);
				potentialSpace[row][column] += recCheckCol(row, column, row);
				potentialSpace[row][column] += recCheckDiagonal(row, column, row, column);
				potentialSpace[row][column] += recCheckBackDiagonal(row, column, row, column);

				if (bestValue < potentialSpace[currentRowIndex[column]][column]) {
					bestValue = potentialSpace[currentRowIndex[column]][column];
					bestIndex = column;
					System.out.println(potentialSpace[currentRowIndex[column]][column]);
				}
			}
		}
		System.out.println(Arrays.deepToString(potentialSpace).replace("[", " ").replace("],", "\n").replace("]]", "")
				.replace(", ", "\t|"));
		return bestIndex;
	}

	public void computerMove() {
		
		for (int column = 0; column < this.numOfColumn; column++) {
			if (findPotentialWin(currentRowIndex[column], column)) {
				updateBoard(column, COMPUTER, HUMAN_VS_COMPUTER, imgRed);
				showWinnerSequence(redTrophy);
				winnerDialog("Computer");
				return;
			} else if (findPotentialLose(currentRowIndex[column], column)) {
				updateBoard(column, COMPUTER, HUMAN_VS_COMPUTER, imgRed);
				currentRowIndex[column]--;
				return;
			}
		}

		if (startPlayer == COMPUTER && count < 3) {
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000);
						int bestColumn = findPotentialSpace();
						updateBoard(bestColumn, COMPUTER, HUMAN_VS_COMPUTER, imgRed);
						currentRowIndex[bestColumn]--;
						count++;
					} catch (InterruptedException ex) {
					}
				}
			}).start();
		} else {
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000);
						if (algorithm == MINMAX) {
							negMax(level, turn);
						} else if (algorithm == ALPHA_BETA) {
							alphaBeta(level, turn, Integer.MIN_VALUE, Integer.MAX_VALUE);
						}
						updateBoard(bestCol, COMPUTER, HUMAN_VS_COMPUTER, imgRed);
						currentRowIndex[bestCol]--;
					} catch (InterruptedException ex) {
					}
				}
			}).start();
		}

	}

	// 1# Algorithm
	public int negMax(int depth, int turn) {
		int best = Integer.MIN_VALUE;
		int value, col;
		if (depth == 0) {
			return -gradeBoard();
		}
		for (col = 0; col < this.numOfColumn; col++) {
			if (this.currentRowIndex[col] > -1) {
				this.logicalBoard[this.currentRowIndex[col]][col] = turn; // Make move
				this.currentRowIndex[col]--;
				value = -negMax(depth - 1, 7 - turn);
				this.currentRowIndex[col]++;
				this.logicalBoard[this.currentRowIndex[col]][col] = FREE_SPACE; // Undo move
				if (value > best) {
					best = value;
					// Only keep best move in the first game tree level
					if (depth == level) {
						bestCol = col;
					}
				}
			}
		}
		return (best);
	}

	// 2# Algorithm
	public int alphaBeta(int depth, int turn, int alpha, int beta) {
		int value, col;
		if (depth == 0) {
			return -gradeBoard();
		}
		for (col = 0; col < this.numOfColumn; col++) {
			if (this.currentRowIndex[col] > -1) {
				this.logicalBoard[this.currentRowIndex[col]][col] = turn; // Make move
				this.currentRowIndex[col]--;
				value = -alphaBeta(depth - 1, 7 - turn, -beta, -alpha);
				this.currentRowIndex[col]++;
				this.logicalBoard[this.currentRowIndex[col]][col] = FREE_SPACE; // Undo move
				if (value >= beta && depth < level) {
					return beta;
				}
				if (value > alpha) {
					alpha = value;
					bestCol = col;
				}
			}
		}
		return alpha;
	}

	class AL implements ActionListener {
		private int col;

		public AL(int col) {
			this.col = col;
		}

		public void actionPerformed(ActionEvent e) {
			if (gameType == HUMAN_VS_HUMAN) {

				if (turn == PLAYER_ONE && currentRowIndex[this.col] > -1) {

					updateBoard(this.col, PLAYER_ONE, HUMAN_VS_HUMAN, imgRed);

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, PLAYER_ONE, "Player One")) {
						showWinnerSequence(redTrophy);
						winnerDialog("Player One");
						return;
					}

					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));

				} else if (turn == PLAYER_TWO && currentRowIndex[this.col] > -1) {
					updateBoard(this.col, PLAYER_TWO, HUMAN_VS_HUMAN, imgBlue);

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, PLAYER_TWO, "Player Two")) {
						showWinnerSequence(blueTrophy);
						winnerDialog("Player Two");
						return;
					}

					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
				}

			} else if (gameType == HUMAN_VS_COMPUTER) {
				if (turn == HUMAN && currentRowIndex[this.col] > -1) {
					updateBoard(this.col, HUMAN, HUMAN_VS_COMPUTER, imgBlue);

					System.out.println();
					System.out.println(Arrays.deepToString(logicalBoard).replace("[", " ").replace("],", " \n")
							.replace("]]", "").replace(", ", " "));

					if (checkWinner(logicalBoard, this.col, HUMAN, "Human")) {
						showWinnerSequence(blueTrophy);
						winnerDialog("Human");
						return;
					}
					currentRowIndex[this.col]--;
					isBoardFull();
					System.out.println(Arrays.toString(currentRowIndex));
					computerMove();
				}
			}
		}
	}
}