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
public class SungBinHandler extends ChannelInboundHandlerAdapter {
    private int DATA_LENGTH = 12;
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
        ByteBuf mBuf = (ByteBuf) msg;
//        byte[] msgBytes = mBuf.array();

        buff.writeBytes(mBuf);  // 클라이언트에서 보내는 데이터가 축적됨
        mBuf.release();
        ByteBuf response = Unpooled.buffer(12);
        response.writeByte(0x1A);
        buff.getBytes(1,response,buff.readableBytes() - 2);
        byte checksum = 0x00;
        byte[] array = response.array();
        for (int idx = 1; idx < array.length; idx++) {
            checksum ^= array[idx];
        }

        response.writeByte(checksum);
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