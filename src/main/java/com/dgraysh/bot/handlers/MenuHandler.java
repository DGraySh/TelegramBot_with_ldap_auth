package com.dgraysh.bot.handlers;

import com.dgraysh.bot.exceptions.RolesNotFoundException;
import com.dgraysh.bot.services.OidcService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;

import static com.dgraysh.bot.entities.Role.Roles;
import static com.dgraysh.bot.utils.ReplyKeyboardFactory.getKeyboardRow;

@Component
@AllArgsConstructor
public class MenuHandler {

    private final OidcService oidcService;

    public SendMessage handle(Update update, Long chatId) {
        var replyKeyboard = new ArrayList<KeyboardRow>();
        if ((update.hasMessage()) && (update.getMessage().hasText())) {
            String message = update.getMessage().getText();

            if (message.equals(MenuItem.TYPE_PROCESS.getText())) {
                replyKeyboard = new ArrayList<>(Arrays.asList(
                        getKeyboardRow(MenuItem.CONF_ROOM.getText()),
                        getKeyboardRow(MenuItem.ABSENT.getText()),
                        getKeyboardRow(MenuItem.PENALTY.getText()),
                        getKeyboardRow(MenuItem.TRIP.getText())
                ));
                if (Boolean.TRUE.equals(oidcService.findUserInfo(chatId)
                        .map(userInfo -> userInfo.hasRole(Roles.ADMIN.getTitle()))
                        .orElseThrow(RolesNotFoundException::new))) {
                    replyKeyboard.add(getKeyboardRow(
                            MenuItem.NEW_EMPLOYEE.getText(),
                            MenuItem.FIRE_EMPLOYEE.getText()
                    ));
                }
            } else if (message.equals(MenuItem.HELP_INFO.getText())) {
                replyKeyboard = new ArrayList<>(Arrays.asList(
                        getKeyboardRow(MenuItem.INSTRUCTIONS.getText()),
                        getKeyboardRow(MenuItem.CONTACTS.getText()),
                        getKeyboardRow(MenuItem.WHERE_ARE_THEY.getText()),
                        getKeyboardRow(MenuItem.USEFUL_FOR_OP.getText()),
                        getKeyboardRow(MenuItem.INT_DOCS.getText())
                ));
            } else if (message.equals(MenuItem.COMPANY_LIFE.getText())) {
                replyKeyboard = new ArrayList<>(Arrays.asList(
                        getKeyboardRow(MenuItem.BIRTHDAY.getText()),
                        getKeyboardRow(MenuItem.FEEDBACK.getText())
                ));
            } else if (message.equals(MenuItem.SUPPORT.getText())) {
                replyKeyboard = new ArrayList<>(Arrays.asList(
                        getKeyboardRow(MenuItem.GET_SUPPORT.getText()),
                        getKeyboardRow(MenuItem.REQUIREMENT.getText())
                ));
                if (Boolean.TRUE.equals(oidcService.findUserInfo(chatId)
                        .map(userInfo -> userInfo.hasRole(Roles.ADMIN.getTitle()))
                        .orElseThrow(RolesNotFoundException::new))) {
                    replyKeyboard.add(getKeyboardRow(
                            MenuItem.GO_FOR_A_WALK.getText()
                    ));
                }
            } else {
                replyKeyboard = new ArrayList<>(Arrays.asList(
                        getKeyboardRow(MenuItem.TYPE_PROCESS.getText()),
                        getKeyboardRow(MenuItem.HELP_INFO.getText()),
                        getKeyboardRow(MenuItem.COMPANY_LIFE.getText()),
                        getKeyboardRow(MenuItem.SUPPORT.getText())
                ));
            }

        }
        var mainKeyboard = ReplyKeyboardMarkup.builder()
                .keyboard(replyKeyboard)
                .selective(true)
                .oneTimeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Выберите пункт меню")
                .replyMarkup(mainKeyboard)
                .build();
    }

    private enum MenuItem {
        TYPE_PROCESS("Типовой процесс"),
        HELP_INFO("Справочная информация"),
        COMPANY_LIFE("Жизнь компании"),
        SUPPORT("Поддержка"),
        CONF_ROOM("Бронь переговорной"),
        ABSENT("Отсутствие"),
        PENALTY("Штраф за опоздание"),
        TRIP("Оформление командировки"),
        INSTRUCTIONS("Инструкции"),
        CONTACTS("Контакты сотрудников"),
        WHERE_ARE_THEY("Кто-где?"),
        USEFUL_FOR_OP("Полезно для ОП"),
        INT_DOCS("Основные документы INT"),
        BIRTHDAY("Дни рождения"),
        FEEDBACK("Обратная связь"),
        GET_SUPPORT("Сообщить о проблеме"),
        REQUIREMENT("Потребность"),
        NEW_EMPLOYEE("Новый сотрудник"),
        FIRE_EMPLOYEE("Увольнение сотрудника"),
        GO_FOR_A_WALK("Пошли гулять");

        private final String text;

        MenuItem(final String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
