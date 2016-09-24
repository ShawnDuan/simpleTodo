package com.shawn_duan.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Shawn Duan on 9/22/16.
 */

public class EditActivity extends AppCompatActivity {
    private final static String TAG = "EditActivity";
    private EditText etItemContent;
    private Button btSave;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItemContent = (EditText) findViewById(R.id.textContent);
        String oldContent = getIntent().getStringExtra("itemContent");
//        etItemContent.setText(oldContent);
        etItemContent.append(oldContent);

        btSave = (Button) findViewById(R.id.buttonSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("position", getIntent().getIntExtra("position", 0));
                data.putExtra("newContent", etItemContent.getText().toString());
                setResult(RESULT_OK, data);
                EditActivity.this.finish();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
