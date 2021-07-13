package com.dgraysh.bot.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    String title;

    public enum Roles {
        USER("ROLE_bot_user"),
        ADMIN("ROLE_bot_admin");

        private final String title;

        Roles(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
