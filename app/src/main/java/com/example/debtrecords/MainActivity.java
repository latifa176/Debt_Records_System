package com.example.debtrecords;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debtrecords.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recordRecyclerView=findViewById(R.id.recordRecyclerView);

        List<RecordItem> recordItems=new ArrayList<>();

        for(int i=0; i<10; i++){
            recordItems.add(
                    new RecordItem(
                            "Latifa",
                            LocalDateTime.now(),
                            10,
                            DebtorSection.First,
                            DebtorSectionNumber.Two,
                            AmountType.Debt
                    )
            );
        }

        recordRecyclerView.setAdapter(new RecordItemAdapter(recordItems));
    }
}