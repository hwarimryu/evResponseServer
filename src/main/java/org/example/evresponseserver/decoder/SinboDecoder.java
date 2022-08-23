package org.example.evresponseserver.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evresponseserver.utils.ByteArrayManager;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SinboDecoder extends ByteToMessageDecoder {
    private int PREFIX_LENGTH = 8;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!in.isReadable(PREFIX_LENGTH))
            return;

        int msgLength = Integer.parseInt(in.readCharSequence(PREFIX_LENGTH, StandardCharsets.UTF_8).toString());

        if (!in.isReadable(msgLength))
            return;

        String msg = in.readCharSequence(msgLength, StandardCharsets.UTF_8).toString();
        out.add(msg);
    }
}