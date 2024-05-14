package ru.abolsoft.sseconnect.infr.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.query.GetPreparedBadgeDataQuery;
import ru.abolsoft.sseconnect.core.usecase.BadgeAcceptUseCase;
import ru.abolsoft.sseconnect.core.usecase.BadgePrepareUseCase;
import ru.abolsoft.sseconnect.infr.controller.req.PrepareBadgeRequest;
import ru.abolsoft.sseconnect.infr.controller.res.BadgeDataResponse;
import ru.abolsoft.sseconnect.infr.controller.res.BadgeResponse;
import ru.abolsoft.sseconnect.infr.controller.sse.DossierAcceptedEvent;
import ru.abolsoft.sseconnect.infr.controller.sse.DossierInProcessEvent;
import ru.abolsoft.sseconnect.infr.repository.DeskRepositoryImpl;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("badge")
public class BadgeController {
    private final DeskRepositoryImpl<SseEmitter> deskRepository;
    private final BadgePrepareUseCase badgePrepareUseCase;
    private final BadgeAcceptUseCase badgeAcceptUseCase;
    private final GetPreparedBadgeDataQuery getPreparedBadgeDataQuery;

    private final UUID baseBadgePresetId = UUID.fromString("2c576bb0-f00d-4157-a492-616fe4a97a7d");

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<BadgeResponse> badgePrepare(@RequestBody final PrepareBadgeRequest req) {
        var useCaseReq = BadgePrepareUseCase.Req.builder()
                .deskId(req.getDeskId())
                .memberId(req.getMemberId())
                .presetId(req.getPresetId())
                .build();
        BadgePrepareUseCase.Res res = badgePrepareUseCase.execute(useCaseReq);
        var emitter = deskRepository.findMappedObject(res.getDeskId());
        var badgeResponse = BadgeResponse.builder()
                .id(res.getBadgeId())
                .status(res.getStatus())
                .deskId(res.getDeskId())
                .build();
        if (emitter.isPresent()) {
            var event = SseEmitter.event()
                    .name(DossierInProcessEvent.EVENT_NAME)
                    .data(new DossierInProcessEvent(badgeResponse))
                    .build();
            try {
                emitter.get().send(event);
            } catch (IOException e) {
                throw new NotImplemented();
            }
        }


        return ResponseEntity.ok(badgeResponse);
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<BadgeDataResponse> getPreparedBadge(@PathVariable("id") final UUID badgeId) {
        var useCaseReq = GetPreparedBadgeDataQuery.Req.builder()
                .badgePresetId(baseBadgePresetId)
                .badgeId(badgeId)
                .build();
        var res = getPreparedBadgeDataQuery.execute(useCaseReq);


        return ResponseEntity
                .ok(BadgeDataResponse.builder()
                        .id(badgeId)
                        .data(res.getBadgeData())
                        .build());
    }


    @PostMapping("/{id}")
    public ResponseEntity<?> acceptPrepare(@PathVariable("id") final UUID badgeId) {
        var useCaseReq = BadgeAcceptUseCase.Req.builder()
                .badgeId(badgeId)
                .build();
        var res = badgeAcceptUseCase.execute(useCaseReq);

        var emitter = deskRepository.findMappedObject(res.getDeskId());

        var badgeResponse = BadgeResponse.builder()
                .id(res.getBadgeId())
                .status(res.getStatus())
                .deskId(res.getDeskId())
                .build();

        if (emitter.isPresent()) {
            var event = SseEmitter.event()
                    .name(DossierAcceptedEvent.EVENT_NAME)
                    .data(new DossierAcceptedEvent(badgeResponse))
                    .build();
            try {
                emitter.get().send(event);
            } catch (IOException e) {
                throw new NotImplemented();
            }
        }


        return ResponseEntity.ok(badgeResponse);
    }
}


