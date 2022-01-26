package com.example.cybersleuthpathfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pathActivity(View view) {
        Intent intent = new Intent(MainActivity.this, PathActivity.class);
        startActivity(intent);
    }

    public void skillActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SkillActivity.class);
        startActivity(intent);
    }

    public void statActivity(View view) {
        Intent intent = new Intent(MainActivity.this, StatActivity.class);
        startActivity(intent);
    }
}