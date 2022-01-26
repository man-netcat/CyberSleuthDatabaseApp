package com.example.cybersleuthpathfinder;

import android.os.Bundle;
import android.util.Log;
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

public class SkillActivity extends AppCompatActivity {
    HashMap<String, Skill> map = new HashMap<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Skill> skill_database = new ArrayList<>();
    ArrayList<String> resList = new ArrayList<>();


    /* Get digimon database from JSON */
    void populateNames() {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "skill_database.json");
        Type listSkillType = new TypeToken<List<Skill>>() {
        }.getType();
        Gson gson = new Gson();
        skill_database = gson.fromJson(jsonFileString, listSkillType);
        assert skill_database != null;
    }

    /* Get list of map from database */
    void populateDatabase() {
        for (Skill skill : skill_database) {
            map.put(skill.Name, skill);
            names.add(skill.Name);
        }
        Collections.sort(names);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        populateNames();
        populateDatabase();

        /* Set result list view */
        ListView skillList = findViewById(R.id.skillResult);
        ArrayAdapter<String> skillAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.item_textview, resList);
        skillList.setAdapter(skillAdapter);

        /* Build skill selection view */
        AutoCompleteTextView skillSelect = findViewById(R.id.skillSelect);
        ArrayAdapter<String> selectAdapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.item_textview, names);
        skillSelect.setOnItemClickListener((adapterView, view, i, l) -> {
            try {
                String selection = skillSelect.getText().toString();

                resList.clear();

                Skill skill = map.get(selection);
                assert skill != null;

                ArrayList<Learns> learned = skill.Digimon;

                for (Learns learn : learned) {
                    String str = learn.Name + " at Lvl " + learn.Level;
                    resList.add(str);
                    Log.i("Learn", str);
                }

                skillAdapter.notifyDataSetChanged();
            } catch (Exception ignored) {
                resList.clear();
            }
        });
        skillSelect.setAdapter(selectAdapter);
        skillSelect.setThreshold(1);

    }
}