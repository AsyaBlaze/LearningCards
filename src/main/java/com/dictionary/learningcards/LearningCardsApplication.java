package com.dictionary.learningcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.function.BiFunction;

@SpringBootApplication
public class LearningCardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningCardsApplication.class, args);
    }

    @Bean
    public BiFunction<String, String, String> replaceOrAddParam() {
        return (paramName, newValue) -> ServletUriComponentsBuilder.fromCurrentRequest()
                .replaceQueryParam(paramName, newValue.replace(" ", "*"))
                .toUriString();
    }
}
