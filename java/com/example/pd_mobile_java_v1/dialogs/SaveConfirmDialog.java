package com.example.pd_mobile_java_v1.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.doc.DocActivity;

import androidx.fragment.app.DialogFragment;

public class SaveConfirmDialog extends DialogFragment {
    private DocActivity context;
    public static final boolean DOC_SELECTED = false;
    public static final boolean TOPIC_SELECTED = true;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = (DocActivity) context;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String action = getArguments().getString("action");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Подтверждение")
                .setIcon(android.R.drawable.ic_menu_save)
                .setMessage("Вы действительно хотите применить изменения?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.getViewModel().save(action);
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.getViewModel().dontSave(action);
                    }
                })
                .setNeutralButton("Отмена", null)
                .create();
    }

}
