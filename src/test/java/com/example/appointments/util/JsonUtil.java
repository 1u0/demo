package com.example.appointments.util;

import org.json.JSONTokener;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class JsonUtil {

    public static Object loadJson(String resourcePath) throws IOException {
        return new JSONTokener(new ClassPathResource(resourcePath).getInputStream()).nextValue();
    }
}
