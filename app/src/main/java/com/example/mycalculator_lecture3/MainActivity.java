package com.example.mycalculator_lecture3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView display; // what the user will see
    private double firstNumber = 0;
    private String operator = "";
    private boolean isNewCalculation = true;  // Flag to check if a new calculation is needed

    private TextView calculationHistory; // Holds the entire input up until now (without the current number being typed)
    private boolean isError=false;
    private int countOperand=0; // no more than one operand for calculating

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        display = findViewById(R.id.textViewResult); // find the button we clicked
        calculationHistory = findViewById(R.id.textViewCalculation);

    }

    // when click on 0-9
    public void onNumberClick(View view) {
        Button button = (Button) view; // get the button we clicked
        // If it's a new calculation, reset the current number
        if (isNewCalculation) {
            display.setText(""); // clear the display if a new calculation is starting
            isNewCalculation=false;
            isError=false;
            countOperand=0;
        }
        display.append((button.getText().toString())); // add the text to the input
        display.setText(display.getText().toString()); // to show the value on result
    }

    // when click on /, X, +, -
    public void onOperatorClick(View view) {
        Button button = (Button) view; // get the button we clicked
        String clickedOperator = button.getText().toString();
        if (isError)
            return;
        if (!display.getText().toString().isEmpty() && countOperand == 0) {
            if (countOperand < 1) {
                firstNumber = Double.parseDouble(display.getText().toString()); // take the string of the button and convert to int
                operator = clickedOperator;
                calculationHistory.append(display.getText().toString() + " " + operator + " ");
                countOperand++;
                display.setText("");
            } else {
                display.setText("Finish the first calculation!");
                isError=true;
                calculationHistory.setText("");
                isNewCalculation = true;
            }
        }
        else if (!operator.isEmpty() && countOperand == 1){
                // If there is already a number and an operator,replace the old operator with the new one
                    operator = clickedOperator;
                    int index = calculationHistory.getText().toString().lastIndexOf(" ");
                    String historyText = calculationHistory.getText().toString();
                    calculationHistory.setText(calculationHistory.getText().toString().substring(0, index - 1));
                    calculationHistory.append(operator + " ");
                }
        }


    // when click on =
    // we need to do a calculation
    @SuppressLint("DefaultLocale")
    public void onEqualsClick(View view) {
        if (!operator.isEmpty() && !display.getText().toString().isEmpty()) {
            double secondNumber = Double.parseDouble(display.getText().toString());  // take the string of the button and convert to int
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "X":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    if (secondNumber != 0) {
                        result = (double) firstNumber / secondNumber;
                    } else {
                        display.setText(" ERROR!!");
                        isError=true;
                        isNewCalculation = true;
                        firstNumber=0;
                        countOperand=0;
                        calculationHistory.setText("");
                        return;
                    }
                    break;
            }

            if (result == (int) result)
                display.setText(" " + (int) result);
            else
                display.setText(String.format("%.9f", result));

            firstNumber = result; // Update first number for the next operation
            operator = ""; // Reset operator after calculation
            // Set the flag to indicate that the next number clicked starts a new calculation
            isNewCalculation = true;
            countOperand=0;
            // set the current calculation TextView
            calculationHistory.setText("");

        }
    }

    // when click on AC
    public void onClearClick(View view) {
        firstNumber = 0;
        operator = "";
        display.setText("");
        isNewCalculation = true; // Reset the flag when clearing the calculation
        calculationHistory.setText("");
        countOperand=0;
    }
}