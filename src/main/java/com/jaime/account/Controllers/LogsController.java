package com.jaime.account.Controllers;

import com.jaime.account.Services.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogsController {
    private final LogService logService;

    public LogsController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/api/security/events")
    public ResponseEntity<?> getLogsEvents() {
        return ResponseEntity.ok(logService.findAllLogs());
    }
}
