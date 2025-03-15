package picturebot.bot.command.webappdata;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Interface for parsing JSON string data into WebappData objects.
 */
interface WebappDataParser {
    
    /**
     * Parses the given JSON string data into a WebappData object.
     *
     * @param data the JSON string to be parsed
     * @return the parsed WebappData object
     * @throws JsonProcessingException if there is an error processing the JSON data
     */
    WebappData parse(String data) throws JsonProcessingException;
}
