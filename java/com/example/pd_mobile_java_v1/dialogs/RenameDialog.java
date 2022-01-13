package com.example.pd_mobile_java_v1.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.pd_mobile_java_v1.R;

import androidx.fragment.app.DialogFragment;

public class RenameDialog extends DialogFragment {
    IRenameElement context;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = (IRenameElement) context;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        String oldTitle = getArguments().getString("oldTitle");
        View v = inflater.inflate(R.layout.rename_item_dialog, null);
        EditText editText = v.findViewById(R.id.editTextRename);
        editText.setText(oldTitle);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Переименование \""+oldTitle+"\"")
                .setIcon(android.R.drawable.ic_menu_edit)
                .setView(v)
                .setPositiveButton("Переименовать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog dialogView = RenameDialog.this.getDialog();
                        assert dialogView != null;
                        EditText renameField = dialogView.findViewById(R.id.editTextRename);
                        String title = renameField.getText().toString();
                        context.renameElement(title);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.cancelRename();
                    }
                })
                .create();
    }
}
