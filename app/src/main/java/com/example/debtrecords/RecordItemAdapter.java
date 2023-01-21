package com.example.debtrecords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordItemAdapter extends RecyclerView.Adapter<RecordItemAdapter.RecordItemViewHolder>
{
    private List<RecordItem> recordItems;

    public RecordItemAdapter(List<RecordItem> recordItems) {
        this.recordItems = recordItems;
    }

    @NonNull
    @Override
    public RecordItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new RecordItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.record_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecordItemViewHolder holder, int position)
    {
        holder.name.setText(recordItems.get(position).getNameOfDebtor());
        holder.section.setText(recordItems.get(position).getSectionOfDebtor()+" "+recordItems.get(position).getSectionNumOfDebtor());
        holder.totalAmount.setText(Float.toString(recordItems.get(position).getTotalAmount()));
        holder.dateAndTime.setText(recordItems.get(position).getFormattedDateCreated());
        holder.changeHistory.setAdapter(new RecordItemChangeHistoryAdapter(recordItems.get(position).getChangeHistory()));
        holder.changeHistory.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return recordItems.size();
    }

    class RecordItemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name, section, totalAmount, dateAndTime;
        private RecyclerView changeHistory;

        public RecordItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            section=itemView.findViewById(R.id.section);
            totalAmount=itemView.findViewById(R.id.totalAmount);
            dateAndTime=itemView.findViewById(R.id.dateAndTime);
            changeHistory=itemView.findViewById((R.id.changeHistoryRecyclerView));
        }
    }
}
