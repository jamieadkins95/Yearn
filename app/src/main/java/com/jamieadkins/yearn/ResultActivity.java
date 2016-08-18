package com.jamieadkins.yearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_YEARN = "com.jamieadkins.yearn.YEARN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }
}
