package com.example.alarmmanagertest;

import static com.example.alarmmanagertest.DBmain.TABLENAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.example.alarmmanagertest.databinding.ActivityDisplayDataBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
public class DisplayData extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    DBmain dBmain;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    private ActivityDisplayDataBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =
                ActivityDisplayDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findId();
        dBmain = new DBmain(this);
        displayData();
        recyclerView.setLayoutManager(new
                LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(DisplayData.this, MainActivity.class);
                startActivity(a);
            }
        });
        dl = (DrawerLayout)findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        //action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_message){
                    Intent a = new Intent(DisplayData.this,
                            DestinationActivity.class);
                    startActivity(a);
                }else if (id == R.id.nav_chat){
                    Intent a = new Intent(DisplayData.this, Home.class);
                    startActivity(a);
                }
                else if (id == R.id.nav_sql){
                    Intent a = new Intent(DisplayData.this, MainActivity.class);
                    startActivity(a);
                }
                return true;
            }
        });
    }

    private void displayData() {
        sqLiteDatabase = dBmain.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLENAME,null);
                ArrayList < Model > models = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] avatar = cursor.getBlob(4);
            String star = cursor.getString(2);
            String price = cursor.getString(3);
            models.add(new Model(id, avatar, name, star, price));
        }
        cursor.close();
        myAdapter = new MyAdapter(this,
                R.layout.singledata, models, sqLiteDatabase);
        recyclerView.setAdapter(myAdapter);
    }

    private void findId() {
        recyclerView = findViewById(R.id.rv);
    }
}