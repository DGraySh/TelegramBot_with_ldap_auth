package com.dgraysh.bot.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "links")
@Data
public class Link extends AbstractEntity{

    @Column(name = "link")
    String link;

    @Column(name = "name")
    String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private LinkCategory linkCategory;

}
