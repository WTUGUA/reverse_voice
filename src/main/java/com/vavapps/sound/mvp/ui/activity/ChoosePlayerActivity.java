package com.vavapps.sound.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.flyco.tablayout.SlidingTabLayout;
import com.vavapps.sound.R;
import com.vavapps.sound.app.utils.SoundPoolHelper;
import com.vavapps.sound.mvp.event.ChooseMan;
import com.vavapps.sound.mvp.ui.fragment.PlayerFragment;
import com.umeng.analytics.MobclickAgent;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;

public class ChoosePlayerActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rl_complete)
    RelativeLayout rlComplete;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.stl_main)
    SlidingTabLayout stlMain;
    @BindView(R.id.vp_main)
    ViewPager vpMain;
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_zhubo);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        mFragments = new ArrayList<>();
        PlayerFragment normal = PlayerFragment.newInstance(0);
        PlayerFragment special = PlayerFragment.newInstance(1);
        PlayerFragment lcoal = PlayerFragment.newInstance(2);
        mFragments.add(normal);
        mFragments.add(special);
        mFragments.add(lcoal);
//      无需编写适配器，一行代码关联TabLayout与ViewPager
        stlMain.setViewPager(vpMain, new String[]{"普通话", "特色主播", "方言主播"},this, mFragments);
    }

    @Subscriber(mode = ThreadMode.MAIN)
    public void CloseBack(ChooseMan chooseMan){
        if (!isFinishing()){
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SoundPoolHelper.getInstance(ChoosePlayerActivity.this).release();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
