package com.wuba.view.imkeyboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wuba.view.R;

import java.util.ArrayList;

/**
 * desc :
 * date : 2018/7/10
 *
 * @author : dongSen
 */
public class ImAdapter extends RecyclerView.Adapter<ImAdapter.ViewHolder> {

    private ArrayList<String> data = new ArrayList<>();

    public ImAdapter() {
        for (int i = 0; i < 100; i++) {
            data.add("test" + i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_im_test, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_im_test);
        }
    }
}
