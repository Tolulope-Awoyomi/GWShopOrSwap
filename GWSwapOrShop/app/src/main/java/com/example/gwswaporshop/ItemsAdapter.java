package com.example.gwswaporshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import android.content.Intent;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private Context mContext;
    private List<Item> mItemList;
    private LayoutInflater mInflater;

    public ItemsAdapter(Context context, List<Item> itemList) {
        mContext = context;
        mItemList = itemList;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = mItemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView;
        TextView itemDescriptionView;
        TextView itemPriceView;
        ImageView imageView;
        TextView itemConditionView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.item_name_view);
            itemDescriptionView = itemView.findViewById(R.id.item_description_view);
            itemPriceView = itemView.findViewById(R.id.item_price_view);
            imageView = itemView.findViewById(R.id.item_image_view);
            itemConditionView = itemView.findViewById(R.id.item_condition_view);

            imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Item item = mItemList.get(position);
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("ITEM_ID", item.getId());
                    intent.putExtra("ITEM_NAME", item.getName());
                    intent.putExtra("ITEM_PRICE", item.getPrice());
                    intent.putExtra("ITEM_DESCRIPTION", item.getDescription());
                    intent.putExtra("ITEM_CONDITION", item.getCondition());
                    intent.putExtra("ITEM_IMAGE_URL", item.getImageUrl());
                    intent.putExtra("USER_NAME", "Username Here");
                    mContext.startActivity(intent);
                }
            });
        }

        public void bind(Item item) {
            itemNameView.setText(item.getName());
            itemDescriptionView.setText(item.getDescription());
            itemPriceView.setText(item.getPrice());
            itemConditionView.setText(item.getCondition());

            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                Glide.with(mContext).load(item.getImageUrl()).into(imageView);
            }
        }
    }
}
