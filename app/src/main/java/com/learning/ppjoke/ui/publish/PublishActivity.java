package com.learning.ppjoke.ui.publish;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.learning.libnavannotation.ActivityDestination;
import com.learning.ppjoke.R;

@ActivityDestination(pageUrl = "main/tabs/publish")
public class PublishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("PublishActivity","进入OnCreate");
        setContentView(R.layout.activity_publish);
    }
}