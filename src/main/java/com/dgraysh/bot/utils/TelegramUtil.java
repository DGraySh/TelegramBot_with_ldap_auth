package com.dgraysh.bot.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramUtil {

    /*
     *
     * Create template for message
     *
     * */

    public static SendMessage createMessageTemplate(User user) {
        return createMessageTemplate(String.valueOf("user.getChatId()")); //todo get chatid from user in DB
    }

    /*
    *
    * Create markdown template for message
    *
    * */

    public static SendMessage createMessageTemplate(String chatId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.enableMarkdown(true);
        return msg;
    }

    /*
    *
    * Create a button
    * */
    public static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(command);
        return button;
    }
}
