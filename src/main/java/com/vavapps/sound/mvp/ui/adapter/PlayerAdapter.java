package com.vavapps.sound.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vavapps.sound.R;
import com.vavapps.sound.app.utils.SoundPoolHelper;
import com.vavapps.sound.mvp.model.entity.Player;

import java.util.List;

public class PlayerAdapter extends BaseQuickAdapter<Player, BaseViewHolder> {

    public PlayerAdapter(@Nullable List<Player> data) {
        super(data);
        this.mLayoutResId = R.layout.item_player;
    }

    public interface SettingCallback {
        void chooseItem(int position);
    }

    private SettingCallback settingCallback;

    public void setSettingCallback(SettingCallback settingCallback) {
        this.settingCallback = settingCallback;
    }

    @Override
    protected void convert(BaseViewHolder helper, Player item) {

        helper.setText(R.id.tv_player_name, item.getPlayerName());


        helper.itemView.findViewById(R.id.fl_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 播测试音
                try {
                    SoundPoolHelper.getInstance(mContext).playSount(item.getPlayerValue());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        helper.setText(R.id.tv_player_type,item.getTypeName());

        ImageView iv_avatar = (ImageView) helper.itemView.findViewById(R.id.iv_avatar);
        iv_avatar.setImageResource(item.getAvatar());


        TextView tv_setting = (TextView) helper.itemView.findViewById(R.id.tv_setting);
        if (item.isChecked()) {
            tv_setting.setText("已设置");
            tv_setting.setOnClickListener(null);
        } else {
            tv_setting.setText("设置");
            tv_setting.setOnClickListener(v -> {
                        if (settingCallback != null) {
                            settingCallback.chooseItem(helper.getLayoutPosition());
                        }
                    }
            );
        }


    }
}
