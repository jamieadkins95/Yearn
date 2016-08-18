package com.jamieadkins.yearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jamieadkins.yearn.ui.QueryFragment;
import com.jamieadkins.yearn.ui.ResultFragment;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_YEARN = "com.jamieadkins.yearn.YEARN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ResultFragment())
                    .commit();
        }

        setTitle(getIntent().getCharSequenceExtra(EXTRA_YEARN));

    }
}
