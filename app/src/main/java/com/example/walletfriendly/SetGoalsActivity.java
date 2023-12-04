package com.example.walletfriendly;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SetGoalsActivity extends AppCompatActivity {
    private EditText editTextFoodGoal;
    private EditText editTextRentalGoal;
    private EditText editTextBillsGoal;
    private EditText editTextTransportationGoal;
    private EditText editTextOtherGoal;
    private TextView textViewTotalGoal;
    private Button buttonSaveGoals;
    private Button buttonClearGoals;
    private ScrollView scrollViewGoals;
    private LinearLayout linearGoalsContainer;
    private List<View> goalViews;
    private DatabaseHelper dbHelper;
    private int budgetNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Find views by their IDs
        editTextFoodGoal = findViewById(R.id.eTFoodGoal);
        editTextRentalGoal = findViewById(R.id.eTRentalGoal);
        editTextBillsGoal = findViewById(R.id.eTBillsGoal);
        editTextTransportationGoal = findViewById(R.id.eTTransportationGoal);
        editTextOtherGoal = findViewById(R.id.eTOtherGoal);
        textViewTotalGoal = findViewById(R.id.tVTotalGoal);
        buttonSaveGoals = findViewById(R.id.btnSaveGoals);
        buttonClearGoals = findViewById(R.id.btnClearGoals);
        scrollViewGoals = findViewById(R.id.scrollViewGoals);
        linearGoalsContainer = findViewById(R.id.linearGoalsContainer);
        goalViews = new ArrayList<>();

        // Disable the save goals button initially
        buttonSaveGoals.setEnabled(false);

        // Add TextChangeListeners to update the total goal amount dynamically
        editTextFoodGoal.addTextChangedListener(new GoalTextWatcher());
        editTextRentalGoal.addTextChangedListener(new GoalTextWatcher());
        editTextBillsGoal.addTextChangedListener(new GoalTextWatcher());
        editTextTransportationGoal.addTextChangedListener(new GoalTextWatcher());
        editTextOtherGoal.addTextChangedListener(new GoalTextWatcher());

        // Set click listeners for save and clear goals buttons
        buttonSaveGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoals();
            }
        });

        buttonClearGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearGoals();
            }
        });
    }

    private void saveGoals() {
        // Get the goal amounts entered by the user
        double foodGoal = parseDoubleValue(editTextFoodGoal.getText().toString());
        double rentalGoal = parseDoubleValue(editTextRentalGoal.getText().toString());
        double billsGoal = parseDoubleValue(editTextBillsGoal.getText().toString());
        double transportationGoal = parseDoubleValue(editTextTransportationGoal.getText().toString());
        double otherGoal = parseDoubleValue(editTextOtherGoal.getText().toString());

        // Calculate the total goal amount
        double totalGoal = foodGoal + rentalGoal + billsGoal + transportationGoal + otherGoal;

        // Set the total goal amount in the TextView
        textViewTotalGoal.setText(String.valueOf(totalGoal));

        // Save the goals to the database
        long rowId = dbHelper.addGoal(foodGoal, rentalGoal, billsGoal, transportationGoal, otherGoal, totalGoal);
        if (rowId != -1) {
            // Create and add the goal view dynamically
            View goalView = createGoalView(budgetNumber, totalGoal);
            linearGoalsContainer.addView(goalView);
            goalViews.add(goalView);

            // Increment the budget number for the next goal
            budgetNumber++;

            // Clear the goal amounts entered by the user
            clearGoalAmounts();

            // Disable the save goals button
            buttonSaveGoals.setEnabled(false);
        } else {
            Toast.makeText(this, "Failed to save goals to the database.", Toast.LENGTH_SHORT).show();
        }
    }

    private View createGoalView(int budgetNumber, double totalGoal) {
        // Inflate the goal item layout
        View goalView = getLayoutInflater().inflate(R.layout.goal_item, null);

        // Find views inside the inflated layout
        TextView textViewBudgetNumber = goalView.findViewById(R.id.textViewBudgetNumber);
        TextView textViewTotalGoal = goalView.findViewById(R.id.textViewTotalGoal);

        // Set the budget number and total goal amount in the views
        textViewBudgetNumber.setText("Monthly Budget " + budgetNumber);
        textViewTotalGoal.setText(String.valueOf(totalGoal));

        // Set layout params for the total goal TextView (optional)
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 16); // Adjust the margins as per your requirement
        textViewTotalGoal.setLayoutParams(layoutParams);

        return goalView;
    }

    private void clearGoals() {
        // Clear the goal views and remove them from the container
        for (View goalView : goalViews) {
            linearGoalsContainer.removeView(goalView);
        }
        goalViews.clear();

        // Clear the goal amounts entered by the user
        clearGoalAmounts();

        // Reset the budget number
        budgetNumber = 1;

        // Reset the total goal amount and update the TextView
        textViewTotalGoal.setText("0.0");

        // Disable the save goals button
        buttonSaveGoals.setEnabled(false);
    }

    private void clearGoalAmounts() {
        editTextFoodGoal.setText("");
        editTextRentalGoal.setText("");
        editTextBillsGoal.setText("");
        editTextTransportationGoal.setText("");
        editTextOtherGoal.setText("");
    }

    private double parseDoubleValue(String value) {
        if (value.isEmpty()) {
            return 0.0;
        } else {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }

    private class GoalTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Enable the save goals button
            buttonSaveGoals.setEnabled(true);
        }
    }
}
