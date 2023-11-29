package com.example.debtrecords;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.TypedValue;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private View currentlyExpandedRecord;
    private DrawerLayout menuDrawerLayout;
    private ActionBarDrawerToggle menuToggle;
    private RecyclerView recordRecyclerView;
    private TextView noRecordItemTextView;
    private static int defaultRecordItemHeightInDp = 100;
    private static int editAmountContainerHeightInDp = 120;
    private static int defaultRecordItemHeight, editAmountContainerHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noRecordItemTextView = findViewById(R.id.noRecordItemTextView);

        defaultRecordItemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultRecordItemHeightInDp, getResources().getDisplayMetrics());
        editAmountContainerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, editAmountContainerHeightInDp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeRecyclerView();
        initializeDrawerLayout();
    }

    void initializeRecyclerView()
    {
        recordRecyclerView = findViewById(R.id.recordRecyclerView);

        List<RecordItem> recordItems = generateListOfAllRecordItems();
        if(recordItems.size()==0) //No record item stored yet
        {
            noRecordItemTextView.setText(R.string.no_record_item_stored);
            return;
        }

        noRecordItemTextView.setText("");
        recordRecyclerView.setAdapter(new RecordItemAdapter(recordItems, getResources().getColorStateList(R.color.record_BG_green), getResources().getColorStateList(R.color.dark_green)));
    }
    List<RecordItem> generateListOfAllRecordItems()
    {
        List<RecordItem> storedRecordItems = new ArrayList<>();

        File directory = getApplicationContext().getFilesDir();
        File debtsFolder = new File(directory, "@string/ongoing_debts_folder");
        debtsFolder.mkdirs();

        try {
            File[] recordFiles = debtsFolder.listFiles();
            int numOfStoredRecords = recordFiles.length;

            for (int i = 0; i < numOfStoredRecords; i++) {
                //Step 1: read the content of the record file
                String content = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(recordFiles[i])));
                int character = reader.read();

                while (character != -1) {
                    content = content + Character.toString((char) character);
                    character = reader.read();
                }

                //Step 2: create the RecordItem object using the content
                String[] dataSegments = content.split(",");
                String name = dataSegments[0];
                String dateCreated = dataSegments[1];
                String section = dataSegments[2];
                String sectionNum = dataSegments[3];
                String recordType = dataSegments[4];
                String amount = dataSegments[5];

                RecordItem newRecord = new RecordItem(name, LocalDateTime.parse(dateCreated), Float.parseFloat(amount),
                        DebtorSection.getEnumWithValueOf(section), DebtorSectionNumber.getEnumWithValueOf(sectionNum), AmountType.getEnumWithValueOf(recordType));

                //Step 3: add the record to the list
                storedRecordItems.add(newRecord);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return storedRecordItems;
    }
    void initializeDrawerLayout()
    {
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        menuToggle = new ActionBarDrawerToggle(this, menuDrawerLayout, R.string.open, R.string.close);

        menuDrawerLayout.addDrawerListener(menuToggle);
        menuToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(menuToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void onRecordItemClick(View view)
    {
        if(view.findViewById(R.id.changeHistoryRecyclerView).getVisibility()==View.INVISIBLE)
            expandRecord(view);
        else shrinkRecord(view);
    }
    public void onNewRecordClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, NewRecordActivity.class);
        startActivity(intent);
    }
    public void onEditAmountClick(View view)
    {
        //First: animate the record item height
        View recordContainer = currentlyExpandedRecord.findViewById(R.id.recordItem);
        animateContainerHeight(recordContainer, recordContainer.getHeight(), defaultRecordItemHeight + editAmountContainerHeight, 300);

        //Second: hide the change history list
        currentlyExpandedRecord.findViewById(R.id.changeHistoryRecyclerView).setVisibility(View.INVISIBLE);

        //Third: change the edit button image
        ImageButton editButton = currentlyExpandedRecord.findViewById(R.id.editAmountButton);
        editButton.setImageResource(R.drawable.btn_edit_pressed_cropped);

        //Finally: show the edit amount container
        currentlyExpandedRecord.findViewById(R.id.editAmountContainer).setVisibility(View.VISIBLE);
    }
    public void onCancelAmountChangeClick(View view)
    {
        //First: hide the edit amount container
        currentlyExpandedRecord.findViewById(R.id.editAmountContainer).setVisibility(View.GONE);

        //Second: show the change history list
        currentlyExpandedRecord.findViewById(R.id.changeHistoryRecyclerView).setVisibility(View.VISIBLE);

        //Third: change the edit button image
        ImageButton editButton = currentlyExpandedRecord.findViewById(R.id.editAmountButton);
        editButton.setImageResource(R.drawable.btn_edit_cropped);

        //Finally: re-animate the record item height
        View recordContainer = currentlyExpandedRecord.findViewById(R.id.recordItem);
        animateContainerHeight(recordContainer, recordContainer.getHeight(), ViewGroup.LayoutParams.WRAP_CONTENT, 300);
    }

    void expandRecord(View view)
    {
        if(currentlyExpandedRecord!=null) //if a different record was currently expanded, shrink it
            shrinkRecord(currentlyExpandedRecord);
        view.findViewById(R.id.changeHistoryRecyclerView).setVisibility(View.VISIBLE);
        View editAmountButton = view.findViewById(R.id.editAmountButton);
        editAmountButton.setVisibility(View.VISIBLE);
        editAmountButton.setClickable(true);

        currentlyExpandedRecord = view;

        View recordContainer = currentlyExpandedRecord.findViewById(R.id.recordItem);
        animateContainerHeight(recordContainer, recordContainer.getHeight(), ViewGroup.LayoutParams.WRAP_CONTENT, 300);
    }
    void shrinkRecord(View view)
    {
        View recordContainer = currentlyExpandedRecord.findViewById(R.id.recordItem);
        animateContainerHeight(recordContainer, recordContainer.getHeight(), defaultRecordItemHeight, 300);

        //Hide change history list
        view.findViewById(R.id.changeHistoryRecyclerView).setVisibility(View.INVISIBLE);
        //Hide edit amount button & container
        View editAmountButton = view.findViewById(R.id.editAmountButton);
        ((ImageButton)editAmountButton).setImageResource(R.drawable.btn_edit_cropped);
        editAmountButton.setVisibility(View.INVISIBLE);
        editAmountButton.setClickable(false);
        view.findViewById(R.id.editAmountContainer).setVisibility(View.GONE);

        currentlyExpandedRecord=null;
    }
    void animateContainerHeight(View animatedContainer, int fromHeight, int toHeight, int duration)
    {
        ValueAnimator slideAnimator = ValueAnimator.ofInt(fromHeight, toHeight).setDuration(duration);
        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override public void onAnimationUpdate(ValueAnimator animator){
                Integer value = (Integer) animator.getAnimatedValue();
                animatedContainer.getLayoutParams().height = value.intValue();
                animatedContainer.requestLayout();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(slideAnimator);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }
}