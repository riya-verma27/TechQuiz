package com.example.techquiz;

import static com.example.techquiz.SplashActivity.category_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class category_activity extends AppCompatActivity {

     private GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");//title of tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to make back button(arrow)
        grid=findViewById(R.id.categoryView);
        //adapter will take an array of strings and assign them to the multiple grids

        GridAdapter ob=new GridAdapter(category_list);
        grid.setAdapter(ob);
    }
    //to make the back button work
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home);
        category_activity.this.finish();
        return super.onOptionsItemSelected(item);
    }
}