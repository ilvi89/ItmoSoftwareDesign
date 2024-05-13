package ru.abolsoft.sseconnect.infr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import ru.abolsoft.sseconnect.core.entity.BadgePreset;
import ru.abolsoft.sseconnect.infr.repository.cashe.BadgePresetRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisTemplate<String, BadgePreset> redisTemplate(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisTemplate<String, BadgePreset> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setDefaultSerializer(new BadgePresetRedisSerializer(objectMapper));
        return template;
    }
}


