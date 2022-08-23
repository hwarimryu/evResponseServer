package org.example.evresponseserver.decoder;

import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequestDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HdcHomenetDecoder extends HttpRequestDecoder {
    public HdcHomenetDecoder() {
        super();
    }

    @Override
    protected HttpMessage createMessage(String[] initialLine) throws Exception {
        log.info("crated {}", super.createMessage(initialLine));
        return super.createMessage(initialLine);
    }
}
