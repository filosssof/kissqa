package org.fiodorov.app.options;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.convert.Jsr310Converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class CustomJsonLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = parser.getCodec();
        Object node = oc.readTree(parser);
        if (node instanceof NumericNode) {
            Long timestamp = ((NumericNode) node).asLong();
            return Jsr310Converters.DateToLocalDateTimeConverter.INSTANCE.convert(new Date(timestamp));
        } else if (node instanceof TextNode) {
            String timestamp = ((TextNode) node).asText();
            return Jsr310Converters.DateToLocalDateTimeConverter.INSTANCE.convert(new Date(Long.parseLong(timestamp)));
        } else {
            throw JsonMappingException.from(parser, node.toString() + " is expected to be timestamp");
        }

    }

}
