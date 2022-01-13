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

public class DeleteConfirmDialog extends DialogFragment {
    IDeleteElements context;

    public void onAttach(Context context){
        super.onAttach(context);
        this.context = (IDeleteElements) context;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String elements = getArguments().getString("elements");
        String count = getArguments().getString("count");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Удаление")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setMessage("Вы действительно хотите удалить элементы ("+count+")?: \""+elements+"\"")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.deleteElements();
                    }
                })
                .setNegativeButton("Отмена", null)
                .create();
    }
}
