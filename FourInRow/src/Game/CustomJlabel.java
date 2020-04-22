package Game;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;

public class CustomJlabel extends JPanel {
    private String title;
    private int number;
    private Color textColor;
    private Color backgroundColor;
    private boolean secondLine;
    private Font textFont;

    CustomJlabel(String title, int number, Color textColor, Color backgroundColor, boolean secondLine, Font textFont) {
        this.title = title;
        this.number = number;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.secondLine = secondLine;
        this.textFont = textFont;

        Font customFont = new Font("Arial", Font.BOLD, 16);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel labelTwo = new JLabel("" + this.number, JLabel.CENTER);
        labelTwo.setFont(this.textFont);
        labelTwo.setForeground(this.textColor);
        labelTwo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
       

        if (this.secondLine) {
            JLabel labelOne = new JLabel(this.title, JLabel.CENTER);
            labelOne.setFont(customFont);
            labelOne.setForeground(this.textColor);
            labelOne.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            add(labelOne);

        }
        add(labelTwo);
        setBackground(this.backgroundColor);
       
    }
}