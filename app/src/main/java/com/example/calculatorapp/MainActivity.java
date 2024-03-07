package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView solutionTextView, resultTextView;
    private Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculator = new Calculator();

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

        // set id and event onclick for advanced functions buttons.
        setId(R.id.btn_modulo);
        setId(R.id.btn_pow);
        setId(R.id.btn_log);
        setId(R.id.btn_sqrt);
        setId(R.id.btn_pi);

        setId(R.id.btn_e);
        setId(R.id.btn_abs);
        setId(R.id.btn_sin);
        setId(R.id.btn_cos);
        setId(R.id.btn_tan);

        setId(R.id.btn_rotate);
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
            case "√":
                solutionTextView.setText(prevText + "√(");
                return;
            case "ln":
                solutionTextView.setText(prevText + "ln(");
                return;
            case "abs":
                solutionTextView.setText(prevText + "abs(");
                return;
            case "sin":
                solutionTextView.setText(prevText + "sin(");
                return;
            case "cos":
                solutionTextView.setText(prevText + "cos(");
                return;
            case "tan":
                solutionTextView.setText(prevText + "tan(");
                return;

        }

        if (v.getId() == R.id.btn_rotate) {
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            return;
        }

        solutionTextView.setText(prevText + buttonText);
    }

    private String getResult(String data) {
        try {
            if (data.contains("^")) {
                data = data.replace("^", ", ");
                data = "Math.pow(" + data + ")";
            }

            else if (data.contains("ln")) {
                data = data.replace("ln", "Math.log");
            }

            else if (data.contains("√")) {
                data = data.replace("√", "Math.sqrt");
            }

            else if (data.contains("π")) {
                data = data.replace("π", "Math.PI");
            }

            else if (data.contains("e")) {
                data = data.replace("e", "2.71828");
            }

            else if (data.contains("abs")) {
                data = data.replace("abs", "Math.abs");
            }

            else if (data.contains("sin")) {
                data = data.replace("sin", "Math.sin");
            }

            else if (data.contains("cos")) {
                data = data.replace("cos", "Math.cos");
            }

            else if (data.contains("tan")) {
                data = data.replace("tan", "Math.tan");
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
