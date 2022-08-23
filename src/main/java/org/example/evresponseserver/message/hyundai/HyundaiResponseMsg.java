package org.example.evresponseserver.message.hyundai;

import java.util.List;

/**
 * 현대EV 연동 시 상태정보 수신하였을 때 사용하는 MSG
 *
 * Created by hwarim on 2021-12-07
 */
public class HyundaiResponseMsg implements HyundaiMsg{
    private List<HyundaiElevatorStatus> currentInfoList;

    public HyundaiResponseMsg (List<HyundaiElevatorStatus> list) {
        this.currentInfoList = list;
    }

    public boolean isCallResponse() {
        return currentInfoList.size() == 1;
    }

    public List<HyundaiElevatorStatus> getCurrentInfoList() {
        return currentInfoList;
    }

    @Override
    public String getMsg() {
        return null;
    }
}
