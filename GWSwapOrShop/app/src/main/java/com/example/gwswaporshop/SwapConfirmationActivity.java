package com.example.gwswaporshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SwapConfirmationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_confirmation);

        TextView confirmationMessage = findViewById(R.id.swap_confirmation_message);
        confirmationMessage.setText("We have received your swap request and will be in contact within 24 hours");

        // Find the buttons by ID
        TextView goBack = findViewById(R.id.go_back_text);
        TextView logOut = findViewById(R.id.log_out_text);

        // Set up click listeners for the TextViews
        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(SwapConfirmationActivity.this, Items.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> {
            Intent intent = new Intent(SwapConfirmationActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clears the activity stack
            startActivity(intent);
        });


    }
}
