package org.example.evresponseserver.Message.sungbin;

/**
 * 성빈EV 감시반과 주고 받는데 사용되는 MSG 포멧
 *
 * Created by hwarim on 2021-12-07
 */
public interface SungBinMsg {

    /**
     * Msg 기반 Data 전송 시 사용
     *
     * @return Msg
     */
    String getMsg();
}