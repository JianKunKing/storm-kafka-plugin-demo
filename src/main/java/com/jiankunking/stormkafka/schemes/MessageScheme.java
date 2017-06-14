package com.jiankunking.stormkafka.schemes;


import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

/**
 * Created by jiankunking on 2017/4/22 10:52.
 */
public class MessageScheme implements Scheme {

    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(MessageScheme.class);
    }

    public List<Object> deserialize(ByteBuffer byteBuffer) {
        String msg = this.getString(byteBuffer);
        return new Values(msg);
    }

    public Fields getOutputFields() {
        return new Fields("msg");
    }

    private String getString(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            //用这个的话，只能输出来一次结果，第二次显示为空
            // charBuffer = decoder.decode(buffer);
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            LOGGER.error("Cannot parse the provided message!" + ex.toString());
            return "error";
        }
    }
}
