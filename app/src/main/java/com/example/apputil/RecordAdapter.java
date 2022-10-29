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

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    Context context;
    ArrayList<Record> records;

    public RecordAdapter(Context context, ArrayList<Record> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordViewHolder(LayoutInflater.from(context).inflate(R.layout.record_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.name.setText(records.get(position).getName());
        holder.date.setText(records.get(position).getDate());
        holder.age.setText(records.get(position).getAge());
        holder.problem.setText(records.get(position).getProblem());
        holder.medicines.setText(records.get(position).getMedicines());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }
    public static class RecordViewHolder extends RecyclerView.ViewHolder{

        TextView name,date,age,problem,medicines;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.record_name);
            date=itemView.findViewById(R.id.record_date);
            age=itemView.findViewById(R.id.record_age);
            problem=itemView.findViewById(R.id.record_problem);
            medicines=itemView.findViewById(R.id.record_medicines);
        }
    }
}
