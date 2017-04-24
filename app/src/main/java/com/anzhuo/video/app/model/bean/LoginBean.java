package com.anzhuo.video.app.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class LoginBean {


    /**
     * ret : 200
     * msg : 用户信息
     * LoginData : [{"id":"129","username":"29909","logo":"","vip_level":"0","vip_expire":"0","is_delete":"0"}]
     */

    private int ret;
    private String msg;
    private List<LoginData> LoginData;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<LoginData> getLoginData() {
        return LoginData;
    }

    public void setLoginData(List<LoginData> LoginData) {
        this.LoginData = LoginData;
    }


}
