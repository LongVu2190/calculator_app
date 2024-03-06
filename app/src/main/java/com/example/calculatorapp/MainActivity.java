package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView solutionTextView, resultTextView;
    MaterialButton btnClear, btnOpenInterval, btnCloseInterval, btnAllClear;
    MaterialButton btn_modulo, btn_pow, btn_log, btn_sqrt, btn_pi;
    MaterialButton btnDivideOperator, btnMultiplyOperator, btnPlusOperator, btnMinusOperator, btnResult, btnDot,
            btn_more_functions;
    MaterialButton btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0;
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
        setId(btnClear, R.id.btn_clear);
        setId(btnOpenInterval, R.id.btn_open_interval);
        setId(btnCloseInterval, R.id.btn_close_interval);
        setId(btnDivideOperator, R.id.btn_divide_operator);
        setId(btn_7, R.id.btn_7);
        setId(btn_8, R.id.btn_8);
        setId(btn_9, R.id.btn_9);
        setId(btnMultiplyOperator, R.id.btn_multiply_operator);
        setId(btn_4, R.id.btn_4);
        setId(btn_5, R.id.btn_5);
        setId(btn_6, R.id.btn_6);
        setId(btnPlusOperator, R.id.btn_plus_operator);
        setId(btn_1, R.id.btn_1);
        setId(btn_2, R.id.btn_2);
        setId(btn_3, R.id.btn_3);
        setId(btnMinusOperator, R.id.btn_minus_operator);
        setId(btnAllClear, R.id.btn_all_clear);
        setId(btn_0, R.id.btn_0);
        setId(btnDot, R.id.btn_dot);
        setId(btnResult, R.id.btn_result);
        setId(btn_more_functions, R.id.btn_more_functions);

        // set id and event onclick for advanced functions buttons.
        setId(btn_modulo, R.id.btn_modulo);
        setId(btn_pow, R.id.btn_pow);
        setId(btn_log, R.id.btn_log);
        setId(btn_sqrt, R.id.btn_sqrt);
        setId(btn_pi, R.id.btn_pi);
    }

    private void setId(MaterialButton button, int id) {
        button = findViewById(id);
        button.setOnClickListener(this);
    }

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
                if (prevText.equals(""))
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
