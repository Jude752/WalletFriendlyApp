package com.example.walletfriendly;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExpensesAndIncomeActivity extends AppCompatActivity {
    private EditText expensesEditText;
    private EditText incomeEditText;
    private TextView balanceTextView;
    private TextView budgetTextView;
    private double expenses = 0.0;
    private double income = 0.0;
    private double budget = 0.0;
    private double totalGoal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_and_income);

        // Find views by their IDs
        expensesEditText = findViewById(R.id.editTextExpenses);
        incomeEditText = findViewById(R.id.editTextIncome);
        balanceTextView = findViewById(R.id.tVBalanceValue);
        budgetTextView = findViewById(R.id.tVBudgetValue);
        Button expensesButton = findViewById(R.id.btnExpenses);
        Button incomeButton = findViewById(R.id.btnIncome);
        Button clearButton = findViewById(R.id.btnClear);

        // Set onClickListener for expenses button
        expensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpensesDialog();
            }
        });

        // Set onClickListener for income button
        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIncomeDialog();
            }
        });

        // Set onClickListener for clear button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        // Set initial budget value
        totalGoal = getSetGoalsTotal(); // You need to implement this method to fetch the total goal
        budget = totalGoal - expenses;
        budgetTextView.setText(String.valueOf(budget));
        updateBalance();
    }

    // Method to update the balance and budget text views
    private void updateBalance() {
        double balance = income - expenses;
        String balanceText = String.format("%.2f", balance);
        balanceTextView.setText(balanceText);

        double remainingBudget = totalGoal - expenses;
        String remainingBudgetText = String.format("%.2f", remainingBudget);
        budgetTextView.setText(remainingBudgetText);
    }

    // Method to show the expenses dialog
    private void showExpensesDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_expenses, null);
        final EditText editText = view.findViewById(R.id.editTextDialogExpenses);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final Spinner spinner = view.findViewById(R.id.spinnerExpenses);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Expenses");
        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String expensesText = editText.getText().toString();
                String category = spinner.getSelectedItem().toString();
                if (!expensesText.isEmpty()) {
                    expenses += Double.parseDouble(expensesText);
                    updateBalance();
                    expensesEditText.setText(String.valueOf(expenses));

                    // Insert data into the database
                    DatabaseHelper dbHelper = new DatabaseHelper(ExpensesAndIncomeActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_EXPENSES, Double.parseDouble(expensesText));
                    values.put(DatabaseHelper.COLUMN_CATEGORY, category);
                    values.put(DatabaseHelper.COLUMN_INCOME, income);
                    values.put(DatabaseHelper.COLUMN_BALANCE, income - expenses);
                    values.put(DatabaseHelper.COLUMN_BUDGET, totalGoal - expenses);

                    db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values);

                    db.close();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Method to show the income dialog
    private void showIncomeDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_income, null);
        final EditText editText = view.findViewById(R.id.editTextDialogIncome);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Income");
        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String incomeText = editText.getText().toString();
                if (!incomeText.isEmpty()) {
                    income += Double.parseDouble(incomeText);
                    updateBalance();
                    incomeEditText.setText(String.valueOf(income));

                    // Insert data into the database
                    DatabaseHelper dbHelper = new DatabaseHelper(ExpensesAndIncomeActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_EXPENSES, expenses);
                    values.put(DatabaseHelper.COLUMN_CATEGORY, "");
                    values.put(DatabaseHelper.COLUMN_INCOME, Double.parseDouble(incomeText));
                    values.put(DatabaseHelper.COLUMN_BALANCE, income - expenses);
                    values.put(DatabaseHelper.COLUMN_BUDGET, totalGoal - expenses);

                    db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values);

                    db.close();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("Range")
    private double getSetGoalsTotal() {
        // Fetch the total goal from the appropriate source, such as a database or API
        // For this example, let's assume the total goal is retrieved from a database
        double totalGoal = 0.0;

        // Replace this code with your logic to fetch the total goal
        // For example, if you have a Goals table in your database:
        DatabaseHelper dbHelper = new DatabaseHelper(ExpensesAndIncomeActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {DatabaseHelper.COLUMN_TOTAL_GOAL};
        Cursor cursor = db.query(DatabaseHelper.TABLE_GOALS, projection, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            totalGoal = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_GOAL));
        }

        cursor.close();
        db.close();

        return totalGoal;
    }

    private void clearFields() {
        // Clear income and expenses fields
        expensesEditText.setText("0");
        incomeEditText.setText("0");

        // Reset expenses and income variables
        expenses = 0.0;
        income = 0.0;

        // Update balance and budget
        updateBalance();
    }
}
