package com.wuba.view.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuba.view.R;

import java.util.ArrayList;

/**
 * desc :
 * date : 2019/1/11
 *
 * @author : dongSen
 */
public class ItemAnimationAdapter extends RecyclerView.Adapter<ItemAnimationAdapter.ViewHolder> {

    private static final String TAG = "Adapter";

    private ArrayList<String> listData = new ArrayList<>();
    private int index;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder:  position = " + i);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_simple, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder:  position = " + i);
        viewHolder.textView.setText(listData.get(i));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void init() {
        for (int i = 0; i < 40; i++) {
            listData.add(String.valueOf(i));
            index++;
        }
    }

    public void add() {
        listData.add(String.valueOf(index));
        notifyItemInserted(index);
        index++;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view_item_animation);
        }

    }
}
