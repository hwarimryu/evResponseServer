package org.example.evresponseserver.Message.sungbin;


import java.util.List;

/**
 * 성빈EV 연동 시 상태정보 수신하였을 때 사용하는 MSG
 * <p>
 * Created by hwarim on 2021-12-07
 */
public class SungBinResponseMsg implements SungBinMsg {
    public static final byte revHeader = 0x1A;
    public static final byte errHeader = 0x02;
    private List<SungBinElevatorStatus> currentInfoList;

    public SungBinResponseMsg(List<SungBinElevatorStatus> list) {
        this.currentInfoList = list;
    }

    public boolean isCallResponse() {
        return currentInfoList.size() == 1;
    }

    public List<SungBinElevatorStatus> getCurrentInfoList() {
        return currentInfoList;
    }

    @Override
    public String getMsg() {
        return null;
    }

}
