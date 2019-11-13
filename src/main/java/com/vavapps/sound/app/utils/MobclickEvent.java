package com.vavapps.sound.app.utils;


//umeng埋点统计
public class MobclickEvent {


    /**
     * 首页
     */

    //首页页面展示次数
    public static final String HomePageView = "homePage_view";
    //首页点击标签草稿箱的次数
    public static final String HomePageDraft = "homePage_draft";
    //首页点击制作配音的次数
    public static final String HomePageCreate = "homePage_create";
    //首页点击任意一条配音进入详情的次数
    public static final String HomePageList = "homePage_list";
    //首页点击任意一条配音播放的次数
    public static final String HomePagePlay = "homePage_play";
    //首页点击分享
    public static final String HomePageVoiceShare = "homePageVoiceShare";


    //配音详情点击更多操作的次数
    public static final String VoiceDetailAction = "voiceDetail_action";
    //配音详情更多操作点击编辑的次数
    public static final String VoiceDetailActionEdit = "voiceDetailAction_edit";
    //配音详情更多操作点击删除的次数
    public static final String VoiceDetailActionDelete = "voiceDetailAction_delete";
    //配音详情更多操作点击取消的次数
    public static final String VoiceDetailActionCancle = "voiceDetailAction_cancle";
    //配音详情点击播放的次数
    public static final String VoiceDetailPlay = "voiceDetail_play";
    //配音详情点击标题的次数
    public static final String VoiceDetailTitle = "voiceDetail_title";


    /**
     * 制作配音
     */

    //制作配音页面总展示次数
    public static final String MakeVoiceView = "makeVoice_view";
    //制作配音页面点击播主按钮次数
    public static final String MakeVoiceAnnouncer = "makeVoice_announcer";

    //制作配音页面点击语速按钮次数
    public static final String MakeVoiceSpeed = "makeVoice_speed";
    //制作配音页面点击音量按钮次数
    public static final String MakeVoiceVolume = "makeVoice_volume";
    //制作配音页面点击返回键次数
    public static final String MakeVoiceBack = "makeVoice_back";
    //制作配音页面点击保存按钮次数
    public static final String MakeVoiceSave = "makeVoice_save";


    //设置语速操作框点击播放次数
    public static final String MakeVoicePlay = "makeVoice_play";
    //设置语速操作框点击取消次数
    public static final String MakeVoiceSpeedCancel = "makeVoiceSpeed_cancel";
    //设置语速操作框点击确认次数
    public static final String MakeVoiceSpeedSure = "makeVoiceSpeed_sure";

    //设置音量操作框点击取消次数
    public static final String MakeVoiceVolumeCancel = "makeVoiceVolume_cancel";
    //设置音量操作框点击确认次数
    public static final String MakeVoiceVolumeSure = "makeVoiceVolume_sure";


    /**
     * 配音生成
     */
    //配音保存页面总展示次数
    public static final String SaveVoiceView = "saveVoice_view";
    //配音保存页面点击播放次数
    public static final String SaveVoicePlay = "saveVoice_play";
    //配音保存页面点击标题次数
    public static final String SaveVoiceTitle = "saveVoice_title";
    //配音保存页面点击仅保存次数
    public static final String VoiceSave = "voiceShare";
    //配音保存页面点击分享并保存
    public static final String VoiceShareSave = "voiceShare_save";
    //录音开
    public static final String  RocordOpen= "rocord_open";
    //录音关
    public static final String  RocordClose= "rocord_close";
    //播放
    public static final String  RocordPlay= "rocord_play";
    //倒放
    public static final String  RocordBack= "rocord_play_back";
    public static final String  AudioPlaysBack= "Audio_plays_back";




    /**
     * 草稿箱
     */
    //草稿箱页面展示次数
    public static final String DraftView = "draft_view";
    //草稿箱页面点击任意一条配音进入制作配音页面的次数
    public static final String DraftList = "draft_list";
    //草稿箱页面点击管理的次数
    public static final String DraftManage = "draft_manage";
    //草稿箱管理页面点击全选的次数
    public static final String DraftManageAllSelcet = "draftManage_allSelcet";
    //草稿箱管理页面点击任意一条数据单选的次数
    public static final String draftManageSelcet = "draftManage_Selcet";
    //草稿箱管理页面点击删除的次数
    public static final String draftManageDelete = "draftManage_delete";


}
