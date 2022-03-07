package com.test.poc.json.schema.validation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.SpecVersionDetector;
import com.networknt.schema.ValidationMessage;

/**
 * @author Abhishek.Kumar
 *
 */
public class ValidateJsonSchemaTest {
    
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode testJsonSchema = null;
    private String DRAFT_7_SCHEMA_URI="https://json-schema.org/draft-07/schema";
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateJsonSchemaTest.class);


    @Before
    public void setup() throws IOException {
      InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("json-validation-poc/test-custom-schema.json");
      String content = IOUtils.toString(resourceAsStream, Charset.defaultCharset());
      testJsonSchema = mapper.readTree(content);
    }
    
    @Test
    public void testAutoDetectJsonSchemaFromJsonNode() {
        JsonSchema schema = getJsonSchemaFromJsonNodeAutomaticVersion(testJsonSchema);
        Set<ValidationMessage> errors = schema.validate(testJsonSchema);
        
        if(errors.size() > 0) {
            LOGGER.error("testAutoDetectJsonSchemaFromJsonNode :: Validation Error %S", errors);
        }
        
        Assert.assertEquals("Error size", 0, errors.size());
        
    }
    
    @Test
    public void testJsonSchemaFromUrl() throws URISyntaxException {
        JsonSchema schema = getV7JsonSchemaFromUrl();
        Set<ValidationMessage> errors = schema.validate(testJsonSchema);
        
        if(errors.size() > 0) {
            LOGGER.error("testJsonSchemaFromUrl :: Validation Error '{}'", errors);
        }
        
        Assert.assertEquals("Error size", 0, errors.size());
    }
    
    @Test
    public void testJsonSchemaFromStringContent() throws IOException {
        JsonSchema schema = getV7JsonSchemaFromStringContent();
        Set<ValidationMessage> errors = schema.validate(testJsonSchema);
        
        if(errors.size() > 0) {
            LOGGER.error("testJsonSchemaFromStringContent :: Validation Error '{}'", errors);
        }
        
        Assert.assertEquals("Error size", 0, errors.size());
    }
    
    protected JsonSchema getJsonSchemaFromJsonNodeAutomaticVersion(JsonNode jsonNode) {
        VersionFlag versionFlag = SpecVersionDetector.detect(jsonNode);
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(versionFlag);
        JsonSchema schema = factory.getSchema(jsonNode);
        return schema;
    }
    
    protected JsonSchema getV7JsonSchemaFromUrl() throws URISyntaxException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        JsonSchema schema = factory.getSchema(new URI(DRAFT_7_SCHEMA_URI));
        return schema;
    }
    
    protected JsonSchema getV7JsonSchemaFromStringContent() throws IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("json-validation-poc/json-draft-07-schema.json");
        String schemaContent = IOUtils.toString(resourceAsStream, Charset.defaultCharset());
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        return factory.getSchema(schemaContent);
    }
}
