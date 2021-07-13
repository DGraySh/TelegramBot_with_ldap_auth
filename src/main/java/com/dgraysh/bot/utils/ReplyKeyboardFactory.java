package com.dgraysh.bot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class ReplyKeyboardFactory {

    private ReplyKeyboardFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static KeyboardRow getKeyboardRow(String... titles) {
        var row = new KeyboardRow();
        for (String title : titles) {
            row.add(new KeyboardButton(title));
        }
        return row;
    }


}
