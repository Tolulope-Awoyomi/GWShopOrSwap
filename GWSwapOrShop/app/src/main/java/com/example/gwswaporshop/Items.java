package com.example.gwswaporshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Items extends Activity {
    private RecyclerView itemsRecyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> itemList;
    private String selectedCategory = "All Items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        itemsRecyclerView = findViewById(R.id.items_recycler_view);
        itemList = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(this, itemList);
        itemsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        itemsRecyclerView.setAdapter(itemsAdapter);

        Spinner spinner = findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                fetchItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "All Items";
                fetchItems();
            }
        });

        String username = getIntent().getStringExtra("username");
        TextView welcomeText = findViewById(R.id.welcome_text);
        welcomeText.setText(username != null && !username.isEmpty() ? getString(R.string.welcome_user, username) : getString(R.string.welcome_user, "Guest"));

        TextView sellItemText = findViewById(R.id.sell_item_text);
        sellItemText.setOnClickListener(v -> {
            Intent intent = new Intent(Items.this, SellItemActivity.class);
            startActivity(intent);
        });
    }

    private void fetchItems() {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("items");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    // Filter out items not matching the selected category
                    if (item != null && (selectedCategory.equals("All Items") || item.getCategory().equals(selectedCategory))) {
                        itemList.add(item);
                    }
                }
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log error
            }
        });
    }
}
