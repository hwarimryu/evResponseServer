package org.example.evresponseserver.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evresponseserver.config.EvCrt;
import org.example.evresponseserver.decoder.HyundaiDecoder;
import org.example.evresponseserver.decoder.SungBinDecoder;
import org.example.evresponseserver.handler.HyundaiHandler;
import org.example.evresponseserver.handler.SungBinHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final EvCrt crt = EvCrt.SUNGBIN;

    // 클라이언트 소켓 채널이 생성될 때 호출
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        switch (crt) {
            case HYUNDAI :
                HyundaiHandler hyundaiHandler = new HyundaiHandler();
                HyundaiDecoder hyundaiDecoder = new HyundaiDecoder();

                log.info("initChannel");
                // 뒤이어 처리할 디코더 및 핸들러 추가
                pipeline.addLast(hyundaiDecoder);
                pipeline.addLast(hyundaiHandler);
                break;
            case SUNGBIN:
            default:
                SungBinHandler sungBinHandler = new SungBinHandler();
                SungBinDecoder sungBinDecoder = new SungBinDecoder();

                log.info("initChannel");
                // 뒤이어 처리할 디코더 및 핸들러 추가
                pipeline.addLast(sungBinDecoder);
                pipeline.addLast(sungBinHandler);
                break;
        }

    }
}