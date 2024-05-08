package ru.abolsoft.sseconnect.infr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.qeuery.GetPreparedBadgeDataQuery;
import ru.abolsoft.sseconnect.core.usecase.BadgeAcceptUseCase;
import ru.abolsoft.sseconnect.core.usecase.BadgePrepareUseCase;
import ru.abolsoft.sseconnect.infr.controller.req.PrepareBadgeRequest;
import ru.abolsoft.sseconnect.infr.controller.res.BadgeDataResponse;
import ru.abolsoft.sseconnect.infr.controller.res.BadgeResponse;
import ru.abolsoft.sseconnect.infr.controller.sse.DossierAcceptedEvent;
import ru.abolsoft.sseconnect.infr.controller.sse.DossierInProcessEvent;
import ru.abolsoft.sseconnect.infr.repository.DeskRepositoryImpl;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("badge")
public class BadgeController {
    private final DeskRepositoryImpl<SseEmitter> deskRepository;
    private final BadgePrepareUseCase badgePrepareUseCase;
    private final BadgeAcceptUseCase badgeAcceptUseCase;
    private final GetPreparedBadgeDataQuery getPreparedBadgeDataQuery;

    @PostMapping
    public ResponseEntity<BadgeResponse> badgePrepare(@RequestBody final PrepareBadgeRequest req) {
        var useCaseReq = BadgePrepareUseCase.Req.builder()
                .deskId(req.getDeskId())
                .memberId(req.getMemberId())
                .build();
        BadgePrepareUseCase.Res res = badgePrepareUseCase.execute(useCaseReq);
        var emitter = deskRepository.findMappedObject(res.getDeskId()).orElseThrow(NotImplemented::new);
        var badgeResponse = BadgeResponse.builder()
                .id(res.getBadgeId())
                .status(res.getStatus())
                .deskId(res.getDeskId())
                .build();
        var event = SseEmitter.event()
                .name(DossierInProcessEvent.EVENT_NAME)
                .data(new DossierInProcessEvent(badgeResponse))
                .build();
        try {
            emitter.send(event);
        } catch (IOException e) {
            throw new NotImplemented();
        }


        return ResponseEntity.ok(badgeResponse);
    }

    @GetMapping
    public ResponseEntity<BadgeDataResponse> getPreparedBadge(@RequestParam(name = "badge_id") final Long badgeId) {
        var useCaseReq = GetPreparedBadgeDataQuery.Req.builder()
                .badgeId(badgeId)
                .build();
        var res = getPreparedBadgeDataQuery.execute(useCaseReq);


        return ResponseEntity
                .ok(BadgeDataResponse.builder()
                        .id(badgeId)
                        .data(res.getBadgeData())
                        .build());
    }


    @GetMapping
    public ResponseEntity<?> acceptPrepare(@RequestParam(name = "badge_id") final Long badgeId) {
        var useCaseReq = BadgeAcceptUseCase.Req.builder()
                .badgeId(badgeId)
                .build();
        var res = badgeAcceptUseCase.execute(useCaseReq);

        var emitter = deskRepository.findMappedObject(res.getDeskId())
                .orElseThrow(NotImplemented::new);

        var badgeResponse = BadgeResponse.builder()
                .id(res.getBadgeId())
                .status(res.getStatus())
                .status(res.getStatus())
                .build();

        var event = SseEmitter.event()
                .name(DossierAcceptedEvent.EVENT_NAME)
                .data(new DossierAcceptedEvent(badgeResponse))
                .build();
        try {
            emitter.send(event);
        } catch (IOException e) {
            throw new NotImplemented();
        }


        return ResponseEntity.ok(badgeResponse);
    }
}
