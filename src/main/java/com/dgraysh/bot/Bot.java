package com.dgraysh.bot;

import com.dgraysh.bot.configs.BotProperties;
import com.dgraysh.bot.handlers.MenuHandler;
import com.dgraysh.bot.services.OidcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final OidcService oidcService;
    private final MenuHandler menuHandler;


    public Bot(BotProperties properties, OidcService oidcService, MenuHandler menuHandler) {
        super(properties.createOptions());
        this.botUsername = properties.getName();
        this.botToken = properties.getToken();
        this.oidcService = oidcService;
        this.menuHandler = menuHandler;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void onUpdateReceived(Update update) {
        log.debug("Got a new update: {}", update);
        if (!update.hasMessage()) {
            log.debug("Update has no message. Skip processing.");
            return;
        }

        var userId = update.getMessage().getFrom().getId();
        var chatId = update.getMessage().getChatId();
        oidcService.findUserInfo(userId).ifPresentOrElse(
                userInfo -> greet(chatId, update),
                () -> askForLogin(userId, chatId));
    }

    private void greet(Long chatId, Update update) {
        tryExecute(menuHandler.handle(update, chatId));
    }

    private void askForLogin(Long userId, Long chatId) {
        var url = oidcService.getAuthUrl(userId);
        var message = String.format("Пожалуйста, <a href=\"%s\">авторизуйтесь</a>, используя свой логин и пароль.", url);
        sendHtmlMessage(message, chatId);
    }

    void sendHtmlMessage(String message, Long chatId) {
        var sendMessage = new SendMessage(chatId.toString(), message);
        sendMessage.setParseMode(HTML);
        tryExecute(sendMessage);
    }

    private void tryExecute(BotApiMethod<?> action) {
        try {
            execute(action);
        } catch (TelegramApiException e) {
            String message = String.format(
                    "Ooops! Something went wrong during action execution. Action: %s. Error: %s.",
                    action,
                    e);
            log.error(message, e);
        }
    }

    public void greet(Long chatId) {
        SendMessage msg = SendMessage.builder()
                .text("Добро пожаловать в INT!\nВыберите пункт меню")
                .chatId(chatId.toString())
                .build();
        tryExecute(msg);
    }
}
