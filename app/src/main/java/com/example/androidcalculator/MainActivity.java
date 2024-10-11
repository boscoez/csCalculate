package com.example.androidcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import java.math.BigDecimal;
/**
 * MainActivity is the primary activity for the calculator application.
 * It handles the UI interactions and evaluates mathematical expressions.
 */
public class MainActivity extends AppCompatActivity {
    private TextView primaryTextView;  // TextView for displaying primary input
    private TextView secondaryTextView; // TextView for displaying secondary output
    private boolean isDefaultTextDisplayed = true; // Tracks if default text is currently displayed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize TextViews
        primaryTextView = findViewById(R.id.idTVprimary);
        secondaryTextView = findViewById(R.id.idTVSecondary);
        // Set initial text in TextViews
        secondaryTextView.setText("CWU ID: 4398xxxx");
        primaryTextView.setText("Boscoe");
        isDefaultTextDisplayed = true; // Indicate default text is displayed
        setupButtons(); // Set up button click listeners
    }
    /**
     * Sets up the buttons in the calculator UI by assigning click listeners.
     */
    private void setupButtons() {
        // Array of button IDs to set listeners
        int[] buttonIds = new int[]{
                R.id.b0, R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5,
                R.id.b6, R.id.b7, R.id.b8, R.id.b9, R.id.bplus,
                R.id.bminus, R.id.bmul, R.id.bdiv, R.id.bdot,
                R.id.bsin, R.id.bcos, R.id.btan, R.id.blog,
                R.id.bln, R.id.bsqrt, R.id.bsquare, R.id.bfact,
                R.id.binv, R.id.bbrac1, R.id.bbrac2, R.id.bpi,
                R.id.bac, R.id.bc, R.id.bequal
        };
        // Listener for button clicks
        View.OnClickListener listener = v -> {
            Button button = (Button) v;
            final String buttonText = button.getText().toString();
            handleButtonClick(button.getId(), buttonText);
        };
        // Assign the listener to each button
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }
    /**
     * Handles button clicks for various calculator functionalities.
     * @param buttonId The ID of the clicked button.
     * @param buttonText The text displayed on the clicked button.
     */
    private void handleButtonClick(int buttonId, String buttonText) {
        // Clear default text on first interaction
        if (isDefaultTextDisplayed) {
            primaryTextView.setText("");
            secondaryTextView.setText(""); // Clear displays
            isDefaultTextDisplayed = false;
        }
        // Handle different button actions
        if (buttonId == R.id.bac) {
            primaryTextView.setText(""); // Clear button action
            secondaryTextView.setText(""); // Also clear secondary display
        } else if (buttonId == R.id.bc) {
            // Backspace handling for primary and secondary text
            String currentText = primaryTextView.getText().toString();
            if (!currentText.isEmpty()) {
                primaryTextView.setText(currentText.substring(0, currentText.length() - 1));
            }
            String secondaryText = secondaryTextView.getText().toString();
            if (!secondaryText.isEmpty()) {
                secondaryTextView.setText(secondaryText.substring(0, secondaryText.length() - 1));
            }
        } else if (buttonId == R.id.bpi) {
            primaryTextView.append("pi");       // Append 'pi' for calculations
            secondaryTextView.append("π");      // Show π symbol on secondary display
        } else if (buttonId == R.id.bsqrt) {
            primaryTextView.append("sqrt(");    // Append sqrt for calculations
            secondaryTextView.append("√(");     // Show √ symbol on secondary display
        } else if (buttonId == R.id.bfact) {
            primaryTextView.append("fact(");    // Append factorial for calculations
            secondaryTextView.append("!");      // Show factorial symbol on secondary display
        } else if (buttonId == R.id.bsquare) {
            primaryTextView.append("^2");       // Append square operator
            secondaryTextView.append("^2");     // Show square symbol on secondary display
        } else if (buttonId == R.id.binv) {
            primaryTextView.append("1/");       // Append inverse for calculations
            secondaryTextView.append("1/");     // Show inverse symbol on secondary display
        } else if (buttonId == R.id.bplus) {
            primaryTextView.append("+");        // Append addition operator
            secondaryTextView.append("+");      // Show addition symbol on secondary display
        } else if (buttonId == R.id.bminus) {
            primaryTextView.append("-");        // Append subtraction operator
            secondaryTextView.append("-");      // Show subtraction symbol on secondary display
        } else if (buttonId == R.id.bmul) {
            primaryTextView.append("*");        // Append multiplication operator
            secondaryTextView.append("×");      // Show multiplication symbol on secondary display
        } else if (buttonId == R.id.bdiv) {
            primaryTextView.append("/");        // Append division operator
            secondaryTextView.append("÷");      // Show division symbol on secondary display
        } else if (buttonId == R.id.bequal) {
            evaluateExpression();               // Trigger evaluation of the expression
        } else {
            // Append numbers, digits, or other operators to the primary and secondary views
            primaryTextView.append(buttonText);
            secondaryTextView.append(buttonText);
        }
    }
    /**
     * Evaluates the mathematical expression entered in the primary TextView.
     * Displays the result or an error message if evaluation fails.
     */
    private void evaluateExpression() {
        String expression = primaryTextView.getText().toString(); // Get the mathematical expression
        try {
            // Build the expression and define custom functions for factorial, sqrt, trigonometric functions in degrees, and logarithms
            Expression exp = new ExpressionBuilder(expression)
                    .functions(new Function("fact", 1) {
                        @Override
                        public double apply(double... args) {
                            return factorial(args[0]); // Custom factorial calculation
                        }
                    })
                    .functions(new Function("sqrt", 1) {
                        @Override
                        public double apply(double... args) {
                            return Math.sqrt(args[0]); // Square root calculation
                        }
                    })
                    .functions(new Function("pow", 2) {  // Custom power function
                        @Override
                        public double apply(double... args) {
                            BigDecimal base = BigDecimal.valueOf(args[0]);
                            BigDecimal result = base.pow((int) args[1]);  // Calculate using BigDecimal
                            return result.doubleValue(); // Convert back to double for display
                        }
                    })
                    .functions(new Function("sin", 1) {
                        @Override
                        public double apply(double... args) {
                            return Math.sin(Math.toRadians(args[0])); // Convert degrees to radians for sin
                        }
                    })
                    .functions(new Function("cos", 1) {
                        @Override
                        public double apply(double... args) {
                            return Math.cos(Math.toRadians(args[0])); // Convert degrees to radians for cos
                        }
                    })
                    .functions(new Function("tan", 1) {
                        @Override
                        public double apply(double... args) {
                            return Math.tan(Math.toRadians(args[0])); // Convert degrees to radians for tan
                        }
                    })
                    .functions(new Function("log", 1) {
                        @Override
                        public double apply(double... args) {
                            // Validate input for log10
                            if (args[0] <= 0) {
                                throw new IllegalArgumentException("logarithm is undefined for non-positive values");
                            }
                            return Math.log10(args[0]); // Base 10 logarithm (log)
                        }
                    })
                    .functions(new Function("ln", 1) {
                        @Override
                        public double apply(double... args) {
                            // Validate input for ln
                            if (args[0] <= 0) {
                                throw new IllegalArgumentException("logarithm is undefined for non-positive values");
                            }
                            return Math.log(args[0]); // Natural logarithm (ln)
                        }
                    })
                    .build();
            double result = exp.evaluate(); // Evaluate the expression

            // Use scientific notation for very large or very small numbers, or format otherwise
            primaryTextView.setText(formatResult(result));

        } catch (ArithmeticException e) {
            primaryTextView.setText("Overflow"); // Display error for overflow
        } catch (IllegalArgumentException e) {
            primaryTextView.setText(e.getMessage()); // Display error message for invalid log input
        } catch (Exception e) {
            primaryTextView.setText("Error"); // Display generic error message for other exceptions
        }
    }
    /**
     * Formats the result for display, using scientific notation for large/small numbers.
     * @param result The result of the evaluated expression.
     * @return A formatted string representing the result.
     */
    private String formatResult(double result) {
        // Use scientific notation if the result is very large or very small
        if (Math.abs(result) >= 1e6 || Math.abs(result) <= 1e-4) {
            return String.format("%e", result); // Use scientific notation
        }

        // Check if result is an integer (to avoid unnecessary decimal places)
        if (Math.floor(result) == result) {
            return String.format("%.0f", result); // Display as integer
        }

        // For regular results, format with up to 10 decimal places and remove trailing zeros
        return String.format("%.10f", result).replaceAll("\\.?0*$", "");
    }
    /**
     * Calculates the factorial of a given number.
     * @param number The number to calculate the factorial for.
     * @return The factorial of the number.
     * @throws IllegalArgumentException if the number is negative or not an integer.
     */
    private double factorial(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("Negative input not allowed for factorial");
        }
        if (number != Math.floor(number)) {
            throw new IllegalArgumentException("Factorial only supports whole numbers");
        }
        if (number == 0) {
            return 1; // Factorial of 0 is 1
        }
        double fact = 1;
        for (int i = 1; i <= number; i++) {
            fact *= i; // Calculate factorial iteratively
        }
        return fact; // Return the calculated factorial
    }
}
