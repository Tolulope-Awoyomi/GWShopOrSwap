package com.example.gwswaporshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        TextView confirmationMessage = findViewById(R.id.confirmation_message);
        confirmationMessage.setText("We have received your order request, and will reach out within 24 hours to finalize your transaction.");

        // Find the buttons by ID
        TextView goBack = findViewById(R.id.go_back);
        TextView logOut = findViewById(R.id.log_out);

        // Set up click listeners for the TextViews
        goBack.setOnClickListener(v -> {
            // Explicitly navigate back to ItemsActivity
            Intent intent = new Intent(OrderConfirmationActivity.this, Items.class);
            // If you want to clear all other activities and bring ItemsActivity to the top
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clears the activity stack
            startActivity(intent);
        });
    }
}
