package com.dgraysh.bot.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "help_requests")
@Data
public class HelpRequest extends AbstractEntity {

    @Column(name = "creation_time")
    Date creationTime;

    @Column(name = "closing_time")
    Date closingTime;

    @Column(name = "request")
    String request;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;



}
