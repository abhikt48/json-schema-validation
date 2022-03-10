package com.test.api.poc.validation.jsonschemafriend;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.Validator;

public class TestSchema {

    @Test
    public void testCustomSchema_2019_09_Valid() throws Exception {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("json-validation-poc/test-custom-schema-2019-09-valid.json");
        String schemaAsJson = IOUtils.toString(resourceAsStream, Charset.defaultCharset());

        JsonObject schemaJsonObject = JsonParser.parseString(schemaAsJson).getAsJsonObject();
        String schemaUriRef = schemaJsonObject.get("$schema").getAsString();

        SchemaStore schemaStore = new SchemaStore(); // Initialize a SchemaStore.
        Schema schema = schemaStore.loadSchema(new URI(schemaUriRef));

        Validator validator = new Validator();
        validator.validateJson(schema, schemaAsJson); // Throw Validation exception for failure1`
    }
}
