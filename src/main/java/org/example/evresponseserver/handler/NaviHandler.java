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
public class NaviHandler extends ChannelInboundHandlerAdapter {
    private int DATA_LENGTH = 62;
    private ByteBuf buff;

    // 1 2 3 4 5 6 7 8 9

    // 1 2 3 4 5 6 7 8 9
    private int[] initStatus = {30, 12, 4, 5, 5, 6, 3, 4, 5};
    private int[] currentStatus = {1, 12, 4, 5, 5, 6, 3, 4, 5};


//    private int[] initStatus = {25, 25, 25, 20, 20, 20, 20, 30, 30, 35, 25, 25, 25, 20, 20, 20, 20, 30, 30, 35, 25, 25, 25, 20, 20, 20, 20, 30, 30, 35};
//    private int[] currentStatus = {25, 25, 25, 20, 20, 20, 20, 30, 30, 35, 25, 25, 25, 20, 20, 20, 20, 30, 30, 35, 25, 25, 25, 20, 20, 20, 20, 30, 30, 35};

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
     * 특정 호기에 대한 엘리베이터 제어 명령어
     * 주어진 층으로 엘리베이터 상향 호출
     * <p>
     * +------------+------------+----------+--------+------------+----------+----------+--------+
     * |  호출호기  | 사용예정   | 절대층   |  방향  | 승강기상태 | Door확인 | 사용예정 |
     * +------------+------------+----------+--------+------------+----------+----------+--------+
     * | 1bytes     |   4bytes   |   1byte  | 1byte  |   1byte    |   1byte  |   1byte  |
     * +------------+------------+----------+--------+------------+----------+----------+--------+
     *
     * @return 제어명령어
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("channelRead {}", msg);

        ByteBuf mBuf = (ByteBuf) msg;
        ByteBuf response;
        log.info("msg {}", mBuf.getByte(0));
        log.info("msg {}", mBuf.getByte(1));
        log.info("msg {}", mBuf.getByte(2));
        log.info("msg {}", mBuf.getByte(3));

        if (mBuf.getByte(3) == 2) {
            log.info("msg {}", msg);
            String res = "535458";
            res+="020100000000050001010000455458";
            log.info("res {}", res);
            byte[] tmp = ByteArrayManager.hexStringToByteArray(res);
            response = Unpooled.buffer(tmp.length);
            response.writeBytes(tmp);
        } else {
            String res = makeStatusResponse();
            log.info("res {}", res);
            response = Unpooled.buffer(res.length());
            response.writeBytes(res.getBytes());
        }
//        String res = makeStatusResponse();
//        log.info("res {}", res);
//        ByteBuf response = Unpooled.buffer(res.length());
//        0x53 0x54 0x58 0x02 0x01 0x00 0x00 0x00 0x00 0x02 0x00 0x01 0x01 0x00 0x00 0x45 0x54 0x58
//        if (mBuf.readableBytes() < 10) {
//        response.writeBytes(res.getBytes());
//            response = Unpooled.buffer(res.length());
//            response.writeBytes(res.getBytes());
//        } else {
//            res = "535458020100000000100000000000455458";
//
//            buff.writeBytes(ByteArrayManager.hexStringToByteArray(res));  // 클라이언트에서 보내는 데이터가 축적됨
//            mBuf.release();
//            response = Unpooled.buffer(buff.readableBytes());
////        response.writeBytes((byte[]) msg);
//            buff.getBytes(0, response, buff.readableBytes());
//        }

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
     * 한 번에 전체 호기에 대한 정보가 수신됨.
     *  => 각 호기 정보는 총 21bytes로 표현됨.
     *  승->원
     *  0x53 0x54 0x58
     *  0x01    상태응답이다.
     *  0x05    Car No
     *  0x00 0x00 0x00 0x00     Spare
     *  0x0a    Floor
     *  0x01    Direction   0x00    Stop    0x01    Up  0x02    Down
     *  0x01    Auto
     *  0x01    Door    0x02    Open
     *  0x00 0x00   Spare
     *  0x45 0x54 0x58
     *
     *
     * @return
     */
    private String makeStatusResponse() {
        String res = "535458";
//
//
//        for (int i = 0; i < 9; i++) {
        String floor = currentStatus[0] > 9 ? ("" + currentStatus[0]) : ("0" + currentStatus[0]);
        if (currentStatus[0] >= initStatus[0]) {
            res += "01" + "01" + "00000000" + floor + "0001020000";
            currentStatus[0]++;
        } else {
            res += "01" + "01" + "00000000" + floor + "0201010000";
            currentStatus[0] = 1;
//            }
        }

        res += "455458";
        return res;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        ctx.close();
        cause.printStackTrace();
    }

}