package uk.co.codesatori.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JsonToMapConverter implements AttributeConverter<Map<String, Object>, String> {

  /* Converts json text to a map, mapping attributes to objects.*/
  @Override
  public Map<String, Object> convertToEntityAttribute(String jsonText) {
    /* A null json text represents an empty json object.
     * As such, an empty map is returned by default. */
    if (jsonText != null) {
      try {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonText, HashMap.class);
      } catch (IOException e) {
        throw new JsonParseException("Failed to convert the following text to JSON: " + jsonText);
      }
    }

    return new HashMap<>();
  }

  /* Converts a map to json text.*/
  @Override
  public String convertToDatabaseColumn(Map<String, Object> attributeToObjectMap) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(attributeToObjectMap);
    } catch (JsonProcessingException e) {
      throw new JsonParseException("Failed to convert map to JSON text.");
    }
  }
}
