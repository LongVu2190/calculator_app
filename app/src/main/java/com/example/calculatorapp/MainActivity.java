package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView solutionTextView, resultTextView;

    LinearLayout advanced_functions, last_row;
    private Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculator = new Calculator();

        advanced_functions = findViewById(R.id.advanced_functions);
        advanced_functions.setVisibility(View.GONE);

        last_row = findViewById(R.id.last_row);

        resultTextView = findViewById(R.id.result_text);
        solutionTextView = findViewById(R.id.solution_text);

        // set id and event onclick for controller buttons.
        setId(R.id.btn_clear);
        setId(R.id.btn_open_interval);
        setId(R.id.btn_close_interval);
        setId(R.id.btn_divide_operator);
        setId(R.id.btn_7);
        setId(R.id.btn_8);
        setId(R.id.btn_9);
        setId(R.id.btn_multiply_operator);
        setId(R.id.btn_4);
        setId(R.id.btn_5);
        setId(R.id.btn_6);
        setId(R.id.btn_plus_operator);
        setId(R.id.btn_1);
        setId(R.id.btn_2);
        setId(R.id.btn_3);
        setId(R.id.btn_minus_operator);
        setId(R.id.btn_all_clear);
        setId(R.id.btn_0);
        setId(R.id.btn_dot);
        setId(R.id.btn_result);
        setId(R.id.btn_more_functions);

        // set id and event onclick for advanced functions buttons.
        setId(R.id.btn_modulo);
        setId(R.id.btn_pow);
        setId(R.id.btn_log);
        setId(R.id.btn_sqrt);
        setId(R.id.btn_pi);
    }

    private void setId(int id) {
        MaterialButton button = findViewById(id);
        button.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String prevText = solutionTextView.getText().toString();
        String prevResult = Float.toString(this.calculator.getCurrentResult());

        if (prevResult.endsWith(".0"))
            prevResult = prevResult.substring(0, prevResult.length() - 2);

        switch (buttonText) {
            case "AC":
                this.calculator.setCurrentResult(0);
                solutionTextView.setText("");
                resultTextView.setText("0");
                return;
            case "=":
                String finalResult;
                String data = solutionTextView.getText().toString();

                finalResult = this.getResult(data);

                if (finalResult.equals("Error..."))
                    return;

                if (finalResult.endsWith(".0"))
                    finalResult = finalResult.substring(0, finalResult.length() - 2);

                resultTextView.setText(finalResult);
                this.calculator.setCurrentResult(Float.parseFloat(finalResult));

                solutionTextView.setText("");
                return;
            case "C":
                if (prevText.isEmpty())
                    return;

                prevText = prevText.substring(0, prevText.length() - 1);
                solutionTextView.setText(prevText);
                return;
            case "+":
            case "-":
            case "*":
            case "/":
                if (prevResult.equals("0"))
                    break;

                solutionTextView.setText(prevResult + buttonText);
                return;
        }

        if (v.getId() == R.id.btn_more_functions) {
            if (advanced_functions.getVisibility() == View.GONE) {
                advanced_functions.setVisibility(View.VISIBLE);
                last_row.setVisibility(View.GONE);
            } else {
                advanced_functions.setVisibility(View.GONE);
                last_row.setVisibility(View.VISIBLE);
            }
            return;
        }

        solutionTextView.setText(prevText + buttonText);
    }

    private String getResult(String data) {
        try {
            // Replace "^" with ", " and wrap with "Math.pow()"
            if (data.contains("^")) {
                data = data.replace("^", ", ");
                data = "Math.pow(" + data + ")";
            }

            if (data.contains("ln")) {
                data = data.replace("ln", "Math.log");
            }

            if (data.contains("√")) {
                data = data.replace("√", "Math.sqrt");
            }

            if (data.contains("π")) {
                data = data.replace("π", "Math.PI");
            }

            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();

            double result = Double
                    .parseDouble(context.evaluateString(scriptable, data, "Javascript", 1, null).toString());
            @SuppressLint("DefaultLocale")
            String finalResult = result % 1 == 0 ? String.valueOf((int) result) : String.format("%.2f", result);

            return finalResult;
        } catch (Exception e) {
            return "Error...";
        }
    }
}

class Calculator {
    public Calculator() {
        this.currentResult = 0;
    }

    private float currentResult;

    public float getCurrentResult() {
        return currentResult;
    }

    public void setCurrentResult(float currentResult) {
        this.currentResult = currentResult;
    }
}
