package com.example.gwswaporshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

// Import Firebase and other necessary libraries
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SellItemActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView itemImageView;
    private EditText itemNameField, itemDescriptionField, itemPriceField, itemConditionField;
    private Spinner categorySpinner;
    private Button sellSubmitButton;
    private Uri imageUri;

    private DatabaseReference mDatabase; // Firebase database reference
    private StorageReference mStorageRef; // Firebase storage reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);

        // Initialize Firebase Database and Storage
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Initialize views
        itemImageView = findViewById(R.id.item_image);
        itemNameField = findViewById(R.id.item_name);
        itemDescriptionField = findViewById(R.id.item_description);
        itemPriceField = findViewById(R.id.item_price);
        itemConditionField = findViewById(R.id.item_condition);
        categorySpinner = findViewById(R.id.category_spinner);
        sellSubmitButton = findViewById(R.id.sell_submit_button);

        // Set up ArrayAdapter for the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set up image upload
        itemImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        // Set up submit button
        sellSubmitButton.setOnClickListener(v -> {
            String itemName = itemNameField.getText().toString().trim();
            String itemDescription = itemDescriptionField.getText().toString().trim();
            String itemPrice = itemPriceField.getText().toString().trim();
            String itemCondition = itemConditionField.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();

            // Validate the input fields
            if (itemName.isEmpty() || itemDescription.isEmpty() || itemPrice.isEmpty() || itemCondition.isEmpty() || imageUri == null) {
                Toast.makeText(SellItemActivity.this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if "All Items" is selected as category
            if ("All Items".equals(category)) {
                Toast.makeText(SellItemActivity.this, "Please select an item category", Toast.LENGTH_SHORT).show();
                return; // Stop further execution
            }

            // Create an item object
            Item item = new Item(itemName, itemDescription, itemPrice, itemCondition, category);

            // Upload the item to Firebase
            uploadItemToFirebase(item);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            itemImageView.setImageURI(imageUri);
        }
    }

    private void uploadItemToFirebase(Item item) {
        // Upload the image first
        if (imageUri != null) {
            StorageReference fileReference = mStorageRef.child("images/" + System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Set image URL and upload item details
                        item.imageUrl = uri.toString();
                        String itemId = mDatabase.child("items").push().getKey();
                        mDatabase.child("items").child(itemId).setValue(item);
                        Toast.makeText(SellItemActivity.this, "Item uploaded successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
            ).addOnFailureListener(e ->
                    Toast.makeText(SellItemActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        }
    }

    // Item model class (inner class or separate file)
    public static class Item {
        public String name;
        public String description;
        public String price;
        public String condition;
        public String category;
        public String imageUrl;

        public Item(String name, String description, String price, String condition, String category) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.condition = condition;
            this.category = category;
        }
    }
}
