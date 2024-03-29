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
public class HyundaiHandler extends ChannelInboundHandlerAdapter {
    private int DATA_LENGTH = 62;
    private ByteBuf buff;

    // 1 2 3 4 5 6 7 8 9
//    private int[] initStatus = {25, 25, 25, 20, 20, 20, 20, 30, 5, 20, 25, 25, 25, 20, 20, 20, 20, 30, 30, 35, 25, 25, 25, 20, 20, 20, 20, 30, 30, 35};
//    private int[] currentStatus = {25, 25, 25, 20, 20, 20, 20, 30, 5, 20, 25, 25, 25, 20, 20, 20, 20, 30, 30, 35, 25, 25, 25, 20, 20, 20, 20, 30, 30, 35};

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


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("channelRead {}", msg);

        ByteBuf mBuf = (ByteBuf) msg;
        String res = "";
        ByteBuf response;// = Unpooled.buffer(msgBytes.length);
        if (mBuf.readableBytes() < 9) {
            res = makeStatusResponse();
            response = Unpooled.buffer(res.length());
            response.writeBytes(res.getBytes());
        } else {
//            res = "STX01010101042200000000000000000000000000000000000000000000000000ETX";
            res = "STX01010201042200000000000000000000000000000000000000000000000000ETX";

//STX 0103 06 01      04   2   2     00    0000000000000000 0000000000000000 0000000000000000 ETX
//     동 호기 운행정보 층수 방향 도어 정지예정층 카호출             상향호출            하향호출
            byte[] msgBytes = res.getBytes();
            buff.writeBytes(msgBytes);  // 클라이언트에서 보내는 데이터가 축적됨
            mBuf.release();
            response = Unpooled.buffer(msgBytes.length);
//        response.writeBytes((byte[]) msg);
            buff.getBytes(0, response, msgBytes.length);
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