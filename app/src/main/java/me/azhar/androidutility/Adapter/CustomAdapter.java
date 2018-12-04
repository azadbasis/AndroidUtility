package me.azhar.androidutility.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import me.azhar.androidutility.Common.Common;
import me.azhar.androidutility.Interface.ItemClickListener;
import me.azhar.androidutility.Model.Item;
import me.azhar.androidutility.R;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    List<Item> items;
    Context context;

    int row_index = -1;

    public CustomAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.layout_item, viewGroup, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {

        holder.textView.setText(items.get(position).getName());
        if (!items.get(position).isChecked())
            holder.imageView.setImageResource(R.drawable.ic_clear_black_24dp);
        else
            holder.imageView.setImageResource(R.drawable.ic_done_black_24dp);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                row_index = position;//set row index to selected position
                Common.currentItem = items.get(position);//set current items is item selection
                notifyDataSetChanged();//made effect on recyclerview adapter
            }
        });
        //set high light color
        if (row_index == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#F8F8FA"));
            holder.textView.setTextColor(Color.parseColor("#C5C5C7"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.textView.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
