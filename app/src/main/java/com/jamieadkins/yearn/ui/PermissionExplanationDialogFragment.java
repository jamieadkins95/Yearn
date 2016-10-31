package com.jamieadkins.yearn.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.jamieadkins.yearn.R;

/**
 * Shows explanation for a permission request.
 */

public class PermissionExplanationDialogFragment extends DialogFragment {
    private final String TAG = getClass().getSimpleName();

    public interface PermissionExplanationListener {
        void onExplanationRead();
    }

    PermissionExplanationListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.permission_location_title)
                .setMessage(R.string.permission_location_message)
                .setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onExplanationRead();
                    }
                });

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        onExplanationRead();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (PermissionExplanationListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception.
            throw new ClassCastException(context.toString()
                    + " must implement " + PermissionExplanationListener.class.getSimpleName());
        }
    }

    private void onExplanationRead() {
        if (mListener != null) {
            mListener.onExplanationRead();
        } else {
            Log.e(TAG, "No listener to callback to!");
        }
    }
}
