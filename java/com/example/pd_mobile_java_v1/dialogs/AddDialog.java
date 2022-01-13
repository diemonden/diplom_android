package com.example.pd_mobile_java_v1.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.pd_mobile_java_v1.R;

import androidx.fragment.app.DialogFragment;

public class AddDialog extends DialogFragment {
        private IAddElement context;
        public static final boolean DOC_SELECTED = false;
        public static final boolean TOPIC_SELECTED = true;
        @Override
         public void onAttach(Context context){
            super.onAttach(context);
            this.context = (IAddElement) context;
        }
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            return builder
                    .setTitle("Создание элемента")
                    .setIcon(android.R.drawable.ic_input_add)
                    .setView(R.layout.add_item_dialog)
                    .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dialog dialogView = AddDialog.this.getDialog();
                            assert dialogView != null;
                            EditText titleField = dialogView.findViewById(R.id.editTextSetTitle);
                            String title = titleField.getText().toString();
                            boolean type = DOC_SELECTED;
                            RadioGroup radioGroup = dialogView.findViewById(R.id.add_radio_group);
                            if(radioGroup.getCheckedRadioButtonId() == R.id.rb_topic)
                                type = TOPIC_SELECTED;
                            context.addElement(title,type);
                        }
                    })
                    .setNegativeButton("Отмена", null)
                    .create();
        }

}
