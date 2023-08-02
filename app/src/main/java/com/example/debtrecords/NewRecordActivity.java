package com.example.debtrecords;

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
    private EditText nameEditText, amountEditText;
    private Spinner sectionSpinner, sectionNumSpinner, recordTypeSpinner;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);

        initializeSectionSpinner();
        initializeSectionNumSpinner();
        initializeRecordTypeSpinner();

        findViews();

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
        nameEditText = findViewById(R.id.nameEditText);
        amountEditText = findViewById(R.id.amountEditText);
        recordTypeSpinner = findViewById(R.id.recordTypeSpinner);
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
    boolean selectedNumberedSection()
    {
        return sectionSpinner.getSelectedItemPosition() != 0 && sectionSpinner.getSelectedItemPosition() != 4; //Not "بدون قسم" NOR "أخرى"
    }
    String generateID()
    { //Generate random letters for ID
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        return generator.generate(8);
    }

    public void onSaveClick(View view) throws IOException
    {
        if(missingData())
        {
            return;
        }
        File directory = getApplicationContext().getFilesDir();
        File file = new File(directory, "test.txt");
        String textToBeSaved = "testing testing " + generateID();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(textToBeSaved.getBytes());

        fos.close();
        Toast.makeText(NewRecordActivity.this, "Saved in "+directory, Toast.LENGTH_LONG).show();

        testDisplaySavedData();
    }
    void testDisplaySavedData()
    {
        File directory = getApplicationContext().getFilesDir();
        File file = new File(directory, "test.txt");
        String content="";

        try
        {
            FileInputStream fis = new FileInputStream(file);
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
