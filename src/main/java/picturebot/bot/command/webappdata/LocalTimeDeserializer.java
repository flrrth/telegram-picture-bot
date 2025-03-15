package picturebot.bot.command.webappdata;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.io.Serial;
import java.time.LocalTime;

/**
 * A custom deserializer for the LocalTime type. It adds a zero to the left if needed, to prevent parsing problems.
 * e.g. 7:00 is changed to 07:00 before parsing.
 */
public class LocalTimeDeserializer extends StdDeserializer<LocalTime> {

    @Serial
    private static final long serialVersionUID = 1L;

    public LocalTimeDeserializer() {
        this(null);
    }

    public LocalTimeDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalTime deserialize(final JsonParser jsonParser,
                                 final DeserializationContext context) throws IOException {

        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return LocalTime.parse(String.format("%1$5s", node.asText()).replace(' ', '0'));
    }
}
