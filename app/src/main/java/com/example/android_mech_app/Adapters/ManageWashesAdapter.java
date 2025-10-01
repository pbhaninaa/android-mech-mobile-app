package com.example.android_mech_app.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_mech_app.Models.CarWashBooking;
import com.example.android_mech_app.R;

import java.util.List;

public class ManageWashesAdapter extends RecyclerView.Adapter<ManageWashesAdapter.WashViewHolder> {

    private List<CarWashBooking> bookings;

    public ManageWashesAdapter(List<CarWashBooking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public WashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_wash, parent, false);
        return new WashViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WashViewHolder holder, int position) {
        CarWashBooking booking = bookings.get(position);

        holder.txtClientUsername.setText("Client: " + booking.getClientUsername());
        holder.txtCarDetails.setText(booking.getCarType() + " - " + booking.getCarPlate());
        holder.txtServices.setText("Services: " + String.join(", ", booking.getServiceTypes()));
        holder.txtStatus.setText("Status: " + booking.getStatus());

        // Conditional buttons
        if (booking.getStatus().equalsIgnoreCase("Pending")) {
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnStart.setVisibility(View.GONE);
            holder.btnDone.setVisibility(View.GONE);
        } else {
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnStart.setVisibility(View.VISIBLE);
            holder.btnDone.setVisibility(View.VISIBLE);

            // Enable/disable based on status
            holder.btnStart.setEnabled(booking.getStatus().equalsIgnoreCase("Paid"));
            holder.btnDone.setEnabled(booking.getStatus().equalsIgnoreCase("In Progress"));
        }

        // Button listeners
        holder.btnAccept.setOnClickListener(v -> {
            booking.setStatus("Paid"); // Accepting moves to Paid
            notifyItemChanged(position);
        });

        holder.btnStart.setOnClickListener(v -> {
            booking.setStatus("In Progress");
            notifyItemChanged(position);
        });

        holder.btnDone.setOnClickListener(v -> {
            booking.setStatus("Completed");
            notifyItemChanged(position);
        });
    }


    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class WashViewHolder extends RecyclerView.ViewHolder {
        TextView txtClientUsername, txtCarDetails, txtServices, txtStatus;
        Button btnStart, btnDone,btnAccept;

        public WashViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClientUsername = itemView.findViewById(R.id.txtClientUsername);
            txtCarDetails = itemView.findViewById(R.id.txtCarDetails);
            txtServices = itemView.findViewById(R.id.txtServices);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnStart = itemView.findViewById(R.id.btnStart);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }
}
