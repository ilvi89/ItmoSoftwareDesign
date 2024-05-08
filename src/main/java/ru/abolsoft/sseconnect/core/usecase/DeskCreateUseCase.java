package ru.abolsoft.sseconnect.core.usecase;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.core.entity.Desk;
import ru.abolsoft.sseconnect.core.repository.DeskRepository;

@Service
@RequiredArgsConstructor
public class DeskCreateUseCase {
    private final DeskRepository deskRepository;

    public Res execute(Req req) {
        var desk = Desk.create(req.name);
        desk = deskRepository.save(desk);
        return new Res(desk.getId(), desk.getName(), desk.getMobile());
    }


    @Data
    @Builder
    public static class Req {
        private String name;
    }

    @Data
    @Builder
    public static class Res {
        private Long deskId;
        private String name;
        private boolean mobileConnect;
    }
}


