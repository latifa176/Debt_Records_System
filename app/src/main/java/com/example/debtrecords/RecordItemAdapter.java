package com.example.debtrecords;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordItemAdapter extends RecyclerView.Adapter<RecordItemAdapter.RecordItemViewHolder>
{
    private List<RecordItem> recordItems;
    private ColorStateList greenBackground, greenTextColor;

    public RecordItemAdapter(List<RecordItem> recordItems, ColorStateList greenBG, ColorStateList greenTxt) {
        this.recordItems = recordItems;

        greenBackground = greenBG;
        greenTextColor = greenTxt;
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
        holder.changeHistory.setAdapter(new RecordItemChangeHistoryAdapter(recordItems.get(position).getChangeHistory(), greenTextColor));
        holder.changeHistory.setVisibility(View.INVISIBLE);
        holder.changeAmountLayout.setVisibility(View.GONE);

        if(recordItems.get(position).getAmountType() == AmountType.Credit)
        {
            holder.backgroundLayout.setBackgroundTintList(greenBackground);
            holder.totalAmount.setTextColor(greenTextColor);
        }
    }

    @Override
    public int getItemCount() {
        return recordItems.size();
    }

    class RecordItemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name, section, totalAmount, dateAndTime;
        private RecyclerView changeHistory;
        private RelativeLayout backgroundLayout;
        private View changeAmountLayout;

        public RecordItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            section=itemView.findViewById(R.id.section);
            totalAmount=itemView.findViewById(R.id.totalAmount);
            dateAndTime=itemView.findViewById(R.id.dateAndTime);
            changeHistory=itemView.findViewById((R.id.changeHistoryRecyclerView));
            backgroundLayout=itemView.findViewById(R.id.recordItem);
            changeAmountLayout=itemView.findViewById(R.id.changeAmountLayout);
        }
    }
}
