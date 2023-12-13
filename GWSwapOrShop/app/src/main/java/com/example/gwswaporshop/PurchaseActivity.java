package com.example.gwswaporshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PurchaseActivity extends AppCompatActivity {

    private RadioGroup paymentOptionsGroup;
    private Spinner pickupLocationSpinner;
    private EditText editTextGWId;
    private String itemImageUrl, itemName, itemPrice, itemDescription, itemCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        // Initialize views
        ImageView itemImageView = findViewById(R.id.item_image);
        TextView itemNameView = findViewById(R.id.item_name);
        TextView itemPriceView = findViewById(R.id.item_price);
        TextView itemDescriptionView = findViewById(R.id.item_description);
        TextView itemConditionView = findViewById(R.id.item_condition);
        editTextGWId = findViewById(R.id.EnterGWId); // EditText for GWID
        paymentOptionsGroup = findViewById(R.id.payment_options_group);
        pickupLocationSpinner = findViewById(R.id.spinner_pickup_location);

        // Get data passed from DetailActivity
        Intent intent = getIntent();
        itemImageUrl = intent.getStringExtra("ITEM_IMAGE_URL");
        itemName = intent.getStringExtra("ITEM_NAME");
        itemPrice = intent.getStringExtra("ITEM_PRICE");
        itemDescription = intent.getStringExtra("ITEM_DESCRIPTION");
        itemCondition = intent.getStringExtra("ITEM_CONDITION");

        // Set data to views
        Glide.with(this).load(itemImageUrl).into(itemImageView);
        itemNameView.setText(itemName);
        itemPriceView.setText(itemPrice);
        itemDescriptionView.setText(itemDescription);
        itemConditionView.setText(itemCondition);

        // Setup the spinner with a prompt
        setupSpinner();

        paymentOptionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_gworld) {
                    editTextGWId.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radio_cash) {
                    editTextGWId.setVisibility(View.GONE);
                }
            }
        });

        // Find the "Previous Page" TextView
        TextView previousPage = findViewById(R.id.previous_page);

        // Set up click listener to mimic the hardware back button press
        previousPage.setOnClickListener(v -> {
                    // Simulate a back button press
                    onBackPressed();
                });


        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Submit purchase details
                submitPurchaseDetails();
            }
        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pickup_locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupLocationSpinner.setAdapter(adapter);
        pickupLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle spinner item selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void submitPurchaseDetails() {
        int selectedPaymentId = paymentOptionsGroup.getCheckedRadioButtonId();
        RadioButton selectedPaymentButton = findViewById(selectedPaymentId);

        // Check if a payment option is selected
        if (selectedPaymentId == -1) {
            Toast.makeText(this, "Please select a payment option (GWorld or Cash)", Toast.LENGTH_LONG).show();
            return;
        }

        String selectedPayment = selectedPaymentButton.getText().toString();
        String selectedLocation = (String) pickupLocationSpinner.getSelectedItem();

        // Validate GWID if GWorld is selected
        if (selectedPayment.equals("GWorld")) {
            String gwId = editTextGWId.getText().toString();
            if (gwId.length() != 9) {
                Toast.makeText(this, "GW ID must be 9 characters", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // Ensure a pick-up location is selected
        if (selectedLocation.equals("Pick-up Location")) {
            Toast.makeText(this, "Please select a pick-up location", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if the GWID field is empty when GWorld is selected
        if (selectedPayment.equals("GWorld") && editTextGWId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your GW ID", Toast.LENGTH_LONG).show();
            return;
        }

        // Submit order to Firebase
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = orderRef.push().getKey();
        Order order = new Order(itemName, itemPrice, itemDescription, itemCondition, selectedPayment, selectedLocation);
        if (orderId != null) {
            orderRef.child(orderId).setValue(order)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Create an intent for CartActivity and pass the item details
                            Intent cartIntent = new Intent(PurchaseActivity.this, OrderConfirmationActivity.class);
                            cartIntent.putExtra("ITEM_NAME", itemName);
                            cartIntent.putExtra("ITEM_PRICE", itemPrice);
                            cartIntent.putExtra("ITEM_IMAGE_URL", itemImageUrl);
                            cartIntent.putExtra("ITEM_DESCRIPTION", itemDescription);
                            cartIntent.putExtra("ITEM_CONDITION", itemCondition);
                            // Start CartActivity
                            startActivity(cartIntent);
                        } else {
                            Toast.makeText(PurchaseActivity.this, "Failed to submit order", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
