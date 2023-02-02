package com.example.debtrecords;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<RecordItem> expandableRecordItem;
    private HashMap<RecordItem, List<AmountChange>> expandableRecordItemHistory;

    public CustomExpandableListAdapter(Context context, List<RecordItem> expandableRecordItem,
                                       HashMap<RecordItem, List<AmountChange>> expandableRecordItemHistory) {
        this.context = context;
        this.expandableRecordItem = expandableRecordItem;
        this.expandableRecordItemHistory = expandableRecordItemHistory;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableRecordItemHistory.get(this.expandableRecordItem.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final AmountChange expandedListHistory = (AmountChange) getChild(listPosition, expandedListPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.record_item_change_history_container, null);
        }
        RelativeLayout expandedListRelativeLayout = (RelativeLayout) convertView
                .findViewById(R.id.expandedListItem);

        ((TextView) expandedListRelativeLayout.findViewById(R.id.changeType)).setText(expandedListHistory.getAmountChangeTypeString());
        ((TextView) expandedListRelativeLayout.findViewById(R.id.changeAmount)).setText(expandedListHistory.getChangeAmountString());
        ((TextView) expandedListRelativeLayout.findViewById(R.id.changeDateAndTime)).setText(expandedListHistory.getFormattedDateChanged());

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableRecordItemHistory.get(this.expandableRecordItem.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableRecordItem.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableRecordItem.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent)
    {
        RecordItem recordItem = (RecordItem) getGroup(listPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.record_item_container, null);
        }
        RelativeLayout listTitleRelativeLayout = (RelativeLayout) convertView
                .findViewById(R.id.recordItem);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}