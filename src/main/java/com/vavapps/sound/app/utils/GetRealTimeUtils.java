package com.vavapps.sound.app.utils;

import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.vavapps.sound.app.utils.HeaderSpf.getVipTime;

public class GetRealTimeUtils {

    public interface  TimeCallBack{
        void getTime(Long time);
    }
    public static boolean isVip(long currentTime) {
        Long vipTime = getVipTime();
        long l = currentTime - vipTime;
        return l <= 24 * 60 * 60 * 1000;
    }

    public static void getNetTime(TimeCallBack timeCallBack) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {

                URL url = null;//取得资源对象
                try {
                    url = new URL("http://www.baidu.com");
                    //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
                    //url = new URL("http://www.bjtime.cn");
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    long ld = uc.getDate(); //取得网站日期时间
//                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(ld);
//                    final String format = formatter.format(calendar.getTime());
                    emitter.onNext(ld);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long time) throws Exception {
                        timeCallBack.getTime(time);
                    }
                });
    }
}
