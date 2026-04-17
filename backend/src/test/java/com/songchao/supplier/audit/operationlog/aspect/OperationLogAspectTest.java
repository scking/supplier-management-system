package com.songchao.supplier.audit.operationlog.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationLogAspectTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void toJsonShouldReturnValidJsonWhenPayloadIsTooLarge() throws Exception {
        OperationLogAspect aspect = new OperationLogAspect(null, objectMapper);
        String longText = "x".repeat(5000);

        String json = invokeToJson(aspect, Map.of("remark", longText));

        JsonNode node = assertDoesNotThrow(() -> objectMapper.readTree(json));
        assertTrue(node.isObject());
        assertTrue(json.length() <= 2000);
    }

    @Test
    void toJsonShouldReturnValidJsonWhenSerializationFails() throws Exception {
        OperationLogAspect aspect = new OperationLogAspect(null, objectMapper);

        String json = invokeToJson(aspect, new SelfReference());

        JsonNode node = assertDoesNotThrow(() -> objectMapper.readTree(json));
        assertTrue(node.isObject());
        assertTrue(json.length() <= 2000);
    }

    private String invokeToJson(OperationLogAspect aspect, Object value) throws Exception {
        Method method = OperationLogAspect.class.getDeclaredMethod("toJson", Object.class);
        method.setAccessible(true);
        return (String) method.invoke(aspect, value);
    }

    private static final class SelfReference {
        public final SelfReference self = this;
    }
}
