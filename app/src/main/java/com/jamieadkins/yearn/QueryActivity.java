package com.jamieadkins.yearn;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jamieadkins.yearn.ui.QueryFragment;
import com.jamieadkins.yearn.utils.LocationFragment;

public class QueryActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();

    private LocationFragment.LocationFetchListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new QueryFragment())
                    .commit();
        }
    }

    @Override
    protected void onSnapshotReady(YearnSnapshot snapshot) {
        // Update recycler view with context based information.
    }

    public void registerListener(LocationFragment.LocationFetchListener listener) {
        mListener = listener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mListener = null;
    }
}
