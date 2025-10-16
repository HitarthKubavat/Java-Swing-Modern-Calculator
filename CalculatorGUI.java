import javax.swing.*;
import javax.swing.border.EmptyBorder; // For padding around the panel
import java.awt.*;
import java.awt.event.*;

public class CalculatorGUI implements ActionListener {

    // --- GUI Components ---
    JFrame frame;
    JTextField displayField;
    JPanel panel;

    // --- Buttons ---
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[9];

    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, clrButton, delButton, negButton;

    // --- Calculator Logic Variables ---
    double num1 = 0, num2 = 0, result = 0;
    char operator;
    boolean newCalculation = true; // Flag to indicate if a new number should clear the display

    // --- Color Palette ---
    Color backgroundColor = new Color(40, 40, 40);      // Dark background
    Color displayColor = new Color(60, 60, 60);         // Slightly lighter for display
    Color textColor = Color.WHITE;                      // White text
    Color operatorColor = new Color(255, 165, 0);       // Orange for operators
    Color equalsColor = new Color(46, 204, 113);        // Green for equals
    Color clearDeleteColor = new Color(231, 76, 60);    // Red for clear/delete
    Color numberButtonColor = new Color(80, 80, 80);    // Dark gray for number buttons
    Color hoverColor = new Color(100, 100, 100);        // Lighter gray for hover

    // --- Constructor: Sets up the GUI ---
    CalculatorGUI() {
        frame = new JFrame("Modern Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 600); // Increased height for better spacing
        frame.setLayout(null);
        frame.getContentPane().setBackground(backgroundColor); // Set frame background
        frame.setResizable(false); // Prevent resizing

        // --- Display Field ---
        displayField = new JTextField();
        displayField.setBounds(30, 30, 360, 70); // Wider and taller display
        displayField.setFont(new Font("Arial", Font.BOLD, 45)); // Modern font, larger
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setBackground(displayColor);
        displayField.setForeground(textColor);
        displayField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding inside display
        displayField.setText("0"); // Start with 0

        // --- Initialize Function Buttons ---
        addButton = createButton("+", operatorColor);
        subButton = createButton("-", operatorColor);
        mulButton = createButton("*", operatorColor);
        divButton = createButton("/", operatorColor);
        decButton = createButton(".", numberButtonColor);
        equButton = createButton("=", equalsColor);
        clrButton = createButton("C", clearDeleteColor);
        delButton = createButton("Del", clearDeleteColor);
        negButton = createButton("(-)", numberButtonColor);

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = clrButton;
        functionButtons[7] = delButton;
        functionButtons[8] = negButton;

        // Add ActionListener and Hover effect to function buttons
        for (int i = 0; i < 9; i++) {
            functionButtons[i].addActionListener(this);
            addHoverEffect(functionButtons[i]);
        }

        // Initialize Number Buttons
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createButton(String.valueOf(i), numberButtonColor);
            numberButtons[i].addActionListener(this);
            addHoverEffect(numberButtons[i]);
        }

        // --- Special Button Placements (Outside Grid Panel) ---
        delButton.setBounds(30, 480, 110, 60); // x, y, width, height
        clrButton.setBounds(145, 480, 110, 60);
        negButton.setBounds(280, 480, 110, 60); // Adjusted position for better balance

        // --- Panel for main calculator grid ---
        panel = new JPanel();
        panel.setBounds(30, 120, 360, 350); // Wider and taller panel, adjusted Y
        panel.setLayout(new GridLayout(4, 4, 15, 15)); // Increased gaps
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0)); // No extra border on panel

        // --- Add buttons to the panel based on desired layout ---
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(divButton); // Moved div here

        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(mulButton);

        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(subButton);

        panel.add(decButton); // Decimal button
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(addButton); // Add button at the bottom right


        // --- Add components to the frame ---
        frame.add(panel);
        frame.add(delButton);
        frame.add(clrButton);
        frame.add(negButton);
        frame.add(displayField);
        frame.setVisible(true);
    }

    // --- Helper to create styled buttons ---
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 28)); // Consistent modern font, slightly smaller
        button.setFocusable(false);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Remove default border
        return button;
    }

    // --- Helper to add hover effect ---
    private void addHoverEffect(JButton button) {
        Color originalBg = button.getBackground();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor); // Change to hover color on mouse enter
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg); // Revert to original color on mouse exit
            }
        });
    }

    // --- Main method ---
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater for thread-safe GUI creation
        SwingUtilities.invokeLater(CalculatorGUI::new);
    }

    // --- ActionListener Interface Implementation ---
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand(); // Get the text of the clicked button

        // Handle number and decimal button clicks
        if ("0123456789.".contains(command)) {
            if (displayField.getText().equals("0") || newCalculation) {
                displayField.setText(command);
                newCalculation = false;
            } else if (command.equals(".") && displayField.getText().contains(".")) {
                // Do nothing if decimal already exists
            } else {
                displayField.setText(displayField.getText().concat(command));
            }
        }
        
        // Handle operator button clicks
        if ("+-*/".contains(command)) {
            if (displayField.getText().isEmpty() || displayField.getText().equals("Error")) {
                return; // Prevent errors if display is empty or showing error
            }
            num1 = Double.parseDouble(displayField.getText());
            operator = command.charAt(0);
            displayField.setText(""); // Clear display for next number input
            newCalculation = false;
        }

        // Handle equals button click
        if (e.getSource() == equButton) {
            if (displayField.getText().isEmpty()) {
                // If equals is pressed without a second number, do nothing
                return;
            }
            num2 = Double.parseDouble(displayField.getText());

            try {
                switch (operator) {
                    case '+': result = num1 + num2; break;
                    case '-': result = num1 - num2; break;
                    case '*': result = num1 * num2; break;
                    case '/':
                        if (num2 == 0) {
                            displayField.setText("Error");
                            return;
                        }
                        result = num1 / num2;
                        break;
                    default: // If no operator was selected yet or initial state
                        result = num2; // Just display the second number
                        break;
                }
                // Format result to avoid excessive decimal places for whole numbers
                if (result == (long) result) {
                    displayField.setText(String.format("%d", (long) result));
                } else {
                    displayField.setText(String.format("%.2f", result)); // Limit to 2 decimal places
                }
                
            } catch (NumberFormatException ex) {
                displayField.setText("Error");
            }
            num1 = result; // Set result as num1 for chained operations
            newCalculation = true; // Next number input should clear display
        }

        // Handle clear button click
        if (e.getSource() == clrButton) {
            displayField.setText("0");
            num1 = 0; num2 = 0; result = 0;
            operator = '\0'; // Reset operator
            newCalculation = true;
        }

        // Handle delete button click (backspace)
        if (e.getSource() == delButton) {
            String currentText = displayField.getText();
            if (currentText.length() > 1 && !currentText.equals("Error")) {
                displayField.setText(currentText.substring(0, currentText.length() - 1));
            } else { // If only one character or "0", set to "0"
                displayField.setText("0");
                newCalculation = true;
            }
        }

        // Handle negative button click
        if (e.getSource() == negButton) {
            String currentText = displayField.getText();
            if (!currentText.isEmpty() && !currentText.equals("0") && !currentText.equals("Error")) {
                try {
                    double temp = Double.parseDouble(currentText);
                    temp *= -1;
                    displayField.setText(String.valueOf(temp));
                } catch (NumberFormatException ex) {
                    // Ignore if not a valid number
                }
            }
        }
    }
}