package com.vavapps.sound.app.utils;

import com.vavapps.sound.R;
import com.vavapps.sound.mvp.model.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManUtils {



    //普通话
    public static final int Ordinary = 0;
    //特色主播
    public static final int Special = 1;
    //方言主播
    public static final int Dialect = 2;


    private static PlayerManUtils playerManUtils= null;

    private PlayerManUtils() {



    }





    public static PlayerManUtils getInstance(){
        if (playerManUtils == null){
            playerManUtils = new PlayerManUtils();
        }
        return playerManUtils;
    }

    public List<Player> getByType(int type){
        ArrayList<Player> players = new ArrayList<>();
        switch (type){
            case Ordinary:
                players.add(new Player("小燕","xiaoyan",Ordinary,"普通话", R.mipmap.icon_xiaoyan,R.raw.xiaoyan));
                players.add(new Player("小宇","xiaoyu",Ordinary,"普通话", R.mipmap.icon_xiaoyu,R.raw.xiaoyu));
                players.add(new Player("小研","vixy",Ordinary,"普通话", R.mipmap.icon_vixy,R.raw.vixy));
                players.add(new Player("小琪","xiaoqi",Ordinary,"普通话", R.mipmap.icon_xiaoqi,R.raw.xiaoqi));
                players.add(new Player("小峰","vixf",Ordinary,"普通话", R.mipmap.icon_vixf,R.raw.vixf));

                players.add(new Player("许久","aisjiuxu",Ordinary,"普通话", R.mipmap.icon_vixf,R.raw.aisjiuxu));
                players.add(new Player("小萍","aisxping",Ordinary,"普通话", R.mipmap.icon_vixy,R.raw.aisxping));
                players.add(new Player("小婧","aisjinger",Ordinary,"普通话", R.mipmap.icon_xiaoqi,R.raw.aisjinger));

                break;
            case Special:
                players.add(new Player("小新","xiaoxin",Special,"普通话", R.mipmap.icon_xiaoxin,R.raw.xiaoxin));
                players.add(new Player("楠楠","nannan",Special,"普通话", R.mipmap.icon_nannan,R.raw.nannan));
                players.add(new Player("老孙","vils",Special,"普通话", R.mipmap.icon_vils,R.raw.vils));
                players.add(new Player("许小宝","aisbabyxu",Special,"普通话", R.mipmap.icon_vixf,R.raw.aisbabyxu));

                break;
            case Dialect:
                players.add(new Player("小梅","xiaomei",Ordinary,"粤语", R.mipmap.icon_xiaomei,R.raw.xiaomei));
                players.add(new Player("小莉","xiaolin",Dialect,"台湾普通话", R.mipmap.icon_xiaolin,R.raw.xiaolin));
                players.add(new Player("小蓉","xiaorong",Dialect,"四川话", R.mipmap.icon_xiaorong,R.raw.xiaorong));
                players.add(new Player("小芸","xiaoqian",Dialect,"东北话", R.mipmap.icon_xiaoqian,R.raw.xiaoqian));
                players.add(new Player("小坤","xiaokun",Dialect,"河南话", R.mipmap.icon_xiaokun,R.raw.xiaokun));
                players.add(new Player("小强","xiaoqiang",Dialect,"湖南话", R.mipmap.icon_xiaoqiang,R.raw.xiaoqiang));
                players.add(new Player("小莹","vixying",Dialect,"陕西话", R.mipmap.icon_vixying,R.raw.vixying));
                break;
        }
        return players;
    }



}
