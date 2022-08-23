package org.example.evresponseserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evresponseserver.utils.ByteArrayManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class CommaxHandler extends ChannelInboundHandlerAdapter {
    private int DATA_LENGTH = 17;
    private ByteBuf buff;

    private int[] initStatus = {39, 39, 39, 39, 39, 39, 39, 39, 39};
    private int[] currentStatus = {10, 12, 20, 20, 20, 20, 30, 25, 15};

    // 핸들러가 생성될 때 호출되는 메소드
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buff = ctx.alloc().buffer(DATA_LENGTH);
    }

    // 핸들러가 제거될 때 호출되는 메소드
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buff = null;
    }

    // 클라이언트와 연결되어 트래픽을 생성할 준비가 되었을 때 호출되는 메소드
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("Remote Address: " + remoteAddress);
    }


    /**
     * sample data hall call req = a5000f03000a000000060000010000015a
     * sample data hall call res = a5000f04000a00030006 01 0002 00 00 01 5a
     * @param ctx
     * @param msg
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("channelRead {}", msg);

        ByteBuf mBuf = (ByteBuf) msg;
        String res = "";
        ByteBuf response;// = Unpooled.buffer(msgBytes.length);

        byte reqType = mBuf.getByte(3);


        if(reqType == 0x03 || reqType == 0x0d) {
            byte[] callRes = {(byte) 0xA5, 0x00, (byte) 0x0F, 0x04, 0x00, 0x0A,
                    0x00, 0x66, // 동
                    0x00, 0x00, // ev 호기
                    0x00,   // 운행정보
                    0x00, 0x00,  // 층 정보
                    0x00,   //운행 방향
                    0x00,   //도어 상태
                    0x00,   // 호출 방향
                    0x5A};
            log.info("{}", ByteArrayManager.toString(callRes));
            response = Unpooled.buffer(17);
            response.writeBytes(callRes);
        } else {
            byte[] statusRes = {(byte) 0xA5, 0x00, (byte) 0x23, 0x02,
                    0x00, 0x1E,
                    0x00, 0x0a, 0x00, 0x01, 0x01, 0x00, 0x02, 0x00, 0x01, 0x00,
                    0x00, 0x0a, 0x00, 0x02, 0x01, 0x00, 0x03, 0x01, 0x00, 0x01,
                    0x00, 0x0a, 0x00, 0x03, 0x02, 0x00, 0x10, 0x02, 0x00, 0x03,
                    0x5A
            };

            log.info("{}", ByteArrayManager.toString(statusRes));
            response = Unpooled.buffer(37);
            response.writeBytes(statusRes);
        }

//        final ChannelFuture f = ctx.writeAndFlush(response);
        final ChannelFuture f = ctx.writeAndFlush(response);
        f.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        buff.clear();
    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, SungBinCallMsg msg) throws Exception {
//        log.info("Elevator call : {}", msg.getMsg());
//
//    }

    /**
     * STX 0103 06 01      04   2   2     00    0000000000000000 0000000000000000 0000000000000000 ETX
     * 동 호기      운행정보  층수 방향 도어  정지예정층 카호출             상향호출            하향호출
     * <p>
     * 방향  '0' : 방향없음, '1' : Up 방향, ‘2’ : Down 방향
     * 문열림  '0' : Door Closed, '2' : Door Opened
     *
     * @return
     */
    private String makeStatusResponse() {
        String res = "STX";
        for (int i = 0; i < 9; i++) {
            String floor = currentStatus[i] > 9 ? ("" + currentStatus[i]) : ("0" + currentStatus[i]);

            String evId = (i < 9) ? ("0" + (i + 1)) : ("" + (i + 1));

            if (evId.equals("02")) {
                res += "0205" + evId + "07" + floor + "2000000000000000000000000000000000000000000000000000";
            } else {
                if (currentStatus[i] == 6) {
                    res += "0205" + evId + "01" + floor + "2000000000000000000000000000000000000000000000000000";
                    currentStatus[i]--;
                } else if (currentStatus[i] > 1) {
                    res += "0205" + evId + "01" + floor + "2000000000000000000000000000000000000000000000000000";
                    currentStatus[i]--;
                } else {
                    res += "0205" + evId + "01" + floor + "0200000000000000000000000000000000000000000000000000";
                    currentStatus[i] = initStatus[i];
                }
            }
        }

        log.info("makeStatusResponse : \n{}", res);
        return res + "ETX";
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        ctx.close();
        cause.printStackTrace();
    }

}