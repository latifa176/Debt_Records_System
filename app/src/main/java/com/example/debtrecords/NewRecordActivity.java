package com.example.debtrecords;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.text.RandomStringGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NewRecordActivity extends AppCompatActivity
{
    private TextView testDataText;

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

        testDataText=findViewById(R.id.testData);
        testDisplaySavedData();
    }
    void initializeSectionSpinner()
    {
        sectionSpinner = findViewById(R.id.sectionSpinner);
        ArrayAdapter<DebtorSection> sectionArrayAdapter = new ArrayAdapter<DebtorSection>
                (this,
                        android.R.layout.simple_spinner_item,
                        DebtorSection.values()
                );

        sectionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(sectionArrayAdapter);
    }
    void initializeSectionNumSpinner()
    {
        sectionNumSpinner = findViewById(R.id.sectionNumSpinner);
        ArrayAdapter<DebtorSectionNumber> sectionNumArrayAdapter = new ArrayAdapter<DebtorSectionNumber>
                (this,
                        android.R.layout.simple_spinner_item,
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
                        android.R.layout.simple_spinner_item,
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
            if(allFileNames[i].equals(newFileName))
                return true;
        }

        return false;
    }
    boolean selectedNumberedSection()
    {
        return sectionSpinner.getSelectedItemPosition() != 0 && sectionSpinner.getSelectedItemPosition() != 4; //Not "بدون قسم" NOR "أخرى"
    }
    /*String generateID()
    { //Generate random letters for ID
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        return generator.generate(8);
    }*/
    String generateDebtDataString()
    {
        return nameEditText.getText().toString()+","+
                sectionSpinner.getSelectedItemPosition()+","+
                sectionNumSpinner.getSelectedItemPosition()+","+
                recordTypeSpinner.getSelectedItemPosition()+","+
                amountEditText.getText().toString()+",";
    }
    void emphasizeRequiredFields()
    {
        if(nameEditText.getText().toString().isEmpty())
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

        if(amountEditText.getText().toString().isEmpty())
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
        if(missingData() || usedName(fileName))
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
        Toast.makeText(NewRecordActivity.this, "Saved in "+directory, Toast.LENGTH_LONG).show();

        testDisplaySavedData();
    }
    void testDisplaySavedData()
    {
        File directory = getApplicationContext().getFilesDir();
        File debtsFolder = new File(directory, "@string/ongoing_debts_folder");
        String content="";

        try
        {
            File[] files = debtsFolder.listFiles();
            FileInputStream fis = new FileInputStream(files[0]);
            int character = fis.read();

            while (character != -1)
            {
                content = content + Character.toString((char)character); //Appending the character into the content string
                character = fis.read(); //Read the next character
            }

            testDataText.setText(content);
        }
        catch (Exception e)
        {
            testDataText.setText(e.toString());
        }
    }
}
