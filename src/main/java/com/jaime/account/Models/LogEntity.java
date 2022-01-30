package com.jaime.account.Models;

import com.jaime.account.util.EventAction;
import lombok.*;
import javax.persistence.*;
import java.sql.Date;

@Entity(name = "log")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Date date;

    @Enumerated(EnumType.STRING)
    private EventAction action;

    @Column
    private String subject;

    @Column
    private String object;

    @Column
    private String path;
}
