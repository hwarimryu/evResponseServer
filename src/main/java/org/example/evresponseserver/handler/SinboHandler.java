package org.example.evresponseserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class SinboHandler extends ChannelInboundHandlerAdapter {
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

        String resOk = "&Resul=ok";
        String resFail = "&Result=fail&ErrMsg=고장";

        String res = msg + resOk;
        String length = String.format("%8s", res.length()).replace(" ", "0");
        res = length + res;
        log.info("res {}", res);
        ByteBuf response;
        response = Unpooled.buffer(res.getBytes(StandardCharsets.UTF_8).length);
        response.writeBytes(res.getBytes(StandardCharsets.UTF_8));

        final ChannelFuture f = ctx.writeAndFlush(response);
        f.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        cause.printStackTrace();
    }
}