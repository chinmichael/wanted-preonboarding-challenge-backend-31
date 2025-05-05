package com.wanted.cqrs.common.utils;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Mappers {

    private static final ObjectMapper OBJ_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static final ObjectReader OBJ_READER = OBJ_MAPPER.reader();
    public static final ObjectWriter OBJ_WRITER = OBJ_MAPPER.writer();

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)  // escape 컨트롤 처리
            .build();

    public static final ObjectReader JSON_READER = JSON_MAPPER.reader();
    public static final ObjectWriter JSON_WRITER = JSON_MAPPER.writer();
}
