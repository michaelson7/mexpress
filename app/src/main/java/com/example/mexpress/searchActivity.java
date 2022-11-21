package com.example.mexpress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.mexpress.adapter.adapter;
import com.example.mexpress.adapter.adapter1;

import java.util.ArrayList;

public class searchActivity extends AppCompatActivity {

    EditText searchBox;
    RecyclerView recyclerView;
    adapter1[] adapters = new adapter1[1];
    com.example.mexpress.adapter.adapter1[] adapter1 = new adapter1[1];
    ArrayList<tripModel> list = new ArrayList<tripModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBox = findViewById(R.id.editTextTextPersonName6);
        recyclerView = findViewById(R.id.recyclerView2);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                searchData(s);
            }
        });
    }

    private void searchData(CharSequence s) {
        DB_Handler prcndbHandler = new DB_Handler(this);
        list = (ArrayList<tripModel>) prcndbHandler.search(s.toString());

        adapters[0] = new adapter1(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapters[0]);
    }
}