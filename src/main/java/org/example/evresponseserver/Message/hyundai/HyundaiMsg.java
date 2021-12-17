package org.example.evresponseserver.Message.hyundai;

/**
 * 현대EV 감시반과 주고 받는데 사용되는 MSG 포멧
 *
 * Created by hyeseon on 2021-06-10
 */
public interface HyundaiMsg {

    /**
     * Msg 기반 Data 전송 시 사용
     *
     * @return Msg
     */
    String getMsg();

}
