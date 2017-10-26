package io.dropwizard.logging.json.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.io.Serializable;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DropwizardJsonFormatterTest {

    private final Map<String, Serializable> map = ImmutableMap.of("name", "Jim", "hobbies",
        ImmutableList.of("Reading", "Biking", "Snorkeling"));
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();

    @Test
    public void testNoPrettyPrintNoLineSeparator() {
        DropwizardJsonFormatter formatter = new DropwizardJsonFormatter(objectMapper, false, false);
        assertThat(formatter.toJson(map)).isEqualTo(
            "{\"name\":\"Jim\",\"hobbies\":[\"Reading\",\"Biking\",\"Snorkeling\"]}");
    }


    @Test
    public void testNoPrettyPrintWithLineSeparator() {
        DropwizardJsonFormatter formatter = new DropwizardJsonFormatter(objectMapper, false, true);
        assertThat(formatter.toJson(map)).isEqualTo(
            "{\"name\":\"Jim\",\"hobbies\":[\"Reading\",\"Biking\",\"Snorkeling\"]}\n");
    }

    @Test
    public void testPrettyPrintWithLineSeparator() {
        DropwizardJsonFormatter formatter = new DropwizardJsonFormatter(objectMapper, true, true);
        assertThat(formatter.toJson(map))
            .isEqualTo("{\n" +
                "  \"name\" : \"Jim\",\n" +
                "  \"hobbies\" : [ \"Reading\", \"Biking\", \"Snorkeling\" ]\n" +
                "}\n");
    }

    @Test
    public void testPrettyPrintNoLineSeparator() {
        DropwizardJsonFormatter formatter = new DropwizardJsonFormatter(objectMapper, true, false);
        assertThat(formatter.toJson(map))
            .isEqualTo("{\n" +
                "  \"name\" : \"Jim\",\n" +
                "  \"hobbies\" : [ \"Reading\", \"Biking\", \"Snorkeling\" ]\n" +
                "}");
    }
}
