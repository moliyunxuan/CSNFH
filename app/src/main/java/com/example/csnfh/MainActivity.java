package com.example.csnfh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.csnfh.fragment.EventFragment;
import com.example.csnfh.fragment.FarmEventFragment;
import com.example.csnfh.fragment.PersonalFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private RelativeLayout rl_content;



    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    Unbinder unbinder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "a50a9b783b262ebbb63dfe1c8bf0b140");

        fragmentManager = getSupportFragmentManager();
        rl_content = findViewById(R.id.rl_content);
        unbinder = ButterKnife.bind(this);
        inint();


    }


    private void inint() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);//设置 NavigationItemSelected 事件监听
        //  BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);//改变 BottomNavigationView 默认的效果
        //选中第一个item,对应第一个fragment
        bottomNavigationView.setSelectedItemId(R.id.item_1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //NavigationItemSelected 事件监听
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        changePageFragment(item.getItemId());
        return true;
    }


    /**
     * 当点击导航栏时改变fragment
     *
     * @param id
     */
    public void changePageFragment(int id) {
        switch (id) {
            case R.id.item_1: {
                FragmentTransaction transaction = fragmentManager.beginTransaction();//创建一个事务
                transaction.replace(R.id.rl_content, new FarmEventFragment());
                transaction.commit();//事务一定要提交，replace才会有效

                break;
            }
            case R.id.item_2: {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.rl_content, new EventFragment());
                transaction.commit();
                break;
            }
            case R.id.item_3:{
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.rl_content, new PersonalFragment());
                transaction.commit();
                break;
        }
        default:
        break;
    }
    }
}










