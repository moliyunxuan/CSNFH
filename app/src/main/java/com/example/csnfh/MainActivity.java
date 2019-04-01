package com.example.csnfh;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.csnfh.fragment.EventFragment;
import com.example.csnfh.fragment.FarmEventFragment;
import com.example.csnfh.fragment.PersonalFragment;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private RelativeLayout rl_content;
    private ImageView item1_iv,item2_iv,item3_iv,item4_iv;
    private TextView item1_tv,item2_tv,item3_tv,item4_tv;
    private LinearLayout item1,item2,item3,item4;
    private ImageView[] ivs;
    private TextView[] tvs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "a50a9b783b262ebbb63dfe1c8bf0b140");


        initView();

        fragmentManager = getSupportFragmentManager();

        initListener();
    }

    private void initListener() {
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);

    }

    private void initView() {
        rl_content = findViewById(R.id.rl_content);
        item1_iv =  findViewById(R.id.item1_iv);
        item1_tv =  findViewById(R.id.item1_tv);
        item1 =  findViewById(R.id.item1);
        item2_iv =  findViewById(R.id.item2_iv);
        item2_tv = findViewById(R.id.item2_tv);
        item2 =  findViewById(R.id.item2);
        item3_iv =  findViewById(R.id.item3_iv);
        item3_tv =  findViewById(R.id.item3_tv);
        item3 =  findViewById(R.id.item3);
        ivs = new ImageView[]{item1_iv,item2_iv,item3_iv,item4_iv};
        tvs = new TextView[]{item1_tv,item2_tv,item3_tv,item4_tv};
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item1: {
                FragmentTransaction transaction = fragmentManager.beginTransaction();//创建一个事务
                transaction.replace(R.id.rl_content,new FarmEventFragment());
                transaction.commit();//事务一定要提交，replace才会有效
                break;
            }
            case R.id.item2: {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.rl_content,new EventFragment());
                transaction.commit();
                break;

            }
            case R.id.item3: {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.rl_content,new PersonalFragment());
                transaction.commit();
                break;
            }
            default:break;
        }
    }
}
