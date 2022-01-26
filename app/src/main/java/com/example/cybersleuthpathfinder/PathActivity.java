package com.example.cybersleuthpathfinder;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PathActivity extends AppCompatActivity {

    ArrayList<String> resList = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<SrcDigimon> digimon_database = new ArrayList<>();
    Pathfinder pathfinder;

    /* Get digimon database from JSON */
    void populateNames() {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "digimon_database.json");
        Type listDigimonType = new TypeToken<List<SrcDigimon>>() {
        }.getType();
        Gson gson = new Gson();
        digimon_database = gson.fromJson(jsonFileString, listDigimonType);
        assert digimon_database != null;
        pathfinder = new Pathfinder(digimon_database);
    }

    /* Get list of names from database */
    void populateDatabase() {
        for (SrcDigimon digimon : digimon_database) {
            names.add(digimon.Name);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        populateNames();
        populateDatabase();

        /* Build src view */
        AutoCompleteTextView srcView = findViewById(R.id.srcView);
        ArrayAdapter<String> srcAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.item_textview, names);
        srcView.setAdapter(srcAdapter);
        srcView.setThreshold(1);

        /* Build dst view */
        AutoCompleteTextView dstView = findViewById(R.id.dstView);
        ArrayAdapter<String> dstAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.item_textview, names);
        dstView.setAdapter(dstAdapter);
        dstView.setThreshold(1);

        /* Set result list view */
        ListView resView = findViewById(R.id.resView);
        ArrayAdapter<String> resAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.item_textview, resList);
        resView.setAdapter(resAdapter);

        /* Set button for finding path */
        Button button = findViewById(R.id.calculate_path_button);
        button.setOnClickListener(v -> {
            String src = srcView.getText().toString();
            String dst = dstView.getText().toString();

            resList.clear();

            ArrayList<String> path = pathfinder.findPath(src, dst);

            for (int i = 0; i < path.size() - 1; i++) {
                String cur = path.get(i);
                String next = path.get(i + 1);
                String str = cur + " -> " + next;
                resList.add(str);
            }

            resAdapter.notifyDataSetChanged();
        });

    }
}