package com.example.cybersleuthpathfinder;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StatActivity extends AppCompatActivity {
    HashMap<String, Digimon> map = new HashMap<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Digimon> digimon_database = new ArrayList<>();
    ArrayList<String> resList = new ArrayList<>();


    /* Get digimon database from JSON */
    void populateNames() {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "digimon_database.json");
        Type listSkillType = new TypeToken<List<Digimon>>() {
        }.getType();
        Gson gson = new Gson();
        digimon_database = gson.fromJson(jsonFileString, listSkillType);
        assert digimon_database != null;
    }

    /* Get list of map from database */
    void populateDatabase() {
        for (Digimon digimon : digimon_database) {
            map.put(digimon.Name, digimon);
            names.add(digimon.Name);
        }
        Collections.sort(names);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        populateNames();
        populateDatabase();

        /* Set result list view */
        ListView statList = findViewById(R.id.statResult);
        ArrayAdapter<String> statAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.item_textview, resList);
        statList.setAdapter(statAdapter);

        /* Build digimon selection view */
        AutoCompleteTextView statSelect = findViewById(R.id.statSelect);
        ArrayAdapter<String> selectAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.item_textview, names);
        statSelect.setOnItemClickListener((adapterView, view, i, l) -> {
            try {
                String selection = statSelect.getText().toString();

                resList.clear();

                Digimon digimon = map.get(selection);
                assert digimon != null;

                resList.add("Name: " + digimon.Name);
                resList.add("HP: " + digimon.HP);
                resList.add("SP: " + digimon.SP);
                resList.add("ATK: " + digimon.Atk);
                resList.add("DEF: " + digimon.Def);
                resList.add("INT: " + digimon.Int);
                resList.add("SPD: " + digimon.Spd);
                resList.add("Type: " + digimon.Type);
                resList.add("Attribute: " + digimon.Attribute);
                resList.add("Stage: " + digimon.Stage);
                resList.add("Memory: " + digimon.Memory);
                resList.add("Equip Slots: " + digimon.EquipSlots);

                statAdapter.notifyDataSetChanged();
            } catch (Exception ignored) {
                resList.clear();
            }
        });
        statSelect.setAdapter(selectAdapter);
        statSelect.setThreshold(1);
    }
}