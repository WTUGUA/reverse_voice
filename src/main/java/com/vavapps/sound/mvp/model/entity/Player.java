package com.vavapps.sound.mvp.model.entity;

public class Player {

    private String PlayerName;

    private String PlayerValue;

    private int type;

    private String typeName;

    private int avatar;

    private int voice;


    public Player(String playerName, String playerValue, int type, String typeName, int avatar, int voice) {
        PlayerName = playerName;
        PlayerValue = playerValue;
        this.type = type;
        this.typeName = typeName;
        this.avatar = avatar;
        this.voice = voice;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    private boolean isChecked = false;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPlayerValue() {
        return PlayerValue;
    }

    public void setPlayerValue(String playerValue) {
        PlayerValue = playerValue;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public void setPlayerName(String playerName) {
        PlayerName = playerName;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
