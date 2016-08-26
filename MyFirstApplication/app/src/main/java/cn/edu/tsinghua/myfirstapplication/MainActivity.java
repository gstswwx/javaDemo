package cn.edu.tsinghua.myfirstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.submitButton);
        if (button != null) {
            button.setOnClickListener(calcBMI);
        }
    }

    private View.OnClickListener calcBMI = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            EditText fieldHeight = (EditText)findViewById(R.id.editTextHeight);
            EditText fieldWeight = (EditText)findViewById(R.id.editTextWeight);
            TextView result = (TextView)findViewById(R.id.textResult);
            TextView fieldSuggest = (TextView)findViewById(R.id.textSuggest);

            if (fieldHeight != null && fieldWeight != null && result != null && fieldSuggest != null) {

                    if(!fieldHeight.getText().toString().equals("") && !fieldWeight.getText().toString().equals("")) {
                        double fHeight = Double.parseDouble(fieldHeight.getText().toString()) / 100;
                        double fWeight = Double.parseDouble(fieldWeight.getText().toString());
                        if(fHeight == 0 || fWeight == 0) {
                            result.setText("");
                            fieldSuggest.setText("请输入合法的数据！");
                        }
                        else {
                            double BMI = fWeight / (fHeight * fHeight);
                            DecimalFormat nf = new DecimalFormat("0.00");
                            result.setText("Your BMI is " + nf.format(BMI));
                            if (BMI > 25) {
                                fieldSuggest.setText("少吃一点！");
                            } else if (BMI < 20)
                                fieldSuggest.setText("多吃一点！");
                            else
                                fieldSuggest.setText("继续保持！");
                        }
                    }
                    else {
                        result.setText("");
                        fieldSuggest.setText("请将数据填写完整！");
                    }
            }
        }
    };

}
