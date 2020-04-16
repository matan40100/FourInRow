import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


public class ButtomPanel extends JPanel{
    private int column;
    private Color colorText;
    private Color backgroundColor;

    public ButtomPanel(int column, Color colorText, Color backgroundColor)
    {
        this.column = column;
        this.colorText = colorText;;
        this.backgroundColor = backgroundColor;


        JLabel numberOfColumn;
        GridLayout grid = new GridLayout(1,column);
        setLayout(grid);
        for (int i = 0; i < column; i++) {
			numberOfColumn = new JLabel("" + (i + 1));
			numberOfColumn.setFont(new Font("Arial", Font.BOLD, 24));
			numberOfColumn.setHorizontalAlignment(JLabel.CENTER);
			numberOfColumn.setBackground(backgroundColor);
			numberOfColumn.setForeground(colorText);
			numberOfColumn.setBorder(new EmptyBorder(4, 0, 4, 0));
			numberOfColumn.setOpaque(true);
			add(numberOfColumn);
		}
    }  
}