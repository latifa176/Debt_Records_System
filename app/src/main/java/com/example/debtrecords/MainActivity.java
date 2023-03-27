package com.example.debtrecords;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debtrecords.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private View currentlyExpandedRecord;
    private DrawerLayout menuDrawerLayout;
    private ActionBarDrawerToggle menuToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeRecyclerView();
        initializeDrawerLayout();
    }
    void initializeRecyclerView()
    {
        RecyclerView recordRecyclerView=findViewById(R.id.recordRecyclerView);

        List<RecordItem> recordItems=new ArrayList<>();

        for(int i=0; i<10; i++)
        {
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
    /*public void onMenuClick(View view)
    {
        view.findViewById(R.id.)
    }*/

    void expandRecord(View view)
    {
        if(currentlyExpandedRecord!=null) //if a different record was currently expanded, shrink it
            shrinkRecord(currentlyExpandedRecord);
        view.findViewById(R.id.changeHistoryRecyclerView).setVisibility(View.VISIBLE);
        currentlyExpandedRecord = view;

        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.expand_history);
        currentlyExpandedRecord.findViewById(R.id.changeHistoryRecyclerView).startAnimation(animation);
    }
    void shrinkRecord(View view)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shrink_history);
        currentlyExpandedRecord.findViewById(R.id.changeHistoryRecyclerView).startAnimation(animation);

        view.findViewById(R.id.changeHistoryRecyclerView).setVisibility(View.INVISIBLE);
        currentlyExpandedRecord=null;
    }
}