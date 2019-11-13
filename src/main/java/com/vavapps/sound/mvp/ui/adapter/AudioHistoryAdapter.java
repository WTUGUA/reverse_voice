package com.vavapps.sound.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vavapps.sound.R;
import com.vavapps.sound.mvp.model.entity.AudioEntity;

import java.util.List;

public class AudioHistoryAdapter extends BaseQuickAdapter<AudioEntity, BaseViewHolder> {


    public AudioHistoryAdapter(@Nullable List<AudioEntity> data) {
        super(data);
        this.mLayoutResId = R.layout.item_audio_history;
    }


    private AudioPlayCallback audioPlayCallback;

    public interface AudioPlayCallback{
        void play(int position);

        void share(int position);
    }

    public void addAudioPlayCallback(AudioPlayCallback audioPlayCallback){
        this.audioPlayCallback = audioPlayCallback;
    }
    @Override
    protected void convert(BaseViewHolder helper, AudioEntity item) {

        helper.setText(R.id.tv_audio_title,item.getAudio_title());
        int totalTime = Math.round(item.getAudio_duration() / 1000);
        String str = String.format("%02d:%02d", totalTime / 60,
                totalTime % 60);
        helper.setText(R.id.tv_audio_time,str);



        ImageView iv_play = helper.itemView.findViewById(R.id.iv_state);

        if (!item.isPlaying()){
            iv_play.setImageResource(R.mipmap.icon_audio_play);
        }else {
            iv_play.setImageResource(R.mipmap.icon_audio_pause);
        }

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayCallback.play(helper.getLayoutPosition());
            }
        });


        ImageView iv_share = (ImageView) helper.itemView.findViewById(R.id.iv_share);

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayCallback.share(helper.getLayoutPosition());
            }
        });

    }
}
