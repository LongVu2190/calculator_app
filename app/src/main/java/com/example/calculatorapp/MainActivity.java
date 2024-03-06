package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView solutionTextView, resultTextView;
    MaterialButton btnClear, btnOpenInterval, btnCloseInterval, btnAllClear;
    MaterialButton btnDivideOperator, btnMultiplyOperator, btnPlusOperator, btnMinusOperator, btnResult, btnDot;
    MaterialButton btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0;
    private Calculator calculator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculator = new Calculator();

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
            prevResult = prevResult.substring(0,prevResult.length() - 2);

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

                if(finalResult.equals("Error...")) return;

                if(finalResult.endsWith(".0"))
                    finalResult = finalResult.substring(0, finalResult.length()-2);

                resultTextView.setText(finalResult);
                this.calculator.setCurrentResult(Float.parseFloat(finalResult));

                solutionTextView.setText("");
                return;
            case "C":
                if(prevText.equals("")) return;

                prevText = prevText.substring(0, prevText.length() - 1);
                solutionTextView.setText(prevText);
                return;
            case "+": case "-": case "*": case "/":
                if(prevResult.equals("0")) break;

                solutionTextView.setText(prevResult + buttonText);
                return;
        }
        solutionTextView.setText(prevText + buttonText);
    }

    private String getResult(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();

            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();

            return finalResult;
        } catch (Exception e) {
            return "Error...";
        }

    }

    private String getResult(String data, String prevData) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();

            data = prevData + data;
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();

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
