package ru.abolsoft.sseconnect.core.usecase;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.repository.DeskRepository;

@Service
@RequiredArgsConstructor
public class DeskMobileConnectUseCase {
    private final DeskRepository deskRepository;

    public Res execute(Req req) {
        var optionalDesk = deskRepository.findById(req.deskId);
        if (optionalDesk.isEmpty())
            throw new NotImplemented();
        var desk = optionalDesk.get();
        desk.setMobile(req.mobileIsActive);
        desk = deskRepository.save(desk);

        return new Res(desk.getId());
    }


    @Data
    @Builder
    public static class Req {
        private Long deskId;
        private boolean mobileIsActive;
    }

    @Data
    @Builder
    public static class Res {
        private Long deskId;
    }
}
