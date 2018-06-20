/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.util;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.teasoft.rydeon.model.Users;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a custom deserializer for the Users class. Necessary for correct 
 * deserialization during token authentication.
 * @author Theodore Elikem Attigah
 */
public class UserDeserializer extends StdDeserializer<Users> {

    public UserDeserializer() { 
        this(null); 
    } 
 
    public UserDeserializer(Class<?> vc) { 
        super(vc); 
    }
    
    @Override
    public Users deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Long id = node.get("id").asLong();
        Boolean isActive = node.get("isActive").asBoolean();
        JsonNode arrayNode = node.get("roles");
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(new TypeReference<List<String>>(){});
        List<String> roles = reader.readValue(arrayNode);
        Users user = new Users();
        user.setId(id);
        user.setIsActive(isActive);
        user.setUserRoles(roles);
        return user;
    }

}
