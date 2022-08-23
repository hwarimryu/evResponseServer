package org.example.evresponseserver.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evresponseserver.utils.ByteArrayManager;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaviDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

        ByteBuf buf = in.readBytes(in.readableBytes());
        byte[] revMsg = new byte[buf.readableBytes()];
        buf.copy().readBytes(revMsg);

        String msg = ByteArrayManager.toString(revMsg);
        log.info("decode {}", msg);
        out.add(buf);
        in.clear();
    }
}