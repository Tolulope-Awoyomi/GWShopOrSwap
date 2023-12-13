package com.example.gwswaporshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SwapActivity extends Activity {
    // Constants
    private static final int PICK_IMAGE_REQUEST = 1;

    // Firebase references
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    // UI references for previous item
    private ImageView previousItemImage;
    private TextView previousItemName, previousItemDescription, previousItemPrice, previousItemCondition;

    // UI references for new item
    private EditText newItemName, newItemDescription, newItemPrice, newItemCondition;
    private ImageView newItemImage;
    private Uri newItemImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Initialize views for previous item details
        previousItemImage = findViewById(R.id.item_image);
        previousItemName = findViewById(R.id.item_name);
        previousItemDescription = findViewById(R.id.item_description);
        previousItemPrice = findViewById(R.id.item_price);
        previousItemCondition = findViewById(R.id.item_condition);

        // Extract data from intent
        Intent intent = getIntent();
        String previousItemImageUrl = intent.getStringExtra("PREVIOUS_ITEM_IMAGE_URL");
        String previousItemNameValue = intent.getStringExtra("PREVIOUS_ITEM_NAME");
        String previousItemDescriptionValue = intent.getStringExtra("PREVIOUS_ITEM_DESCRIPTION");
        String previousItemPriceValue = intent.getStringExtra("PREVIOUS_ITEM_PRICE");
        String previousItemConditionValue = intent.getStringExtra("PREVIOUS_ITEM_CONDITION");

        // Display previous item details
        Glide.with(this).load(previousItemImageUrl).into(previousItemImage);
        previousItemName.setText(previousItemNameValue);
        previousItemDescription.setText(previousItemDescriptionValue);
        previousItemPrice.setText(previousItemPriceValue);
        previousItemCondition.setText(previousItemConditionValue);

        // Initialize views for new item input
        newItemName = findViewById(R.id.new_item_name);
        newItemDescription = findViewById(R.id.new_item_description);
        newItemPrice = findViewById(R.id.new_item_price);
        newItemCondition = findViewById(R.id.new_item_condition);
        newItemImage = findViewById(R.id.new_item_image);

        // Find the "Previous Page" TextView
        TextView previousPageText = findViewById(R.id.previous_page_text);

        // Set up click listener to go back to the previous page
        previousPageText.setOnClickListener(v -> {
            // Navigate back to the previous page
            onBackPressed();
        });

        // Image upload listener for new item
        newItemImage.setOnClickListener(v -> {
            Intent pickIntent = new Intent();
            pickIntent.setType("image/*");
            pickIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        // Submit button listener
        Button submitButton = findViewById(R.id.swap_submit_button);
        submitButton.setOnClickListener(v -> submitSwap());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            newItemImageUri = data.getData();
            newItemImage.setImageURI(newItemImageUri);
        }
    }

    private void submitSwap() {
        String newName = newItemName.getText().toString().trim();
        String newDescription = newItemDescription.getText().toString().trim();
        String newPrice = newItemPrice.getText().toString().trim();
        String newCondition = newItemCondition.getText().toString().trim();

        // Validate the input fields
        if (newName.isEmpty() || newDescription.isEmpty() || newPrice.isEmpty() || newCondition.isEmpty() || newItemImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload the image and item details to Firebase
        uploadNewItem(newName, newDescription, newPrice, newCondition);
    }

    private void uploadNewItem(String name, String description, String price, String condition) {
        StorageReference fileReference = mStorageRef.child("swap_images/" + System.currentTimeMillis() + ".jpg");
        fileReference.putFile(newItemImageUri).addOnSuccessListener(taskSnapshot ->
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    String itemId = mDatabase.child("swapItems").push().getKey();
                    SwapItem swapItem = new SwapItem(name, description, price, condition, imageUrl);
                    mDatabase.child("swapItems").child(itemId).setValue(swapItem).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Navigate to ConfirmationActivity
                            Intent confirmationIntent = new Intent(SwapActivity.this, SwapConfirmationActivity.class);
                            startActivity(confirmationIntent);
                        } else {
                            Toast.makeText(SwapActivity.this, "Failed to upload swap details", Toast.LENGTH_SHORT).show();
                        }
                    });
                })).addOnFailureListener(e ->
                Toast.makeText(SwapActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // SwapItem class
    public static class SwapItem {
        public String name;
        public String description;
        public String price;
        public String condition;
        public String imageUrl;

        public SwapItem(String name, String description, String price, String condition, String imageUrl) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.condition = condition;
            this.imageUrl = imageUrl;
        }

    }
}
