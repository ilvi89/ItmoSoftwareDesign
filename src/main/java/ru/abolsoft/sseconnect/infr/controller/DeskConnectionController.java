package ru.abolsoft.sseconnect.infr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.abolsoft.sseconnect.core.entity.Desk;
import ru.abolsoft.sseconnect.core.exception.NotImplemented;
import ru.abolsoft.sseconnect.core.usecase.DeskCreateUseCase;
import ru.abolsoft.sseconnect.core.usecase.DeskMobileConnectUseCase;
import ru.abolsoft.sseconnect.infr.controller.req.CreateDeskRequest;
import ru.abolsoft.sseconnect.infr.controller.res.DeskResponse;
import ru.abolsoft.sseconnect.infr.repository.DeskRepositoryImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("desk/connect")
public class DeskConnectionController {
    private final DeskRepositoryImpl<SseEmitter> deskRepository;
    private final DeskCreateUseCase deskCreateUseCase;
    private final DeskMobileConnectUseCase deskMobileConnectUseCase;
    private final ObjectMapper objectMapper;


    @PostMapping
    public ResponseEntity<?> createDesk(@RequestBody CreateDeskRequest req) {
        DeskCreateUseCase.Res res = deskCreateUseCase
                .execute(DeskCreateUseCase.Req.builder().name(req.getName()).build());
        return ResponseEntity.ok(res);
    }


    @GetMapping(value = "/{deskId}")
    public SseEmitter connectToDesk(@PathVariable Long deskId) {
        SseEmitter emitter = new SseEmitter(-1L); //TODO: set max connection count and timeout
        Optional<Desk> desk = deskRepository.findById(deskId);
        if (desk.isEmpty())
            throw new NotImplemented();

        deskRepository.put(desk.get().getId(), emitter);
        emitter.onCompletion(() -> {
            deskRepository.removeObject(deskId);
        });
        emitter.onTimeout(() -> {
            deskRepository.removeObject(deskId);
        });
        emitter.onError(throwable -> {
            deskRepository.removeObject(deskId);
        });

        var d = desk.get();
        var eventData = DeskResponse.builder()
                .id(d.getId())
                .name(d.getName())
                .mobileConnection(d.getMobile())
                .build();
        var dataToSend = new HashMap<String, Object>();
        dataToSend.put("desk", eventData);
        var event = SseEmitter.event()
                .name("mobile_connection_event")
                .data(dataToSend)
                .build();
        try {
            emitter.send(event);
        } catch (IOException e) {
            throw new NotImplemented();
        }

        return emitter;
    }


    @PostMapping("/{deskId}")
    public void sendMobileStatus(@PathVariable Long deskId, @RequestParam Boolean active) {
        var req = DeskMobileConnectUseCase.Req.builder()
                .deskId(deskId)
                .mobileIsActive(active)
                .build();
        var res = deskMobileConnectUseCase.execute(req);

        // TODO: refactor me !!!
        Optional<Map.Entry<Desk, Optional<SseEmitter>>> mapping = deskRepository.findMapping(res.getDeskId());
        if (mapping.isPresent()) {
            Optional<SseEmitter> emitter = mapping.get().getValue();
            if (emitter.isEmpty())
                return;
            try {
                var em = emitter.get();
                var dataToSend = new HashMap<String, Object>();
                var desk = mapping.get().getKey();
                var eventData = DeskResponse.builder()
                        .id(desk.getId())
                        .name(desk.getName())
                        .mobileConnection(desk.getMobile())
                        .build();
                dataToSend.put("desk", eventData);
                var event = SseEmitter.event()
                        .name("mobile_connection_event")
                        .data(dataToSend)
                        .build();
                em.send(event);
//                TODO: with this client should reconnect
//                em.complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

