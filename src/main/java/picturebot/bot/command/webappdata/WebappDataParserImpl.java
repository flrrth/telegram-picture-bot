package picturebot.bot.command.webappdata;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class that parses the JSON data from the webapp into a {@link WebappData} object.
 */
@Component
class WebappDataParserImpl implements WebappDataParser {

    private final ObjectMapper objectMapper;

    /* default */ WebappDataParserImpl(final ObjectMapper customObjectMapper) {
        this.objectMapper = customObjectMapper;
    }

    @Override
    public WebappData parse(final String data) throws JsonProcessingException {
        return objectMapper.readValue(data, WebappData.class);
    }    
}
