package com.jaime.account.Services;

import com.jaime.account.Models.LogEntity;
import com.jaime.account.Repository.LogRepository;
import com.jaime.account.util.EventAction;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logAction(EventAction action, String subject, String object, String path) {
        LogEntity log = LogEntity.builder()
                .date(new Date(System.currentTimeMillis()))
                .action(action)
                .subject(subject)
                .object(object)
                .path(path).build();

        logRepository.save(log);
    }

    @Override
    public List<LogEntity> findAllLogs() {
        return logRepository.findAll();
    }
}
