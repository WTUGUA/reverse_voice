package com.vavapps.sound.mvp.model;

import com.google.gson.annotations.SerializedName;

public class WechatPayResultEntity {


    /**
     * code : 0
     * msg : null
     * res : {"appid":"wxa61b14bfaaac6004","partnerid":"1525928551","prepayid":"wx1418022466096123dd2eadcb4225911507","noncestr":"OscnEXVvHEsaVwpn","timestamp":1557828144,"package":"Sign=WXPay","sign":"4973D31506E87CC895A947592C0971C6"}
     */

    private int code;
    private Object msg;
    private ResBean res;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public static class ResBean {

        private String order_id;
        private PayData pay_data;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public PayData getPay_data() {
            return pay_data;
        }

        public void setPay_data(PayData pay_data) {
            this.pay_data = pay_data;
        }
    }

    public static class PayData{
        /**
         * appid : wxa61b14bfaaac6004
         * partnerid : 1525928551
         * prepayid : wx1418022466096123dd2eadcb4225911507
         * noncestr : OscnEXVvHEsaVwpn
         * timestamp : 1557828144
         * package : Sign=WXPay
         * sign : 4973D31506E87CC895A947592C0971C6
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private int timestamp;
        @SerializedName("package")
        private String packageX;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
