package picturebot.bot.command.webappdata;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class LocalTimeDeserializerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private JsonParser jsonParser;
    @Mock private JsonNode jsonNode;
    @Mock private DeserializationContext context;

    @InjectMocks private LocalTimeDeserializer deserializer;

    @Nested
    @DisplayName("deserialize()")
    class Deserialize {

        @Test
        @DisplayName("should correctly parse time without leading zero")
        void shouldCorrectlyParseTimeWithoutLeadingZero() throws IOException {
            when(jsonParser.getCodec().readTree(jsonParser)).thenReturn(jsonNode);
            when(jsonNode.asText()).thenReturn("7:00");

            final LocalTime actual = deserializer.deserialize(jsonParser, context);

            assertThat(actual, is(equalTo(LocalTime.of(7, 0, 0))));
        }

        @Test
        @DisplayName("should correctly parse time with leading zero")
        void shouldCorrectlyParseTimeWithLeadingZero() throws IOException {
            when(jsonParser.getCodec().readTree(jsonParser)).thenReturn(jsonNode);
            when(jsonNode.asText()).thenReturn("07:00");

            final LocalTime actual = deserializer.deserialize(jsonParser, context);

            assertThat(actual, is(equalTo(LocalTime.of(7, 0, 0))));
        }

        @Test
        @DisplayName("should correctly parse time when default constructor is used")
        void shouldCorrectlyParseTimeWhenDefaultConstructorIsUsed() throws IOException {
            deserializer = new LocalTimeDeserializer();
            when(jsonParser.getCodec().readTree(jsonParser)).thenReturn(jsonNode);
            when(jsonNode.asText()).thenReturn("07:00");

            final LocalTime actual = deserializer.deserialize(jsonParser, context);

            assertThat(actual, is(equalTo(LocalTime.of(7, 0, 0))));
        }
    }
}
