package com.example.debtrecords;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.text.RandomStringGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class NewRecordActivity extends AppCompatActivity
{
    private TextView nameTextView, sectionTextView, sectionNumTextView, recordTypeTextView, amountTextView;
    private EditText nameEditText, amountEditText;
    private Spinner sectionSpinner, sectionNumSpinner, recordTypeSpinner;
    private ColorStateList originalBackgroundTint, redBackgroundTint;
    private ColorStateList originalTextColor, redTextColor;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);

        initializeSectionSpinner();
        initializeSectionNumSpinner();
        initializeRecordTypeSpinner();

        findViews();
        initializeColorVariables();
    }
    void initializeSectionSpinner()
    {
        sectionSpinner = findViewById(R.id.sectionSpinner);
        ArrayAdapter<DebtorSection> sectionArrayAdapter = new ArrayAdapter<DebtorSection>
                (this,
                        R.layout.spinner_item,
                        DebtorSection.values()
                );

        sectionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(sectionArrayAdapter);

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Check the selected item & disable/enable sectionNumSpinner based on it
                if(sectionSpinner.getSelectedItemPosition()==1 || sectionSpinner.getSelectedItemPosition()==2 || sectionSpinner.getSelectedItemPosition()==3)
                {
                    sectionNumSpinner.setEnabled(true);
                }
                else
                {
                    sectionNumSpinner.setSelection(0);
                    sectionNumSpinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    void initializeSectionNumSpinner()
    {
        sectionNumSpinner = findViewById(R.id.sectionNumSpinner);
        ArrayAdapter<DebtorSectionNumber> sectionNumArrayAdapter = new ArrayAdapter<DebtorSectionNumber>
                (this,
                        R.layout.spinner_item,
                        DebtorSectionNumber.values()
                );

        sectionNumArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionNumSpinner.setAdapter(sectionNumArrayAdapter);
    }
    void initializeRecordTypeSpinner()
    {
        Spinner recordTypeSpinner = findViewById(R.id.recordTypeSpinner);
        ArrayAdapter<AmountType> recordTypeArrayAdapter = new ArrayAdapter<AmountType>
                (this,
                        R.layout.spinner_item,
                        AmountType.values()
                );

        recordTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recordTypeSpinner.setAdapter(recordTypeArrayAdapter);
    }
    void findViews()
    {
        nameTextView = findViewById(R.id.nameTextView);
        sectionTextView = findViewById(R.id.sectionTextView);
        sectionNumTextView = findViewById(R.id.sectionNumTextView);
        recordTypeTextView = findViewById(R.id.recordTypeTextView);
        amountTextView = findViewById(R.id.amountTextView);

        nameEditText = findViewById(R.id.nameEditText);
        amountEditText = findViewById(R.id.amountEditText);
        recordTypeSpinner = findViewById(R.id.recordTypeSpinner);
    }
    void initializeColorVariables()
    {
        originalBackgroundTint = nameEditText.getBackgroundTintList();
        redBackgroundTint = getApplicationContext().getResources().getColorStateList(R.color.red);
        originalTextColor = nameTextView.getTextColors();
        redTextColor = getApplicationContext().getResources().getColorStateList(R.color.red);
    }
    boolean missingData()
    {
        if(nameEditText.getText().toString().isEmpty())
        {
            Toast.makeText(NewRecordActivity.this, "Please enter the name", Toast.LENGTH_LONG).show();
            return true;
        }
        else if(sectionSpinner.getSelectedItem().toString().isEmpty())
        {
            Toast.makeText(NewRecordActivity.this, "Please select section", Toast.LENGTH_LONG).show();
            return true;
        }
        else if(selectedNumberedSection() && sectionNumSpinner.getSelectedItem().toString()=="-")
        {
            Toast.makeText(NewRecordActivity.this, "Please select section number", Toast.LENGTH_LONG).show();
            return true;
        }
        else if(amountEditText.getText().toString().isEmpty())
        {
            Toast.makeText(NewRecordActivity.this, "Please enter amount", Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }
    boolean usedName(String newFileName)
    {
        //Look inside all file names and see if this file name was used before
        File directory = getApplicationContext().getFilesDir();
        File debtsFolder = new File(directory, "@string/ongoing_debts_folder");
        String[] allFileNames = debtsFolder.list();

        if(allFileNames==null) return false;

        for (int i = 0; i<allFileNames.length; i++)
        {
            if(allFileNames[i].equals(newFileName+".txt"))
            {
                Toast.makeText(NewRecordActivity.this, "Name is already in use. Please enter a unique name.", Toast.LENGTH_LONG).show();
                return true;
            }
        }

        return false;
    }
    boolean nameNotStartingWithLetter(String newFileName)
    {
        if(Character.isLetter(newFileName.charAt(0))) return false;

        Toast.makeText(NewRecordActivity.this, "Name should start with a letter", Toast.LENGTH_SHORT).show();
        return true;
    }
    boolean selectedNumberedSection()
    {
        return sectionSpinner.getSelectedItemPosition() != 0 && sectionSpinner.getSelectedItemPosition() != 4; //Not "بدون قسم" NOR "أخرى"
    }
    boolean zeroAmount(String amountText)
    {
        if(Float.parseFloat(amountText) == 0)
        {
            Toast.makeText(NewRecordActivity.this, "Amount cannot be zero", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    /*String generateID()
    { //Generate random letters for ID
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        return generator.generate(8);
    }*/
    String generateDebtDataString()
    {
        // PATTERN EXAMPLE (with 1 amount change): debtorName/2024-01-21T05:47:08.644/section/sectionNum/recordType/totalAmount/2024-01-21T05:47:08.644,amount1,amountChangeType1/2024-01-21T06:47:08.001,amount2,amountChangeType2
        return nameEditText.getText().toString()+"/"+
                LocalDateTime.now()+"/"+
                sectionSpinner.getSelectedItem().toString()+"/"+
                sectionNumSpinner.getSelectedItem().toString()+"/"+
                recordTypeSpinner.getSelectedItem().toString()+"/"+
                amountEditText.getText().toString()+"/"+ //<< total amount

                //This is for the amount change history:
                LocalDateTime.now()+","+
                amountEditText.getText().toString()+","+
                recordTypeSpinner.getSelectedItem().toString();

    }
    void emphasizeRequiredFields()
    {
        String nameText = nameEditText.getText().toString();
        String amountText = amountEditText.getText().toString();

        if(nameText.isEmpty() || nameNotStartingWithLetter(nameText) || usedName(nameText))
        {
            nameEditText.setBackgroundTintList(redBackgroundTint);
            nameTextView.setTextColor(redTextColor);
        }
        else
        {
            nameEditText.setBackgroundTintList(originalBackgroundTint);
            nameTextView.setTextColor(originalTextColor);
        }

        if(selectedNumberedSection() && sectionNumSpinner.getSelectedItem().toString()=="-")
        {
            sectionNumSpinner.setBackgroundTintList(redBackgroundTint);
            sectionNumTextView.setTextColor(redTextColor);
        }
        else
        {
            sectionNumSpinner.setBackgroundTintList(originalBackgroundTint);
            sectionNumTextView.setTextColor(originalTextColor);
        }

        if(amountText.isEmpty() || zeroAmount(amountText))
        {
            amountEditText.setBackgroundTintList(redBackgroundTint);
            amountTextView.setTextColor(redTextColor);
        }
        else
        {
            amountEditText.setBackgroundTintList(originalBackgroundTint);
            amountTextView.setTextColor(originalTextColor);
        }
    }

    public void onSaveClick(View view) throws IOException
    {
        String fileName = nameEditText.getText().toString();
        String amountText = amountEditText.getText().toString();
        if(missingData() || usedName(fileName) || nameNotStartingWithLetter(fileName) || zeroAmount(amountText))
        {
            emphasizeRequiredFields();
            return;
        }
        File directory = getApplicationContext().getFilesDir();
        File debtsFolder = new File(directory, "@string/ongoing_debts_folder");
        debtsFolder.mkdirs();
        File file = new File(debtsFolder, fileName+".txt");
        String textToBeSaved = generateDebtDataString();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(textToBeSaved.getBytes());

        fos.close();
        Toast.makeText(NewRecordActivity.this, "Saved", Toast.LENGTH_LONG).show();

        //testDisplaySavedData();

        finish();
    }
    public void onCancelClick(View view) throws  IOException
    {
        ExitNewRecordActivity();
    }
    @Override
    public void onBackPressed()
    {
        ExitNewRecordActivity();
    }
    void ExitNewRecordActivity()
    {
        if(dataEntered()) //if there's data entered by user on any of the fields
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewRecordActivity.this);
            builder.setMessage(R.string.new_record_discard_alert);
            builder.setPositiveButton(R.string.new_record_discard_confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User confirms the discard
                    finish();
                }
            });
            builder.setNegativeButton(R.string.new_record_discard_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancels the dialog.
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else //No data was entered by user >> go back immediately
        {
            finish();
        }
    }

    boolean dataEntered()
    {
        if(nameEditText.getText().toString().isEmpty() && amountEditText.getText().toString().isEmpty()) return false;
        return true;
    }
}
