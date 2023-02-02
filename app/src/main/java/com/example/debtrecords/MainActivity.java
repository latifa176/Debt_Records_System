package com.example.debtrecords;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int lastExpandedPosition = -1;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<RecordItem> expandableRecordItems;
    HashMap<RecordItem, List<AmountChange>> expandableRecordItemHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = (ExpandableListView) findViewById(R.id.recordRecyclerView);
        expandableRecordItemHistory = ExpandableListDataPump.getData();
        expandableRecordItems = new ArrayList<RecordItem>(expandableRecordItemHistory.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableRecordItems, expandableRecordItemHistory);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                //Collapse all groups except selected group:
                if(lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
                {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableRecordItems.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableRecordItems.get(groupPosition)
                                + " -> "
                                + expandableRecordItemHistory.get(
                                expandableRecordItems.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }
}