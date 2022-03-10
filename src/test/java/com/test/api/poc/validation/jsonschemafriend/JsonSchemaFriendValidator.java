package com.test.api.poc.validation.jsonschemafriend;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.Validator;

/**
 * @author Abhishek.Kumar
 *
 */
public class JsonSchemaFriendValidator {
    
    
    @Test
    public void testCustomSchema_V7() throws Exception {
        
        String customSchemaJson = getFileContent("json-validation-poc/test-custom-schema-07.json");
        String schemaUri = getSchemaUri(customSchemaJson);

        validateSchema(customSchemaJson, new URI(schemaUri)); 
    }
    @Test
    public void testCustomSchema_2019_09() throws Exception {
        
        String customSchemaJson = getFileContent("json-validation-poc/test-custom-schema-2019-09.json");
        String schemaUri = getSchemaUri(customSchemaJson);

        validateSchema(customSchemaJson, new URI(schemaUri)); 
    }
    
    @Test
    public void testCustomSchema_2019_09_Valid() throws Exception {
        
        String customSchemaJson = getFileContent("json-validation-poc/test-custom-schema-2019-09-valid.json");
        String schemaUri = getSchemaUri(customSchemaJson);

        validateSchema(customSchemaJson, new URI(schemaUri)); 
    }
    
    
    @Test
    public void testSchemaExample() throws Exception {
        
        String schema = getFileContent("json-validation-poc/test-custom-schema-2019-09-valid.json");
        String example = getFileContent("json-validation-poc/test-custom.example.json");

        validateSchema(example, schema); 
    }
    
    private static void validateSchema(String jsonMsgToValidate, Object schemaRef) throws Exception {
        Schema schema = getSchema(schemaRef);
        
        Validator validator = new Validator(); 
        validator.validateJson(schema, jsonMsgToValidate);
    }

    private static Schema getSchema(Object schemaRef) throws GenerationException, Exception {
        SchemaStore schemaStore = new SchemaStore(); // Initialize a SchemaStore.
        Schema schema = null;
        
        if(schemaRef instanceof URI) {
            schema = schemaStore.loadSchema((URI)schemaRef);
        }else if(schemaRef instanceof String) {
            schema = schemaStore.loadSchemaJson((String)schemaRef);
        }else {
            throw new Exception("Invalid schema type");
        }
        return schema;
    }
    
    private String getFileContent(String resourceFilePath) throws IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resourceFilePath);
        return IOUtils.toString(resourceAsStream, Charset.defaultCharset());
    }
    private String getSchemaUri(String customSchemaJson) {
        JsonObject schemaJsonObject = JsonParser.parseString(customSchemaJson).getAsJsonObject();
        return schemaJsonObject.get("$schema").getAsString();
    }

    
}
