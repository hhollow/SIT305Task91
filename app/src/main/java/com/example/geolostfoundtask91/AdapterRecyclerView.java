package com.example.geolostfoundtask91;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.RvViewHolder> {
    private OnItemClickListener cardClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        cardClickListener = listener;
    }


    private Context context;
    private ArrayList item_Id;
    private ArrayList item_Type;
    private ArrayList item_Name;
    private ArrayList item_Phone;
    private ArrayList item_Description;
    private ArrayList item_Date;
    private ArrayList item_Location;

    // constructor
    public AdapterRecyclerView(Context context,ArrayList item_Id, ArrayList item_Type, ArrayList item_Name, ArrayList item_Phone, ArrayList item_Description, ArrayList item_Date, ArrayList item_Location) {
        this.context = context;
        this.item_Id = item_Id;
        this.item_Type = item_Type;
        this.item_Name = item_Name;
        this.item_Phone = item_Phone;
        this.item_Description = item_Description;
        this.item_Date = item_Date;
        this.item_Location = item_Location;
    }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card,parent,false);
        return new RvViewHolder(v, cardClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {

        holder.item_Name.setText(String.valueOf(item_Name.get(position)));
        holder.item_Type.setText(String.valueOf(item_Type.get(position)));
        holder.item_Phone.setText(String.valueOf(item_Phone.get(position)));
        holder.item_Description.setText(String.valueOf(item_Description.get(position)));
        holder.item_Date.setText(String.valueOf(item_Date.get(position)));
        holder.item_Location.setText(String.valueOf(item_Location.get(position)));
    }

    // determines the number of items to show in the view
    @Override
    public int getItemCount() {
        return item_Name.size();
    }

    // this method controls the display
    public class RvViewHolder extends RecyclerView.ViewHolder {
        TextView item_Type,  item_Name, item_Phone, item_Description, item_Date,  item_Location;
        public RvViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            item_Type = itemView.findViewById(R.id.cardType);
            item_Name = itemView.findViewById(R.id.cardName);
            item_Phone = itemView.findViewById(R.id.cardPhone);
            item_Description = itemView.findViewById(R.id.cardDescription);
            item_Date = itemView.findViewById(R.id.cardDate);
            item_Location = itemView.findViewById(R.id.cardLocation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
