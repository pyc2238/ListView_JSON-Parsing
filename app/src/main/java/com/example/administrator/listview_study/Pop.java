package com.example.administrator.listview_study;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

class Pop extends Activity implements View.OnClickListener {
    private Button inputBtn,finishBtn;
    private EditText editText;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        inputBtn = (Button)findViewById(R.id.inputBtn);
        finishBtn = (Button)findViewById(R.id.finishBtn);
        editText = (EditText) findViewById(R.id.editText);


        setResult(MainActivity.RESULT_NG);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.inputBtn){
            Intent data = new Intent();
            data.putExtra("result", editText.getText().toString());
            setResult(MainActivity.RESULT_OK, data);
            finish();
        }else if(v.getId() == R.id.finishBtn){
            finish();
        }
    }
}
