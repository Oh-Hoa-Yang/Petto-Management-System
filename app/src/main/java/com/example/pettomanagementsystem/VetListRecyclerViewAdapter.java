package com.example.pettomanagementsystem;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VetListRecyclerViewAdapter extends RecyclerView.Adapter<VetListRecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<VetModel> vetList;
    VetListInterface vetListInterface;

    public VetListRecyclerViewAdapter(Context context, ArrayList<VetModel> vetList, VetListInterface vetListInterface) {
        this.context = context;
        this.vetList = vetList;
        this.vetListInterface = vetListInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_single_row_vet, parent, false);
        return new MyViewHolder(view, vetListInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(vetList.get(position).getName());
        // Create Location objects for user and clinic
        Location userLocation = new Location("");
        userLocation.setLatitude(vetListInterface.getLatitude());
        userLocation.setLongitude(vetListInterface.getLongitude());

        Location clinicLocation = new Location("");
        clinicLocation.setLatitude(vetList.get(position).getLatitude());
        clinicLocation.setLongitude(vetList.get(position).getLongitude());

        // Calculate the distance
        float distanceInMeters = userLocation.distanceTo(clinicLocation);
        float distanceInKm = distanceInMeters / 1000;

        // Display the distance
        holder.distance.setText(String.format("%.2f km", distanceInKm));
    }

    @Override
    public int getItemCount() {
        return vetList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, distance;
        public MyViewHolder(@NonNull View itemView, VetListInterface vetListInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewVetName);
            distance = itemView.findViewById(R.id.textViewDistance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vetListInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            vetListInterface.onItemClicked(position);
                        }
                    }
                }
            });
        }
    }
}
