package ru.geekbrains.notes30.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import ru.geekbrains.notes30.R;

public class InfoFragment extends Fragment {

    public static final String TAG = "InfoFragment";

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getChildFragmentManager().setFragmentResultListener(AlertDialogFragment.RESULT, getViewLifecycleOwner(), (requestKey, result) -> Snackbar.make(getView(), "Positive", Snackbar.LENGTH_SHORT).show());

        view.findViewById(R.id.dialog_fragment).setOnClickListener(v -> showDialogFragment());


        view.findViewById(R.id.alert_dialog).setOnClickListener(v -> showAlertDialog());

        view.findViewById(R.id.list_dialog).setOnClickListener(v -> showListDialog());

        view.findViewById(R.id.single_choice_dialog).setOnClickListener(v -> showSingleChoiceDialog());

        view.findViewById(R.id.multi_choice_dialog).setOnClickListener(v -> showMultiChoiceDialog());

        view.findViewById(R.id.custom_view_dialog).setOnClickListener(v -> showCustomViewDialog());

        view.findViewById(R.id.alert_dialog_fragment).setOnClickListener(v -> showAlertDialogFragment());

    }

    private void showDialogFragment() {
        CustomDialogFragment.newInstance()
                .show(getChildFragmentManager(), CustomDialogFragment.TAG);
    }

    private void showAlertDialogFragment() {
        AlertDialogFragment.newInstance(R.string.list_title)
                .show(getChildFragmentManager(), AlertDialogFragment.TAG);
    }

    private void showCustomViewDialog() {

        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit, null, false);

        EditText editText = view.findViewById(R.id.edit_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.notes_title)
                .setView(view)
                .setPositiveButton(R.string.select, (dialog, which) -> Snackbar.make(getView(), editText.getText(), Snackbar.LENGTH_SHORT).show());

        builder.show();

    }

    private void showMultiChoiceDialog() {
        String[] items = getResources().getStringArray(R.array.options);

        boolean[] checked = new boolean[items.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setMultiChoiceItems(items, checked, (dialog, which, isChecked) -> checked[which] = isChecked)
                .setPositiveButton(R.string.select, (dialog, which) -> {

                    StringBuilder strBuilder = new StringBuilder();

                    for (int i = 0; i < checked.length; i++) {
                        if (checked[i]) {
                            strBuilder.append(items[i]);
                            strBuilder.append(',');
                        }
                    }

                    Snackbar.make(getView(), strBuilder, Snackbar.LENGTH_SHORT).show();
                });

        builder.show();

    }

    private void showSingleChoiceDialog() {
        String[] items = getResources().getStringArray(R.array.options);

        final int[] selected = {-1};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setSingleChoiceItems(items, selected[0], (dialog, which) -> selected[0] = which)
                .setPositiveButton(R.string.select, (dialog, which) -> {
                    if (selected[0] != -1) {
                        Snackbar.make(getView(), items[selected[0]], Snackbar.LENGTH_SHORT).show();
                    }
                });

        builder.show();

    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.alert_dialog_title)
                .setMessage(R.string.alert_dialog_message)
                .setIcon(R.drawable.ic_clear)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_positive, (dialog, which) -> Snackbar.make(getView(), "Positive", Snackbar.LENGTH_SHORT).show())
                .setNegativeButton(R.string.btn_negative, (dialog, which) -> Snackbar.make(getView(), "Negative", Snackbar.LENGTH_SHORT).show())
                .setNeutralButton(R.string.btn_neutral, (dialog, which) -> Snackbar.make(getView(), "Neutral", Snackbar.LENGTH_SHORT).show());

        builder.show();
    }

    private void showListDialog() {
        String[] items = getResources().getStringArray(R.array.options);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.list_title)
                .setItems(items, (dialog, which) -> Snackbar.make(getView(), items[which], Snackbar.LENGTH_SHORT).show());

        builder.show();
    }

}
