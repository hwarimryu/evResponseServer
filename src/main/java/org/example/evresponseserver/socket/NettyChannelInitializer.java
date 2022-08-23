package org.example.evresponseserver.socket;

import com.sun.net.httpserver.HttpHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evresponseserver.config.EvCrt;
import org.example.evresponseserver.decoder.*;
import org.example.evresponseserver.handler.*;
import org.example.evresponseserver.utils.SslUtil;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private EvCrt crt;

    public void setCrt(EvCrt crt) {
        this.crt = crt;
    }

    // 클라이언트 소켓 채널이 생성될 때 호출
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        switch (crt) {
            case NAVI:
                NaviHandler naviHandler = new NaviHandler();
                NaviDecoder naviDecoder = new NaviDecoder();

                log.info("initChannel");
                // 뒤이어 처리할 디코더 및 핸들러 추가
                pipeline.addLast(naviDecoder);
                pipeline.addLast(naviHandler);
                break;
            case HYUNDAI:
                HyundaiHandler hyundaiHandler = new HyundaiHandler();
                HyundaiDecoder hyundaiDecoder = new HyundaiDecoder();

                log.info("initChannel");
                // 뒤이어 처리할 디코더 및 핸들러 추가
                pipeline.addLast(hyundaiDecoder);
                pipeline.addLast(hyundaiHandler);
                break;
            case OTIS:
                OtisHandler otisHandler = new OtisHandler();
                OtisDecoder otisDecoder = new OtisDecoder();

                log.info("initChannel");
                // 뒤이어 처리할 디코더 및 핸들러 추가
                pipeline.addLast(otisDecoder);
                pipeline.addLast(otisHandler);
                break;
            case CHEONJO:
                CheonjoHandler cheonjoHandler = new CheonjoHandler();
                CheonjoDecoder cheonjoDecoder = new CheonjoDecoder();

                log.info("initChannel");

                pipeline.addLast(cheonjoDecoder);
                pipeline.addLast(cheonjoHandler);
                break;
            case COMMAX:
                CommaxHandler commaxHandler = new CommaxHandler();
                CommaxDecoder commaxDecoder = new CommaxDecoder();

                log.info("initChannel");

                pipeline.addLast(commaxDecoder);
                pipeline.addLast(commaxHandler);
                break;
            case SINBO:
                SinboHandler sinboHandler = new SinboHandler();
                SinboDecoder sinboDecoder = new SinboDecoder();

                log.info("initChannel");

                pipeline.addLast(sinboDecoder);
                pipeline.addLast(sinboHandler);
                break;

            case MISSBC:
                MissbcHandler missbcHandler = new MissbcHandler();
                MissbcDecoder missbcDecoder = new MissbcDecoder();

                log.info("initChannel");

                pipeline.addLast(missbcDecoder);
                pipeline.addLast(missbcHandler);
                break;

            case HOMENET_HDC:
                SslUtil sslUtil = new SslUtil();
//                sslUtil.setCaPath("config/cert/CA.crt");
                sslUtil.setCertPath("config/cert/netty.crt");
                sslUtil.setGetKeyPath("config/cert/pkey.pem");

                SslContext sslCtx = null;
                try {
                    sslCtx = sslUtil.getCertificate();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (sslCtx != null) {
                    pipeline.addLast(sslCtx.newHandler(ch.alloc()));
                    pipeline.addLast(new HdcHomenetDecoder());
                    pipeline.addLast(new HttpResponseEncoder());
                    pipeline.addLast(new HttpObjectAggregator(65536));
                    pipeline.addLast(new HdcHomenetHandler());
                    log.info("initChannel");
                } else
                    log.info("initChannel failed ");

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