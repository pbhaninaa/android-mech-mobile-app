package com.example.android_mech_app.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_mech_app.Models.MechanicRequest;
import com.example.android_mech_app.R;
import com.example.android_mech_app.Role;

import java.util.List;

public class JobRequestsAdapter extends RecyclerView.Adapter<JobRequestsAdapter.RequestViewHolder> {

    private final List<MechanicRequest> requests;
    private final Role role;

    public JobRequestsAdapter(List<MechanicRequest> requests, Role role) {
        this.requests = requests;
        this.role = role;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job_request, parent, false);
        return new RequestViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        MechanicRequest request = requests.get(position);

        holder.txtUsername.setText("Client: " + request.getUsername());
        holder.txtDescription.setText("Job: " + request.getDescription());
        holder.txtLocation.setText("Location: " + request.getLocation());
        holder.txtDate.setText("Date: " + request.getDate());
        holder.txtStatus.setText("Status: " + request.getStatus());

        // Show/hide buttons based on role
        if (role == Role.MECHANIC) {
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnPay.setVisibility(View.GONE);
        } else if (role == Role.CLIENT) {
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnPay.setVisibility(View.VISIBLE);
        }

        holder.btnAccept.setOnClickListener(v -> {
            request.setStatus("accepted");
            notifyItemChanged(position);
        });

        holder.btnPay.setOnClickListener(v -> {
            request.setStatus("paid"); // or your desired status
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtDescription, txtLocation, txtDate, txtStatus;
        Button btnAccept, btnPay;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnPay = itemView.findViewById(R.id.btnPay);
        }
    }
}
