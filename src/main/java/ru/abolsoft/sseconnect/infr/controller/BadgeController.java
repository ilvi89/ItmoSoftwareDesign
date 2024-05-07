package ru.abolsoft.sseconnect.infr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.usecase.BadgeAcceptUseCase;
import ru.abolsoft.sseconnect.core.usecase.BadgePrepareUseCase;
import ru.abolsoft.sseconnect.infr.controller.req.PrepareBadgeRequest;
import ru.abolsoft.sseconnect.infr.controller.res.AcceptBadgeResponse;
import ru.abolsoft.sseconnect.infr.controller.res.BadgeStatusResponse;
import ru.abolsoft.sseconnect.infr.controller.res.PrepareBadgeResponse;
import ru.abolsoft.sseconnect.infr.repository.DeskRepositoryImpl;

import java.io.IOException;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("badge")
public class BadgeController {
    private final DeskRepositoryImpl<SseEmitter> deskRepository;
    private final BadgePrepareUseCase badgePrepareUseCase;
    private final BadgeAcceptUseCase badgeAcceptUseCase;

    @PostMapping
    public ResponseEntity<PrepareBadgeResponse> badgePrepare(@RequestBody final PrepareBadgeRequest req) {
        var useCaseReq = BadgePrepareUseCase.Req.builder()
                .deskId(req.getDeskId())
                .memberId(req.getMemberId())
                .build();
        BadgePrepareUseCase.Res res = badgePrepareUseCase.execute(useCaseReq);
        var emitter = deskRepository.findMappedObject(res.getDeskId()).orElseThrow(NotImplemented::new);
        var dataToSend = new HashMap<String, Object>();
        var eventData = BadgeStatusResponse.builder()
                .id(res.getBadgeId())
                .status(res.getStatus())
                .build();
        dataToSend.put("desk", eventData);
        var event = SseEmitter.event()
                .name("dossier_in_process_event")
                .data(dataToSend)
                .build();
        try {
            emitter.send(event);
        } catch (IOException e) {
            throw new NotImplemented();
        }


        return ResponseEntity.ok(PrepareBadgeResponse.builder()
                .status(res.getStatus())
                .badgeId(res.getBadgeId())
                .build()
        );
    }


    @GetMapping
    public ResponseEntity<?> acceptPrepare(@RequestParam(name = "badge_id") final Long badgeId) {
        var useCaseReq = BadgeAcceptUseCase.Req.builder()
                .badgeId(badgeId)
                .build();
        var res = badgeAcceptUseCase.execute(useCaseReq);
        var data = res.getBadgeData();

        var emitter = deskRepository.findMappedObject(res.getBadgeData().getDeskId())
                .orElseThrow(NotImplemented::new);
        var dataToSend = new HashMap<String, Object>();
        var eventData = BadgeStatusResponse.builder()
                .id(res.getBadgeData().getId())
                .status(res.getBadgeData().getStatus())
                .build();
        dataToSend.put("desk", eventData);
        var event = SseEmitter.event()
                .name("dossier_accepted_event")
                .data(dataToSend)
                .build();
        try {
            emitter.send(event);
        } catch (IOException e) {
            throw new NotImplemented();
        }


        return ResponseEntity.ok(AcceptBadgeResponse.builder()
                .badge(data)
                .build()
        );
    }
}
