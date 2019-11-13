package com.vavapps.sound.mvp.model.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vavapps.sound.R;
import com.vavapps.sound.app.utils.ArrayUtils;

import java.util.Date;

import cafe.adriel.androidaudioconverter.model.AudioFormat;

public abstract class SaveDialog extends Dialog implements View.OnClickListener {

    private EditText etName;
    private Spinner spExt;
    private View btnSave;
    private int position = 0;
    private String name;

    protected SaveDialog(Context context) {
        super(context);
    }

    protected SaveDialog(Context context,String name) {
        super(context);
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save);
        etName =  findViewById(R.id.etName);
        spExt = findViewById(R.id.spExt);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        if (name == null)
            etName.setText(new Date().getTime()+"");
        else etName.setText(name);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.format,R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.dropdown_style);
        spExt.setAdapter(adapter);
        spExt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public abstract void onSave(String name, AudioFormat format);

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave){
            dismiss();
            String name = etName.getText().toString();
            if (TextUtils.isEmpty(name)){
                Toast.makeText(getContext(), "文件名不能为空", Toast.LENGTH_SHORT).show();
            }
            else onSave(etName.getText().toString(), ArrayUtils.formats[position]);
        }
    }
}