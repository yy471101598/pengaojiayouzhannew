package com.shoppay.paojyz.bean;

import java.util.List;

/**
 * Created by songxiaotao on 2017/7/5.
 */

public class VipInfoMsg {


    /**
     * flag : 1
     * msg : 会员正常
     * vdata : [{"MemID":"6","MemCard":"0011","MemName":"我","MemMobile":"18781954455","MemPoint":"0","MemMoney":"0.00","LevelName":"一级会员","Discount":"100","DiscountPoint":"0"}]
     * OilList:[{"OilID":"2","OilCode":"0","OilName":"0#柴油","OilTypeID":"2","OilPrice":"5.0000","OilShopID":"1","OilDiscountMoney":"0.3000","OilPoint":"1"},{"OilID":"4","OilCode":"10","OilName":"10#柴油","OilTypeID":"2","OilPrice":"5.8500","OilShopID":"1","OilDiscountMoney":"0.0000","OilPoint":"0"}]
     * print : [{"printNumber":0,"printContent":""}]
     */

    private int flag;
    private String msg;
    private List<VipInfo> vdata;
    private List<PrintBean> print;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<VipInfo> getVdata() {
        return vdata;
    }

    public void setVdata(List<VipInfo> vdata) {
        this.vdata = vdata;
    }

    public List<PrintBean> getPrint() {
        return print;
    }

    public void setPrint(List<PrintBean> print) {
        this.print = print;
    }


    public static class PrintBean {
        /**
         * printNumber : 0
         * printContent :
         */

        private int printNumber;
        private String printContent;

        public int getPrintNumber() {
            return printNumber;
        }

        public void setPrintNumber(int printNumber) {
            this.printNumber = printNumber;
        }

        public String getPrintContent() {
            return printContent;
        }

        public void setPrintContent(String printContent) {
            this.printContent = printContent;
        }
    }
}
