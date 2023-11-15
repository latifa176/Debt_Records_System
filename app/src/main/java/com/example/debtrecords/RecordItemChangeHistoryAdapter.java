package com.example.debtrecords;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordItemChangeHistoryAdapter extends RecyclerView.Adapter<RecordItemChangeHistoryAdapter.RecordItemChangeHistoryViewHolder>
{
    private List<AmountChange> changeHistory;
    private ColorStateList greenTextColor;

    public RecordItemChangeHistoryAdapter(List<AmountChange> changeHistory, ColorStateList greenTxt)
    {
        this.changeHistory = changeHistory;
        greenTextColor = greenTxt;
    }

    @NonNull
    @Override
    public RecordItemChangeHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new RecordItemChangeHistoryViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.record_item_change_history_container,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecordItemChangeHistoryViewHolder holder, int position)
    {
        holder.changeType.setText(changeHistory.get(position).getAmountChangeTypeString());
        holder.changeAmount.setText(changeHistory.get(position).getChangeAmountString());
        holder.changeDateAndTime.setText(changeHistory.get(position).getFormattedDateChanged());

        if(changeHistory.get(position).getChangeType() == AmountType.Credit)
        {
            holder.changeAmount.setTextColor(greenTextColor);
        }
    }

    @Override
    public int getItemCount() {
        return changeHistory.size();
    }

    class RecordItemChangeHistoryViewHolder extends RecyclerView.ViewHolder
    {
        private TextView changeType, changeAmount, changeDateAndTime;

        public RecordItemChangeHistoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            changeType=itemView.findViewById((R.id.changeType));
            changeAmount=itemView.findViewById(R.id.changeAmount);
            changeDateAndTime=itemView.findViewById(R.id.changeDateAndTime);
        }
    }
}
