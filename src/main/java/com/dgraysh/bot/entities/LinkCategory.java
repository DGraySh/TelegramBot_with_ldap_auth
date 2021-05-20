package com.dgraysh.bot.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "link_categories")
@Data
public class LinkCategory extends AbstractEntity {

    @Column(name = "title")
    private String title;
}
