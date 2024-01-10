package com.example.debtrecords;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private List<RecordItem> recordItems;
    private View currentlyExpandedRecord;
    private DrawerLayout menuDrawerLayout;
    private ActionBarDrawerToggle menuToggle;
    private RecyclerView recordRecyclerView;
    private TextView noRecordItemTextView;
    private static int defaultRecordItemHeightInDp = 100;
    private static int editAmountContainerHeightInDp = 120;
    private static int defaultRecordItemHeight, editAmountContainerHeight;
    private ColorStateList originalBackgroundTint, originalTextColor, redBackgroundTint, redTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noRecordItemTextView = findViewById(R.id.noRecordItemTextView);

        defaultRecordItemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultRecordItemHeightInDp, getResources().getDisplayMetrics());
        editAmountContainerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, editAmountContainerHeightInDp, getResources().getDisplayMetrics());
        initializeColorVariables();
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

        recordItems = generateListOfAllRecordItems();
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
                String[] dataSegments = content.split("/");
                String name = dataSegments[0];
                String dateCreated = dataSegments[1];
                String section = dataSegments[2];
                String sectionNum = dataSegments[3];
                String recordType = dataSegments[4];
                String totalAmount = dataSegments[5];
                String amountChangeHistory = "";
                for(int j=6; j<dataSegments.length; j++) //loop through the rest of data segments that represent the amountChangeHistory change history:
                {
                    amountChangeHistory += dataSegments[j];
                }

                RecordItem newRecord = new RecordItem(name, LocalDateTime.parse(dateCreated), Float.parseFloat(totalAmount),
                        DebtorSection.getEnumWithValueOf(section), DebtorSectionNumber.getEnumWithValueOf(sectionNum), AmountType.getEnumWithValueOf(recordType), amountChangeHistory);

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
    void initializeColorVariables()
    {
        originalBackgroundTint = getApplicationContext().getResources().getColorStateList(R.color.light_green);
        redBackgroundTint = getApplicationContext().getResources().getColorStateList(R.color.red);
        originalTextColor = getApplicationContext().getResources().getColorStateList(R.color.dark_green);
        redTextColor = getApplicationContext().getResources().getColorStateList(R.color.red);
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

        //Finally: show the edit amount container and initialize the spinner
        currentlyExpandedRecord.findViewById(R.id.editAmountContainer).setVisibility(View.VISIBLE);
        Spinner typeOfChangeSpinner = currentlyExpandedRecord.findViewById(R.id.typeOfChangeSpinner);
        ArrayAdapter<AmountType> typeOfChangeSpinnerAdaptor = new ArrayAdapter<AmountType>
                (this,
                        R.layout.spinner_item,
                        AmountType.values()
                );
        typeOfChangeSpinnerAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfChangeSpinner.setAdapter(typeOfChangeSpinnerAdaptor);
    }
    public void onCancelAmountChangeClick(View view)
    {
        //First: change the amount field color back to normal and empty the field
        emphasizeChangeAmountField(false);
        emptyChangeAmountField();

        //Second: hide the edit amount container
        currentlyExpandedRecord.findViewById(R.id.editAmountContainer).setVisibility(View.GONE);

        //Third: show the change history list
        currentlyExpandedRecord.findViewById(R.id.changeHistoryRecyclerView).setVisibility(View.VISIBLE);

        //Forth: change the edit button image
        ImageButton editButton = currentlyExpandedRecord.findViewById(R.id.editAmountButton);
        editButton.setImageResource(R.drawable.btn_edit_cropped);

        //Finally: re-animate the record item height
        View recordContainer = currentlyExpandedRecord.findViewById(R.id.recordItem);
        animateContainerHeight(recordContainer, recordContainer.getHeight(), ViewGroup.LayoutParams.WRAP_CONTENT, 300);
    }
    public void onSaveAmountChangeClick(View view)
    {
        //First: validate the input
        EditText amountChangeEditText = currentlyExpandedRecord.findViewById(R.id.amountChangeEditText);
        if(amountChangeEditText.getText().toString().isEmpty() || Float.parseFloat(amountChangeEditText.getText().toString()) == 0)
        {
            emphasizeChangeAmountField(true);
            return;
        }

        //Input is valid, proceed:
        //Second: save the amount change
        String dateSaved = LocalDateTime.now().toString();
        String amount = ((TextView) currentlyExpandedRecord.findViewById(R.id.amountChangeEditText)).getText().toString();
        String changeType = ((Spinner) currentlyExpandedRecord.findViewById(R.id.typeOfChangeSpinner)).getSelectedItem().toString();
        String nameOfDebtor = ((TextView) currentlyExpandedRecord.findViewById(R.id.name)).getText().toString();
        int indexOfCorrespondingRecordItem = updateCorrespondingRecordItem(nameOfDebtor, dateSaved, amount, changeType);
        writeAmountChangeToFile(dateSaved, amount, changeType, indexOfCorrespondingRecordItem);

        //Third: change the amount field color back to normal and empty the field
        emphasizeChangeAmountField(false);
        emptyChangeAmountField();

        //Forth: hide the edit amount container and show the change history list
        currentlyExpandedRecord.findViewById(R.id.editAmountContainer).setVisibility(View.GONE);
        currentlyExpandedRecord.findViewById(R.id.changeHistoryRecyclerView).setVisibility(View.VISIBLE);

        //Fifth: change the edit button image
        ImageButton editButton = currentlyExpandedRecord.findViewById(R.id.editAmountButton);
        editButton.setImageResource(R.drawable.btn_edit_cropped);

        //Finally: re-animate the record item height
        View recordContainer = currentlyExpandedRecord.findViewById(R.id.recordItem);
        animateContainerHeight(recordContainer, recordContainer.getHeight(), ViewGroup.LayoutParams.WRAP_CONTENT, 300);
    }
    void writeAmountChangeToFile(String date, String amount, String changeType, int indexOfRecordItem)
    {
        File directory = getApplicationContext().getFilesDir();
        File debtsFolder = new File(directory, "@string/ongoing_debts_folder");
        File targetFile = new File(debtsFolder, ((TextView) currentlyExpandedRecord.findViewById(R.id.name)).getText().toString() + ".txt");

        String textToBeAppended = "/"+date+","+amount+","+changeType;

        try
        {
            FileOutputStream fosForAppending = new FileOutputStream(targetFile, true);
            fosForAppending.write(textToBeAppended.getBytes());
            fosForAppending.close();

            //*** Rewriting the TOTAL AMOUNT in data file ***
            //1) Splitting the data content string of this file
            String content = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile)));
            int character = br.read();
            while(character != -1)
            {
                content += (char) character;
                character = br.read();
            }
            String[] thisFileDataSegments = content.split("/");
            String textBeforeTotalAmount = thisFileDataSegments[0]+"/"+thisFileDataSegments[1]+"/"+thisFileDataSegments[2]+"/"+thisFileDataSegments[3]+"/"+thisFileDataSegments[4]+"/";
            String newTotalAmount = "" + recordItems.get(indexOfRecordItem).getTotalAmount();
            String textAfterTotalAmount = "";
            for(int i=6; i<thisFileDataSegments.length; i++) //loop through the rest of the segments to produce the text after total amount
            {
                textAfterTotalAmount += "/"+thisFileDataSegments[i];
            }
            //2) Merging the produced string variables
            String mergedNewContent = textBeforeTotalAmount+newTotalAmount+textAfterTotalAmount;
            FileOutputStream fosForRewriting = new FileOutputStream(targetFile);
            fosForRewriting.write(mergedNewContent.getBytes());

            fosForRewriting.close();


            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){}
    }
    int updateCorrespondingRecordItem(String name, String date, String amount, String changeType)
    {
        //First find the corresponding one:
        int indexOfCorrespondingItem = -1;
        for(int i=0; i<recordItems.size(); i++)
        {
            if(recordItems.get(i).getNameOfDebtor() == name)
            {
                indexOfCorrespondingItem = i;
                break;
            }
        }
        //Then update it:
        if(indexOfCorrespondingItem>-1)
        {
            recordItems.get(indexOfCorrespondingItem).addNewAmountChange(LocalDateTime.parse(date), Float.parseFloat(amount), AmountType.getEnumWithValueOf(changeType));
        }
        //If not found?
        else
        {
            Toast.makeText(MainActivity.this, "Error: Corresponding record item was not found!", Toast.LENGTH_LONG).show();
        }
        return indexOfCorrespondingItem;
    }
    void emphasizeChangeAmountField(boolean isEmphasized)
    {
        EditText amountChangeEditText = currentlyExpandedRecord.findViewById(R.id.amountChangeEditText);
        TextView amountChangeTextView = currentlyExpandedRecord.findViewById(R.id.amountChangeTextView);

        if(isEmphasized)
        {
            amountChangeEditText.setBackgroundTintList(redBackgroundTint);
            amountChangeTextView.setTextColor(redTextColor);
        }
        else
        {
            amountChangeEditText.setBackgroundTintList(originalBackgroundTint);
            amountChangeTextView.setTextColor(originalTextColor);
        }
    }
    void emptyChangeAmountField()
    {
        EditText amountChangeEditText = currentlyExpandedRecord.findViewById(R.id.amountChangeEditText);
        amountChangeEditText.setText("");
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
        //If edit amount container was expanded, make sure the amount field is not red and empty
        if(currentlyExpandedRecord.findViewById(R.id.amountChangeEditText).getVisibility() == View.VISIBLE) {
            emphasizeChangeAmountField(false);
            emptyChangeAmountField();
        }
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