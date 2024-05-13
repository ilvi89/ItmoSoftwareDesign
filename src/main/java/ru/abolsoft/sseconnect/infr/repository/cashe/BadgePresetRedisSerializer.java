package ru.abolsoft.sseconnect.infr.repository.cashe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import ru.abolsoft.sseconnect.core.entity.BadgePreset;

import java.io.IOException;

public class BadgePresetRedisSerializer implements RedisSerializer<BadgePreset> {
    private final ObjectMapper objectMapper;

    public BadgePresetRedisSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(BadgePreset badgePreset) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(badgePreset);
        } catch (IOException e) {
            throw new SerializationException("Error serializing BadgePreset", e);
        }
    }

    @Override
    public BadgePreset deserialize(byte[] bytes) throws SerializationException {
        try {
            return objectMapper.readValue(bytes, BadgePreset.class);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing BadgePreset", e);
        }
    }
}

