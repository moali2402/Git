package dev.vision.bmi.advicer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;

;

public class HealthAdvicer extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		View calculateButton = findViewById(R.id.button1);
		calculateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				Double weight = getValue(R.id.weight);
				Double height = getValue(R.id.height);
				Integer age = new Integer(((EditText) findViewById(R.id.age)).getText().toString());

				RadioGroup gender = (RadioGroup) findViewById(R.id.gender);
				RadioButton selectedGender = (RadioButton) findViewById(gender.getCheckedRadioButtonId());

				Double bmr = Gender.valueOf((String)selectedGender.getText()).calculateBMR(weight, height, age);
				Double bmi = weight / (Math.pow(height/100, 2));
				((TextView) findViewById(R.id.bmi_index)).setText(String.format("%.1f", bmi));
				((TextView) findViewById(R.id.bmr_index)).setText(bmr.toString());
				findViewById(R.id.RelativeLayout1).setVisibility(View.VISIBLE);
			}

			private Double getValue(int id) {
				return new Double(((EditText) findViewById(id)).getText()
						.toString());
			}
		});
	}
}