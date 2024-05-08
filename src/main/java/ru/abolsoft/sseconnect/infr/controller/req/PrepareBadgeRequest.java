package ru.abolsoft.sseconnect.infr.controller.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PrepareBadgeRequest {
    @Schema(description = "Id участника")
    private Long memberId;
    @Schema(description = "Id стойки")
    private Long deskId;
}

