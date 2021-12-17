package org.example.evresponseserver.Message.sungbin;


import io.netty.buffer.ByteBuf;
import org.example.evresponseserver.utils.ByteArrayManager;

/**
 * 성빈EV 연동 시 EV 호출에 사용하는 MSG
 * <p>
 * Created by hwarim on 2021-12-07
 */
public class SungBinCallMsg implements SungBinMsg {
    // Function(1byte), EV Count(1byte), Bytes Count(2byte)
    static final byte sendHeader = 0x1B;
    static final byte[] hallCallPrefix = {0x10, 0x01, 0x06, 0x00};
    static final byte[] destinationCallPrefix = {0x20, 0x01, 0x06, 0x00};

    protected ByteBuf msgByteBuf;
    protected String msg;

    public SungBinCallMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        if (msg == null || msg.isEmpty())
            msg = ByteArrayManager.toString(msgByteBuf.array());

        return msg;
    }
}
