package ru.geekbrains.notes30.ui.info;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.geekbrains.notes30.R;

public class AlertDialogFragment extends DialogFragment {

    public static final String TAG = "AlertDialogFragment";
    public static final String RESULT = "AlertDialogFragment";

    private static final String ARG_TITLE = "ARG_TITLE";

    public static AlertDialogFragment newInstance(@StringRes int title) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TITLE, title);
        alertDialogFragment.setArguments(bundle);
        return alertDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(getArguments().getInt(ARG_TITLE))
                .setMessage(R.string.alert_dialog_message)
                .setIcon(R.drawable.ic_clear)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getParentFragmentManager().setFragmentResult(RESULT, null);
                    }
                });

        return builder.create();
    }
}
