package com.jamieadkins.yearn.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.yearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        getActivity().setTitle(R.string.im_yearning_for);
    }
}