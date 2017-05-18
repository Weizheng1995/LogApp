package com.wz.logapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle=getIntent().getExtras();
        String result=bundle.getString("result");
        Toast.makeText(DetailActivity.this,result,Toast.LENGTH_SHORT).show();
    }
}
