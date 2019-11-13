package com.vavapps.sound.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.vavapps.sound.R;
import com.vavapps.sound.app.utils.Constans;
import com.vavapps.sound.mvp.event.ChooseMan;
import com.vavapps.sound.mvp.model.entity.Player;
import com.vavapps.sound.mvp.ui.adapter.PlayerAdapter;

import org.simple.eventbus.EventBus;

import java.util.List;

import static com.vavapps.sound.app.utils.PlayerManUtils.Ordinary;
import static com.vavapps.sound.app.utils.PlayerManUtils.getInstance;

public class PlayerFragment extends Fragment {

    private static final String TYPE = "type";
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    Unbinder unbinder;


    private List<Player> players;

    private int type = Ordinary;


    public static PlayerFragment newInstance(int type) {
        PlayerFragment playerFragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        playerFragment.setArguments(bundle);
        return playerFragment;
    }


    private List<Player> getPlayers(int type) {
        return getInstance().getByType(type);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhubo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE);
        }
        players = getPlayers(type);


        String voicer = DataHelper.getStringSF(getActivity(), Constans.VoicerPreference);

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerValue().equals(voicer)){
                players.get(i).setChecked(true);
            }
        }

        PlayerAdapter playerAdapter = new PlayerAdapter(this.players);

        playerAdapter.setSettingCallback(new PlayerAdapter.SettingCallback() {
            @Override
            public void chooseItem(int position) {
                String playerValue = players.get(position).getPlayerValue();
                DataHelper.setStringSF(getActivity(), Constans.VoicerPreference, playerValue);
                EventBus.getDefault().post(new ChooseMan());

            }
        });

        ArmsUtils.configRecyclerView(rvContent, new LinearLayoutManager(getActivity()));
        rvContent.setAdapter(playerAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
