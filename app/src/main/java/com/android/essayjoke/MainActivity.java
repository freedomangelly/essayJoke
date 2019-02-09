package com.android.essayjoke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baselibrary.ioc.CheckNet;
import com.android.baselibrary.ioc.OnClick;
import com.android.baselibrary.ioc.ViewById;
import com.android.baselibrary.ioc.ViewUtils;

public class MainActivity extends AppCompatActivity {
    @ViewById(R.id.test_tv)
    TextView textView;

    private int mPage=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(MainActivity.this);
        textView.setText("IOCTV");
    }

    @CheckNet(R.id.test_tv)
    @OnClick({R.id.test_tv,R.id.test_iv})
    private void onClick(View view){
        Toast.makeText(this,"1111111111", Toast.LENGTH_SHORT).show();
    }


}
