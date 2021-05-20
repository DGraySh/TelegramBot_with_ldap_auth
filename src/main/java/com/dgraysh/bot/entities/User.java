package com.dgraysh.bot.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User extends AbstractEntity {

    @Column(name = "name")
    String name;

    @Column(name = "chat_id")
    Long chatId;

}