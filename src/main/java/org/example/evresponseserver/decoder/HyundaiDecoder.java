package org.example.evresponseserver.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HyundaiDecoder extends ByteToMessageDecoder {
    private boolean isStart(byte[] marker) {
        return marker[0] == 'S' && marker[1] == 'T' && marker[2] == 'X';
    }

    private boolean isEnd(byte[] marker) {
        return marker[0] == 'E' && marker[1] == 'T' && marker[2] == 'X';
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        ByteBuf buf = in.readBytes(in.readableBytes());
        byte[] revMsg = new byte[buf.readableBytes()];
        buf.copy().readBytes(revMsg);

        String msg = new String(revMsg, Charset.forName("ASCII"));
        log.info("decode {}", msg);
        out.add(buf);
        in.clear();
    }
}
