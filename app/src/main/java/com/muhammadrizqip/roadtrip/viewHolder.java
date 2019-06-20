package com.muhammadrizqip.roadtrip;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class viewHolder extends RecyclerView.ViewHolder {

    TextView mwisata, mbiaya;
    View mView;
    public viewHolder(@NonNull View itemView) {
        super(itemView);
        mView= itemView;


        // item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
        // item long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
        // initialize views with model_layout.xml
        mwisata= itemView.findViewById(R.id.rwisata);
        mbiaya= itemView.findViewById(R.id.rbiaya);
    }

    private viewHolder.ClickListener mClickListener;
    // interface for click listener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }
    public void setOnClickListener(viewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
