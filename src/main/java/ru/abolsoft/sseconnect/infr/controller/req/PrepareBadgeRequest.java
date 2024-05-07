package ru.abolsoft.sseconnect.infr.controller.req;


import lombok.Data;

@Data
public class PrepareBadgeRequest {
    private Long memberId;
    private Long deskId;
}

