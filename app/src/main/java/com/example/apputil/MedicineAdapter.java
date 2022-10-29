package com.example.apputil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    Context context;
    ArrayList<Medicine> medicines;

    public MedicineAdapter(Context context, ArrayList<Medicine> medicines) {
        this.context = context;
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedicineViewHolder(LayoutInflater.from(context).inflate(R.layout.medicine_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        holder.name.setText(medicines.get(position).getName());
        holder.id.setText(medicines.get(position).getMedicineId());
        holder.quantity.setText(medicines.get(position).getQuantity());
        holder.expiryDate.setText(medicines.get(position).getExpiryDate());
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }
    public static class MedicineViewHolder extends RecyclerView.ViewHolder{

        TextView name,id,quantity,expiryDate;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.medicine_name);
            id=itemView.findViewById(R.id.medicine_id);
            quantity=itemView.findViewById(R.id.medicine_quantity);
            expiryDate=itemView.findViewById(R.id.medicine_expiry_date);
        }
    }
}
