package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Game {
    // VERSION: 3.3
    // LAST EDIT: 28.4.20
    // Free space - 0, Player One(RED) - 1, Player Two(BLUE) - 2, Computer - 3
    // HUMAN - 1, COMPUTER - 2
    protected static final int HUMAN_VS_HUMAN = 0;
    protected static final int HUMAN_VS_COMPUTER = 1;
    protected static final int FREE_SPACE = 0;
    protected static final int PLAYER_ONE = 1;
    protected static final int PLAYER_TWO = 2;
    protected static final int COMPUTER = 3;
    protected static final int HUMAN = 4;
    protected static final int MINMAX = 0;

    protected static final int ALPHA_BETA = 1;
    protected static final int DEFAULT_ROWS = 6;
    protected static final int DEFAULT_COLUMNS = 7;
    protected static final int DEFAULT_SEQUENCE = 4;
    protected static final int EASY = 1;
    protected static final int MEDIUM = 3;
    protected static final int HARD = 5;

    private Board gameBoard;
    protected static Computer computer;
    protected static Human humanOne;
    protected static Human humanTwo;

    protected static int gameType;
    protected static int sequence;
    protected static int turn;
    protected static int startPlayer;

    protected File[] fileArray;
    protected static Image imgRed;
    protected static Image iconRed;
    protected static Image imgBlue;
    protected static Image iconBlue;
    protected static Image redTrophy;
    protected static Image blueTrophy;
    protected static Image imgFreeSpace;

    // Builder for Human VS Human game
    public Game(int numOfRow, int numOfColumn, int sequence, int gameType) {
        Random rand = new Random();
        try {
            createImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Game.sequence = sequence;
        Game.gameType = gameType;
        Game.humanOne = new Human(PLAYER_ONE, "Player One", imgRed, redTrophy);
        Game.humanTwo = new Human(PLAYER_TWO, "Player_Two", imgBlue, blueTrophy);
        this.gameBoard = new Board(numOfRow, numOfColumn);

        turn = rand.nextInt(2 - 1 + 1) + 1;
        startPlayer = turn;
        Board.changeTurnIcon(turn);
        printGameInfo(numOfRow, numOfColumn, sequence, gameType);
    }

    // Builder for Human VS Computer game
    public Game(int numOfRow, int numOfColumn, int sequence, int gameType, int level, int algorithm) {
        Random rand = new Random();
        try {
            createImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Game.sequence = sequence;
        Game.gameType = gameType;
        Game.computer = new Computer(COMPUTER, "Computer", level, algorithm, imgRed, redTrophy);
        Game.humanOne = new Human(HUMAN, "Human", imgBlue, blueTrophy);
        this.gameBoard = new Board(numOfRow, numOfColumn);
        turn = rand.nextBoolean() ? COMPUTER : HUMAN;
        startPlayer = turn;
        Board.changeTurnIcon(turn);
        if (startPlayer == computer.getID()) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Board.undoButton.setEnabled(false);
                        Thread.sleep(1000);
                        Game.computer.computerMove();
                    } catch (InterruptedException ex) {
                    }
                }
            }).start();
        }

        printGameInfo(numOfRow, numOfColumn, sequence, gameType);
    }

    //Builder for load game
    public Game(int numOfRow, int numOfColumn,int sequence,int gameType,int turn,int algorithm,int level,int [][]logicalBoard, int [] currentRowIndex)
    {
        try {
            createImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Game.sequence = sequence;
        Game.gameType = gameType;
        Game.turn = turn;
        if(gameType == HUMAN_VS_HUMAN)
        {
            Game.humanOne = new Human(PLAYER_ONE, "Player One", imgRed, redTrophy);
            Game.humanTwo = new Human(PLAYER_TWO, "Player_Two", imgBlue, blueTrophy);
            this.gameBoard= new Board(numOfRow,numOfColumn,logicalBoard,currentRowIndex);
            Board.changeTurnIcon(turn);
        }
        else if (gameType == HUMAN_VS_COMPUTER)
        {
            Game.computer = new Computer(COMPUTER, "Computer", level, algorithm, imgRed, redTrophy);
            Game.humanOne = new Human(HUMAN, "Human", imgBlue, blueTrophy); 
            this.gameBoard= new Board(numOfRow,numOfColumn,logicalBoard,currentRowIndex);
            Board.changeTurnIcon(turn);
            if (turn == computer.getID()) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Board.undoButton.setEnabled(false);
                            Thread.sleep(1000);
                            Game.computer.computerMove();
                        } catch (InterruptedException ex) {
                        }
                    }
                }).start();
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonPadding", 15);

        JFrame mainFrame = new JFrame("N In Row");
        JPanel buttonsPanel = new JPanel(new GridLayout(2,3,10,10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel logo = new JLabel();
        ImageIcon image = new ImageIcon("images/icon.png");
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setBackground(Color.WHITE);

        logo.setIcon(image);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        mainFrame.add(logo, BorderLayout.NORTH);

        JButton normalGameButton = new JButton("Noraml Game");
        JButton humnaVsCoputerButton = new JButton("Human VS Computer");
        JButton customGameButton = new JButton("Custom Game");
        JButton loadGame = new JButton("Load Game");

        normalGameButton.setPreferredSize(new Dimension(170, 30));
        humnaVsCoputerButton.setPreferredSize(new Dimension(170, 30));
        customGameButton.setPreferredSize(new Dimension(170, 30));
        loadGame.setPreferredSize(new Dimension(170, 30));

        normalGameButton.setFont(new Font("Arial", Font.PLAIN, 14));
        humnaVsCoputerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        customGameButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loadGame.setFont(new Font("Arial", Font.PLAIN, 14));

        normalGameButton.addActionListener(e -> {
            new Game(DEFAULT_ROWS, DEFAULT_COLUMNS, DEFAULT_SEQUENCE, HUMAN_VS_HUMAN);
            mainFrame.dispose();
        });

        humnaVsCoputerButton.addActionListener(e -> {
            chooseDifficulty(DEFAULT_ROWS, DEFAULT_COLUMNS, DEFAULT_SEQUENCE, HUMAN_VS_COMPUTER);
            mainFrame.dispose();
        });

        customGameButton.addActionListener(e -> {
            createCustomGame();
            mainFrame.dispose();
        });

        loadGame.addActionListener(e -> {
            loadGame(); 
            mainFrame.dispose();   
        });

        buttonsPanel.add(normalGameButton);
        buttonsPanel.add(humnaVsCoputerButton);
        buttonsPanel.add(customGameButton);
        buttonsPanel.add(loadGame);

        mainFrame.add(buttonsPanel, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    //Loading all game information
    public static void loadGame() {
        int numOfRow = 0, numOfColumn = 0, sequence = 0, gameType = 0, turn = 0, level = 0, algorithm = 0;
        int [][] logicalBoard;
        int [] currentRowIndex;

        try {
            Scanner input = new Scanner(new File("gamesave"));
            numOfRow = input.nextInt();
            numOfColumn = input.nextInt();
            sequence = input.nextInt();
            gameType = input.nextInt();
            turn = input.nextInt();
            algorithm = input.nextInt();
            level = input.nextInt();
            logicalBoard = new int [numOfRow][numOfColumn];
            currentRowIndex = new int[numOfColumn];

            for(int row = 0; row <numOfRow; row++)
            {
                for(int column = 0; column < numOfColumn; column++)
                {
                    logicalBoard[row][column] = input.nextInt();
                }
            }
           
            for(int column = 0; column < numOfColumn; column++)
            {
                currentRowIndex[column] = input.nextInt();
            }
            new Game(numOfRow, numOfColumn, sequence, gameType,turn,algorithm,level,logicalBoard,currentRowIndex);
        } catch (FileNotFoundException e1) {
            System.out.println("Cant open the file");
        }
    }

    public static void createCustomGame() {

        JTextField numOfRowfField = new JTextField();
        JTextField numOfColumnfField = new JTextField();
        JTextField numOfSequencefField = new JTextField();
        String[] gameTypeArray = { "Human VS Human", "Human VS Computer" };
        JComboBox<String> gameTypeList = new JComboBox<>(gameTypeArray);

        final JComponent[] inputs = new JComponent[] { new JLabel("Type the number of row(6 and up):"), numOfRowfField,
                new JLabel("Type the number of column(7 and up):"), numOfColumnfField,
                new JLabel("Type the number of sequence(4 and up):"), numOfSequencefField,
                new JLabel("Choose game mode:"), gameTypeList };

        gameTypeList.addActionListener(e -> {
            gameType = gameTypeList.getSelectedIndex();

        });

        int result = JOptionPane.showConfirmDialog(null, inputs, "Custom game", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if (Integer.parseInt(numOfRowfField.getText()) < DEFAULT_ROWS
                    || Integer.parseInt(numOfColumnfField.getText()) < DEFAULT_COLUMNS
                    || Integer.parseInt(numOfSequencefField.getText()) < DEFAULT_SEQUENCE) {
                createCustomGame();
            } else if (gameType == HUMAN_VS_HUMAN) {
                new Game(Integer.parseInt(numOfRowfField.getText()), Integer.parseInt(numOfColumnfField.getText()),
                        Integer.parseInt(numOfSequencefField.getText()), gameType);

            } else if (gameType == HUMAN_VS_COMPUTER) {
                chooseDifficulty(Integer.parseInt(numOfRowfField.getText()),
                        Integer.parseInt(numOfColumnfField.getText()), Integer.parseInt(numOfSequencefField.getText()),
                        gameType);
            }
        } else {
            System.out.println("User canceled / closed the dialog, result = " + result);
        }
    }

    public static void chooseDifficulty(int row, int column, int sequence, int gameTypeID) {
        String[] algorithm = { "NegMax", "Alpha-Beta" };
        int algorithmAnswer = JOptionPane.showOptionDialog(null, "Choose algorithm:", "Game Settings",
                JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, algorithm, algorithm[0]);

        if (algorithmAnswer == -1) {
            System.exit(0);
        }

        String[] difficultyList = { "Easy", "Medium", "Hard" };

        int difficulty = JOptionPane.showOptionDialog(null, "Choose difficulty level:", "Game Settings",
                JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, difficultyList, difficultyList[0]);
        switch (difficulty) {
            case -1:
                System.out.println("Option Dialog Window Was Closed");
                System.exit(0);

            case 0:
                new Game(row, column, sequence, gameTypeID, EASY, algorithmAnswer);
                break;
            case 1:
                new Game(row, column, sequence, gameTypeID, MEDIUM, algorithmAnswer);
                break;

            case 2:
                new Game(row, column, sequence, gameTypeID, HARD, algorithmAnswer);
                break;

            default:
                break;
        }
    }

    // Print the game settings
    public void printGameInfo(int numOfRow, int numOfColumn, int sequence, int gameType) {
        System.out.println("Game Settings:");
        System.out.println("Rows: " + numOfRow);
        System.out.println("Columns: " + numOfColumn);
        System.out.println("Sequence: " + sequence);
        System.out.println("Game Type: " + gameType);
        System.out.println("Difficult Level: " + ((computer == null) ? 0 : computer.getLevel()));
        System.out.println("Algorithm:"
                + ((computer == null) ? 0 : (computer.getAlgorithm() == MINMAX ? "negMax" : "Alpha-Beta")));
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
        imgFreeSpace = ImageIO.read(fileArray[0]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        imgRed = ImageIO.read(fileArray[1]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        imgBlue = ImageIO.read(fileArray[2]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        iconRed = ImageIO.read(fileArray[3]).getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        iconBlue = ImageIO.read(fileArray[4]).getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        redTrophy = ImageIO.read(fileArray[5]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        blueTrophy = ImageIO.read(fileArray[6]).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
    }

    // Open dialog that show who win the game with options to play agin or exit.
    public static void winnerDialog(String winnerName) {
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
                Board.boardFrame.dispose();
                if (gameType == HUMAN_VS_HUMAN) {
                    new Game(Board.numOfRow, Board.numOfColumn, sequence, gameType);
                } else {
                    new Game(Board.numOfRow, Board.numOfColumn, sequence, gameType, computer.getLevel(),
                            computer.getAlgorithm());
                }
                break;
            case 1:
                System.exit(0);
                break;
            default:
                break;
        }
    }

    // Dialog that open if the end with tie
    public static void endGameDialog() {
        UIManager.put("OptionPane.messageForeground", new Color(255, 204, 0));

        String[] options = { "Play again", "Exit" };
        int response = JOptionPane.showOptionDialog(null, "Tie", "The board is full", JOptionPane.WARNING_MESSAGE,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (response) {
            case -1:
                System.out.println("Option Dialog Window Was Closed");
                System.exit(0);

            case 0:
                Board.boardFrame.dispose();
                if (gameType == HUMAN_VS_HUMAN) {
                    new Game(Board.numOfRow, Board.numOfColumn, sequence, gameType);
                } else {
                    new Game(Board.numOfRow, Board.numOfColumn, sequence, gameType, computer.getLevel(),
                            computer.getAlgorithm());
                }
                break;
            case 1:
                System.exit(0);
                break;

            default:
                break;
        }
    }
}