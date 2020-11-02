package com.pashin.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

public class DBActivity extends AppCompatActivity {

    GridView gv;
    private SQLiteDatabase myDB;
    EditText idView, cityView, countView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_b);
        openDB();
        myDB.execSQL("CREATE TABLE IF NOT EXISTS visit (id INTEGER PRIMARY KEY AUTOINCREMENT, city TEXT, count INTEGER);");
        selectDB();
        idView = findViewById(R.id.id);
        cityView = findViewById(R.id.city);
        countView = findViewById(R.id.count);
    }

    public void openDB() {
        myDB = openOrCreateDatabase("compass_db", MODE_PRIVATE, null);
    }

    public void selectDB() {
        Cursor myCursor = myDB.rawQuery("select * from visit", null);
        ArrayList<String> data = new ArrayList<>(Arrays.asList("id | city | count"));
        gv = findViewById(R.id.grid_view);
        int id;
        String city;
        int count;
        while(myCursor.moveToNext()) {
            id = myCursor.getInt(0);
            city = myCursor.getString(1);
            count = myCursor.getInt(2);
            data.add(id + " | " + city + " | " + count);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        gv.setAdapter(adapter);
        myCursor.close();
    }

    public void delete(View view) {
        myDB.execSQL("DELETE FROM visit WHERE id=" + idView.getText().toString());
        selectDB();
    }

    public void insert(View view) {
        ContentValues cv = new ContentValues();
        cv.put("city", cityView.getText().toString());
        cv.put("count", countView.getText().toString());
        myDB.insert("visit", null, cv);
        selectDB();
    }

    public void update(View view) {
        myDB.execSQL("UPDATE visit SET city='" + cityView.getText().toString() + "', count='" + countView.getText().toString() + "'WHERE id=" + idView.getText().toString());
        selectDB();
    }

    public void closeDB() {
        myDB.close();
    }

    public void goInfo(View view) {
        closeDB();
        Intent intent = new Intent(DBActivity.this, InfoActivity.class);
        startActivity(intent);
    }
}