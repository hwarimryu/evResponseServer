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
public class MissbcHandler extends ChannelInboundHandlerAdapter {
    private int DATA_LENGTH = 18;
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
     * sample data hall call req = 53545800020d007600120101000000455458
     * sample data hall call res = 53545801020d007600120901010000455458
     *
     * @param ctx
     * @param msg
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("channelRead {}", msg);
        ByteBuf response;// = Unpooled.buffer(msgBytes.length);
        byte[] callRes = {
                0x53, 0x54, 0x58,
                0x01, 0x02,
                0x0d,
                0x00, 0x76,
                0x00, 0x12,
                0x09,
                0x01,
                0x01,
                0x00, 0x00,
                0x45, 0x54, 0x58};
        log.info("{}", ByteArrayManager.toString(callRes));
        response = Unpooled.buffer(DATA_LENGTH);
        response.writeBytes(callRes);
        final ChannelFuture f = ctx.writeAndFlush(response);
        f.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        buff.clear();
    }

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