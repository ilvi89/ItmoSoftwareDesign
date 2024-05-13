package ru.abolsoft.sseconnect.infr.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.abolsoft.sseconnect.core.entity.Badge;
import ru.abolsoft.sseconnect.core.port.res.BadgeData;
import ru.abolsoft.sseconnect.core.repository.BadgePresetRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class CoreServiceAdapterTest {
    @Autowired
    private CoreServiceAdapter coreServiceAdapter;
    @Autowired
    private BadgePresetRepository badgePresetRepository;

    @Test
    void getBadgeForMember() {
        Optional<BadgeData> optionalBadgeData = coreServiceAdapter.getBadgeDataById(42L);
        assertTrue(optionalBadgeData.isPresent());
        var badge = optionalBadgeData.get();
        assertNotEquals(0, badge.getProperties().size());
    }

    @Test
    void getBadgeDataById() {
        var preset = badgePresetRepository.findById(UUID.fromString("2c576bb0-f00d-4157-a492-616fe4a97a7d"));
        Optional<Badge> optionalBadgeData = coreServiceAdapter.getBadgeForMember(2L, preset.get());
    }
}