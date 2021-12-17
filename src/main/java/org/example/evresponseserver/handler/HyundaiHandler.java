package org.example.evresponseserver.handler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class HyundaiHandler extends ChannelInboundHandlerAdapter {
    private int DATA_LENGTH = 62;
    private ByteBuf buff;

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
        String res="";
        ByteBuf response;// = Unpooled.buffer(msgBytes.length);
        if (mBuf.readableBytes() < 10) {
            res = "STX01010101011201000000000000000000000000000000000000000000000000010102010512010000000000000000000000000000000000000000000000000101030101120100000000000000000000000000000000000000000000000001010401011201000000000000000000000000000000000000000000000000ETX";
            response = Unpooled.buffer(res.length());
            response.writeBytes(res.getBytes());
        } else {
            byte[] msgBytes = {'S','T','X','E','T','X'};
            res = "STX01010101050000000000000000000000000000000000000000000000000ETX";
//            STX 11000000000000000000000000000000000001
//STX 0103 06 01      04   2   2     00    0000000000000000 0000000000000000 0000000000000000 ETX
//     동 호기 운행정보 층수 방향 도어 정지예정층 카호출             상향호출            하향호출
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        ctx.close();
        cause.printStackTrace();
    }

}