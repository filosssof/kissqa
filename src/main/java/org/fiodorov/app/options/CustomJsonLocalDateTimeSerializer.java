package org.fiodorov.app.options;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.data.convert.Jsr310Converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class CustomJsonLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        Long ms = Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(value).getTime();
        generator.writeNumber(ms);
    }

}
