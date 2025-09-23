package com.example.android_mech_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_mech_app.Models.Earning;
import com.example.android_mech_app.R;

import java.util.List;

public class EarningsAdapter extends RecyclerView.Adapter<EarningsAdapter.EarningViewHolder> {

    private List<Earning> earnings;

    public EarningsAdapter(List<Earning> earnings) {
        this.earnings = earnings;
    }

    @NonNull
    @Override
    public EarningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earnings_item, parent, false);
        return new EarningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarningViewHolder holder, int position) {
        Earning earning = earnings.get(position);
        holder.txtClientUsername.setText("Client: " + earning.getClientUsername());
        holder.txtJobDescription.setText("Job: " + earning.getJobDescription());
        holder.txtServicePrice.setText("Amount: $" + earning.getAmount());
        holder.txtPaidAt.setText("Paid At: " + earning.getPaidAt());
//        holder.txtPlatformFee.setText("Platform Fee: $" + earning.getPlatformFee());
    }


    @Override
    public int getItemCount() {
        return earnings.size();
    }

    static class EarningViewHolder extends RecyclerView.ViewHolder {
        TextView txtClientUsername, txtCarDetails, txtServicePrice, txtPaidAt, txtJobDescription;

        public EarningViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClientUsername = itemView.findViewById(R.id.txtClientUsername);
            txtCarDetails = itemView.findViewById(R.id.txtCarDetails);
            txtServicePrice = itemView.findViewById(R.id.txtServicePrice);
            txtPaidAt = itemView.findViewById(R.id.txtPaidAt);
            txtJobDescription = itemView.findViewById(R.id.txtJobDescription);
        }
    }
}
