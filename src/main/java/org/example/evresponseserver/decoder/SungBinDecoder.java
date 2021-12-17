package org.example.evresponseserver.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evresponseserver.utils.ByteArrayManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SungBinDecoder extends ByteToMessageDecoder {
    private int DATA_LENGTH = 12;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf buf = in.readBytes(in.readableBytes());
        byte[] revMsg = new byte[DATA_LENGTH];
        buf.copy().readBytes(revMsg);
        log.info("decode {} {}", ByteArrayManager.toString(revMsg), buf.copy().readableBytes());
        out.add(buf.copy());
        in.clear();
    }
}