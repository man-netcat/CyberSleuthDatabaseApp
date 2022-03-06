package com.example.cybersleuthpathfinder;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PathActivity extends AppCompatActivity {

    ArrayList<String> resList = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<SrcDigimon> digimonDatabase = new ArrayList<>();
    HashMap<String, HashMap<String, DstDigimon>> digimonMap = new HashMap<>();
    Pathfinder pathfinder;

    /* Get digimon database from JSON */
    void populateNames() {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "digivolution_database.json");
        Type listDigimonType = new TypeToken<List<SrcDigimon>>() {
        }.getType();
        Gson gson = new Gson();
        digimonDatabase = gson.fromJson(jsonFileString, listDigimonType);
        assert digimonDatabase != null;
        for (SrcDigimon srcDigimon : digimonDatabase) {
            HashMap<String, DstDigimon> digimonDstMap = new HashMap<>();
            for (DstDigimon dstDigimon : srcDigimon.Next) {
                digimonDstMap.put(dstDigimon.Name, dstDigimon);
            }
            for (String prevDigimon : srcDigimon.Prev) {
                digimonDstMap.put(prevDigimon, null);
            }
            digimonMap.put(srcDigimon.Name, digimonDstMap);
        }

        pathfinder = new Pathfinder(digimonDatabase);
    }

    /* Get list of names from database */
    void populateDatabase() {
        for (SrcDigimon digimon : digimonDatabase) {
            names.add(digimon.Name);
        }
        Collections.sort(names);
    }

    public StringBuilder statString(DstDigimon stats) throws IllegalAccessException {
        StringBuilder statStr = new StringBuilder();
        Field[] fields = stats.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (name.equals("Name")) continue;
            Object value = field.get(stats);
            if (value != null)
                statStr.append("\n").append(name).append(": ").append(value);
        }

        return statStr;
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
        ListView resView = findViewById(R.id.pathResults);
        ArrayAdapter<String> resAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.item_textview, resList);
        resView.setAdapter(resAdapter);

        /* Set button for finding path */
        Button button = findViewById(R.id.calculate_path_button);
        button.setOnClickListener(v -> {
            try {
                String src = srcView.getText().toString();
                String dst = dstView.getText().toString();

                resList.clear();

                ArrayList<String> path = pathfinder.findPath(src, dst);

                for (int i = 0; i < path.size() - 1; i++) {
                    String cur = path.get(i);
                    String next = path.get(i + 1);

                    DstDigimon stats = Objects.requireNonNull(digimonMap.get(cur)).get(next);
                    StringBuilder statStr;
                    if (stats == null) {
                        statStr = new StringBuilder("\nDe-Digivolve");
                    } else {
                        statStr = statString(stats);
                    }

                    String str = cur + " -> " + next + statStr;
                    resList.add(str);
                }

                resAdapter.notifyDataSetChanged();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception ignored) {
            }
        });
    }
}