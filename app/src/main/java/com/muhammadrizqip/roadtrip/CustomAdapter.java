package com.muhammadrizqip.roadtrip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<viewHolder> {
    listWisata listWisata;
    List<model> modelList;

    public CustomAdapter(com.muhammadrizqip.roadtrip.listWisata listWisata, List<model> modelList) {
        this.listWisata = listWisata;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_layout, parent, false);

        viewHolder viewHolder = new viewHolder(itemView);
        //handle item clicks here
        viewHolder.setOnClickListener(new viewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //this will be called when user click item

                // show data in toast on clicking
                String wisata = modelList.get(position).getWisata();
                Integer biaya = modelList.get(position).getBiaya();
                Toast.makeText(listWisata, wisata+"\n"+biaya, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                //this will be called when user long  click item

                // creating alert dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(listWisata);
                AlertDialog.Builder builder = new AlertDialog.Builder(listWisata);
                //options to display in dialog
                String[] options = {"Update", "Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public     void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //UPDATE IS CLICKED
                            String id = modelList.get(position).getId();
                            String ws = modelList.get(position).getWisata();
                            String by = String.valueOf(modelList.get(position).getBiaya());
                            // INTENT TO START ACTIVITY
                            Intent intent = new Intent(listWisata, WisataActivity.class);
                            // put data in intent
                            intent.putExtra("pid", id);
                            intent.putExtra("pwisata", ws);
                            intent.putExtra("pbiaya", by);
                            listWisata.startActivity(intent);
                        }
                        if (which == 1){
                            listWisata.hapusList(position);
                        }
                    }
                }).create().show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        // bind views/ set data
        holder.mwisata.setText(modelList.get(position).getWisata());
        holder.mbiaya.setText(String.valueOf(modelList.get(position).getBiaya()));

    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
