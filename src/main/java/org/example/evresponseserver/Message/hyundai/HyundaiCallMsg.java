package org.example.evresponseserver.Message.hyundai;

/**
 * 현대EV 연동 시 EV 호출에 사용하는 MSG
 *
 * Created by hwarim on 2021-12-09
 */
public class HyundaiCallMsg implements HyundaiMsg {
    protected String msg;

    public HyundaiCallMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
