package ru.abolsoft.sseconnect.infr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.abolsoft.sseconnect.core.usecase.DeskCreateUseCase;
import ru.abolsoft.sseconnect.infr.controller.req.CreateDeskRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("desk")
public class DeskController {
    private final DeskCreateUseCase deskCreateUseCase;


    @PostMapping
    public ResponseEntity<?> createDesk(@RequestBody CreateDeskRequest req) {
        DeskCreateUseCase.Res res = deskCreateUseCase
                .execute(DeskCreateUseCase.Req.builder().name(req.getName()).build());
        return ResponseEntity.ok(res);
    }
}
