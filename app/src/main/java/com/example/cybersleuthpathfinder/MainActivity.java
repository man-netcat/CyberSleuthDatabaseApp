package com.example.cybersleuthpathfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> resList = new ArrayList<>();

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
        AutoCompleteTextView srcView = findViewById(R.id.srcView);
        ArrayAdapter<String> srcAdapter = new ArrayAdapter<>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, names);
        srcView.setAdapter(srcAdapter);
        srcView.setThreshold(1);

        // Build dst view
        AutoCompleteTextView dstView = findViewById(R.id.dstView);
        ArrayAdapter<String> dstAdapter = new ArrayAdapter<>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, names);
        dstView.setAdapter(dstAdapter);
        dstView.setThreshold(1);

        ListView resView = findViewById(R.id.results);
        ArrayAdapter<String> resAdapter = new ArrayAdapter<>(this, R.layout.custom_list_item, resList);
        resView.setAdapter(resAdapter);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            String src = srcView.getText().toString();
            String dst = dstView.getText().toString();

            Log.i("Src", src);
            Log.i("Dst", dst);

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