package com.jaime.account.Services;

import com.jaime.account.Models.LogEntity;
import com.jaime.account.util.EventAction;

import java.util.List;

public interface LogService {
    void logAction(EventAction action, String subject, String object, String path);
    List<LogEntity> findAllLogs();
}
