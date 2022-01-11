package com.example.cybersleuthpathfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get digimon database from JSON
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "digimon_database.json");
        Type listDigimonType = new TypeToken<List<SrcDigimon>>() {
        }.getType();
        Gson gson = new Gson();
        List<SrcDigimon> digimon_database = gson.fromJson(jsonFileString, listDigimonType);
        assert digimon_database != null;
        Pathfinder pathfinder = new Pathfinder(digimon_database);

        // Get list of names from database
        ArrayList<String> names = new ArrayList<>();
        for (SrcDigimon digimon : digimon_database) {
            names.add(digimon.Name);
        }

        // Build src view
        AutoCompleteTextView srcActv = findViewById(R.id.srcActv);
        ArrayAdapter<String> srcAdapter = new ArrayAdapter<>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, names);
        srcActv.setAdapter(srcAdapter);
        srcActv.setThreshold(1);

        // Build src view
        AutoCompleteTextView dstActv = findViewById(R.id.dstActv);
        ArrayAdapter<String> dstAdapter = new ArrayAdapter<>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, names);
        dstActv.setAdapter(dstAdapter);
        dstActv.setThreshold(1);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String src = srcAdapter.getItem(position);
                String dst = dstAdapter.getItem(position);
                try {
                    ArrayList<String> path = pathfinder.findPath(src, dst);
                    for (String vertex : path) {
                        Log.i("node", vertex);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        srcActv.setOnItemClickListener(onItemClickListener);
        dstActv.setOnItemClickListener(onItemClickListener);

    }
}