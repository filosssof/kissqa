package org.fiodorov.app.options;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.google.common.base.CharMatcher;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class CustomStringDeserialazer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String nodeValue = StringDeserializer.instance.deserialize(p, ctxt);
        nodeValue = CharMatcher.whitespace().trimFrom(nodeValue);
        return nodeValue;
    }

}
