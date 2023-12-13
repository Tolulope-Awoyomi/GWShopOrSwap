package com.example.gwswaporshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class DetailActivity extends Activity {

    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve data passed from the previous activity
        Intent intent = getIntent();
        itemId = intent.getStringExtra("ITEM_ID");
        String itemName = intent.getStringExtra("ITEM_NAME");
        String itemPrice = intent.getStringExtra("ITEM_PRICE");
        String itemDescription = intent.getStringExtra("ITEM_DESCRIPTION");
        String itemCondition = intent.getStringExtra("ITEM_CONDITION");
        String itemImageUrl = intent.getStringExtra("ITEM_IMAGE_URL");
        String userName = intent.getStringExtra("USER_NAME");

        // Initialize views
        TextView nameView = findViewById(R.id.item_name);
        TextView priceView = findViewById(R.id.item_price);
        TextView descriptionView = findViewById(R.id.item_description);
        TextView conditionView = findViewById(R.id.item_condition);
        ImageView imageView = findViewById(R.id.item_image);
        Button swapButton = findViewById(R.id.swap_button);

        // Set values to views
        nameView.setText(itemName);
        priceView.setText(itemPrice);
        descriptionView.setText(itemDescription);
        conditionView.setText(itemCondition);
        Glide.with(this).load(itemImageUrl).into(imageView);

        // Set up swap button to navigate to SwapActivity
        swapButton.setOnClickListener(v -> {
            Intent swapIntent = new Intent(DetailActivity.this, SwapActivity.class);

            // Pass the item details to SwapActivity
            swapIntent.putExtra("PREVIOUS_ITEM_IMAGE_URL", itemImageUrl);
            swapIntent.putExtra("PREVIOUS_ITEM_NAME", itemName);
            swapIntent.putExtra("PREVIOUS_ITEM_PRICE", itemPrice);
            swapIntent.putExtra("PREVIOUS_ITEM_DESCRIPTION", itemDescription);
            swapIntent.putExtra("PREVIOUS_ITEM_CONDITION", itemCondition);

            startActivity(swapIntent);
        });


        Button buyButton = findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent purchaseIntent = new Intent(DetailActivity.this, PurchaseActivity.class);

                // Pass the item details to PurchaseActivity
                purchaseIntent.putExtra("ITEM_IMAGE_URL", itemImageUrl);
                purchaseIntent.putExtra("ITEM_NAME", itemName);
                purchaseIntent.putExtra("ITEM_PRICE", itemPrice);
                purchaseIntent.putExtra("ITEM_DESCRIPTION", itemDescription);
                purchaseIntent.putExtra("ITEM_CONDITION", itemCondition);

                startActivity(purchaseIntent);
            }
        });
    }
}
