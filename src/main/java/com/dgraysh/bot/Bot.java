package com.dgraysh.bot;

import com.dgraysh.bot.configs.BotProperties;
import com.dgraysh.bot.entities.UserInfo;
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


    public Bot(BotProperties properties, OidcService oidcService) {
        super(properties.createOptions());
        this.botUsername = properties.getName();
        this.botToken = properties.getToken();
        this.oidcService = oidcService;
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

        var userId = update.getMessage().getFrom().getId().toString();
        var chatId = update.getMessage().getChatId().toString();
        oidcService.findUserInfo(userId).ifPresentOrElse(
                userInfo -> greet(userInfo, chatId),
                () -> askForLogin(userId, chatId));
    }

    private void greet(UserInfo userInfo, String chatId) {
        var username = userInfo.getPreferredUsername();
        var message = String.format("Hello, <b>%s</b>!%nYou are the best! Have a nice day!%n%s", username, userInfo.hasRole("ROLE_bot_admin"));
        sendHtmlMessage(message, chatId);
//        message = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
//        sendHtmlMessage(message, chatId);

    }

    private void askForLogin(String userId, String chatId) {
        var url = oidcService.getAuthUrl(userId);
//        var message = String.format("Please, <a href=\"%s\">log in</a>.", url);
        var message = (url);
//        var message = String.format("Please, <a href=\"http://localhost:8080/auth/realms/ldap_int/protocol/openid-connect/auth?response_type=code&client_id=tg_bot&redirect_uri=http://localhost:8888/bot/Fauth&scope=openid/offline_access&state=bf9f42e0-141e-4554-9744-ee1977d2c69c\">log in</a>.");
        sendHtmlMessage(message, chatId);
    }

    void sendHtmlMessage(String message, String chatId) {
        var sendMessage = new SendMessage(chatId, message);
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


/*
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            log.info("update msg: {}", update.getMessage().getText());
            SendMessage msg = createMessageTemplate(update.getMessage().getChatId().toString());
            msg.enableMarkdown(true);
            msg.setText(update.getMessage().getFrom().getId() + "\n" +
                    update.getMessage().getChatId());
            execute(msg);

            if (update.hasMessage() && !update.getMessage().hasText()) {
                msg.setText("has message but no text");
                execute(msg);
            }
            if (update.getMessage().hasText() && update.getMessage().getText().equals("/auth")) {
                msg.setText("username");
                msg.setReplyMarkup(new ForceReplyKeyboard(true));
                execute(msg);
//                msg.setText(update.getMessage().get


//                log.info(info(authentication));
                Authentication authentication;
                log.info(SecurityContextHolder.getContext().toString());
                log.info(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());


            } else if (update.getMessage().getReplyToMessage() != null) {
                if (update.getMessage().getReplyToMessage().getText().equals("username")) {
                    log.info(update.getMessage().getReplyToMessage().getText());
                    log.info(update.getMessage().getText());
                    msg.setText("password");
                    msg.setReplyMarkup(new ForceReplyKeyboard(true));
                    execute(msg);
                }
                if (update.getMessage().getReplyToMessage().getText().equals("password")) {
                    log.info(update.getMessage().getReplyToMessage().getText());
                    log.info(update.getMessage().getText());
//                    setAuthentication(authenticationService.authenticateByLdap(username, password));
//                    msg.setText("auth: " + authentication.isAuthenticated());
                    execute(msg);
//                    log.info(info(authentication));

*/
/*
                    if (authentication.isAuthenticated()) {
                        msg.setText(String.format("username: %s, password: %s", getUsername(), getPassword()));
                        execute(msg);
                        msg.setText(info(authentication));
                        execute(msg);
                        log.info("username: {}", getUsername());
                        log.info("password: {}", getPassword());
                        log.info(info(authentication));
                    } else {
                        msg.setText("not auth, try again!");
                        execute(msg);
                        msg.setText(info(authentication));
                        execute(msg);
                    }
*//*

                }
            } else {
                log.info("unknown message");
                log.info("reply " + update.getMessage().getReplyToMessage());
                msg.setText("unknown");
//                execute(msg);

      */
/*          InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                        createInlineKeyboardButton("*auth*", "/auth"),
                        createInlineKeyboardButton("_start_", "/start"),
                        createInlineKeyboardButton("`change`", "/change"));
                inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));
                msg.setText("blabla");
                msg.setReplyMarkup(inlineKeyboardMarkup);
                execute(msg);*//*


                KeyboardRow row = new KeyboardRow();
                KeyboardButton button = new KeyboardButton("hello");
                row.add(button);

                KeyboardRow row2 = new KeyboardRow();
                row2.add(new KeyboardButton("goodbye"));
                row2.add(new KeyboardButton("/auth"));

                List<KeyboardRow> keyboardMarkup =
                        Arrays.asList(row, row2);

                ReplyKeyboard key = new ReplyKeyboardMarkup(keyboardMarkup);

//                ReplyKeyboard keyboard = ReplyKeyboardMarkup.builder()
//                        .keyboard(keyboardMarkup)
//                        .selective(true)
//                        .keyboardRow(new KeyboardRow())
//                        .build();

//                msg.setText("text");
                msg.setReplyMarkup(key);
                execute(msg);
            }
//                msg.setText(update.getUpdateId().toString());
//                execute(msg);
//                log.info(execute(msg).getReplyToMessage().getText());

//
//            }
//            if ((update.getCallbackQuery().getData() != null)&&(update.getCallbackQuery().getData().equals("/callback"))){
//                SendMessage cbMsg = createMessageTemplate(update.getMessage().getChatId().toString());
//                cbMsg.setText("callback received");
//                execute(cbMsg);
//            }
//            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
//            message.setChatId(update.getMessage().getChatId().toString());
//            String messaga = (
//                    update.getMessage().getChatId().toString() + "\n" +
//                            update.getMessage().getFrom().getId().toString() + "\n" +
//                            update.getMessage().getFrom().getLastName() + "\n" +
//                            update.getMessage().getFrom().getFirstName()
//            );
//            message.setText(messaga);
//
//            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//
//            List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
//                    createInlineKeyboardButton("Change name", "/change"),
//                    createInlineKeyboardButton("2", "/change"),
//                    createInlineKeyboardButton("3", "/change"),
//                    createInlineKeyboardButton("Change name5", "/change"));
//
//            inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne, inlineKeyboardButtonsRowTwo));
//
//            SendMessage msg = createMessageTemplate(update.getMessage().getChatId().toString());
//            msg.setText("blabla");
//            msg.setReplyMarkup(inlineKeyboardMarkup);
//
//            if (!update.hasCallbackQuery()) {
//                log.info("callback is null");
//            } else {
//                log.info("cb " + update.getCallbackQuery().getData());


//            execute((update.getCallbackQuery().getMessage().getChatId()), update.getCallbackQuery().getData()));

//                InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
//                keyboardButton.setText("callback");
//                keyboardButton.setCallbackData("/callback");
//                List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(keyboardButton);
//                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//                inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));
//
//                KeyboardRow row = new KeyboardRow();
//                KeyboardButton button = new KeyboardButton("hello");
//                row.add(button);
//
//
//                KeyboardRow row2 = new KeyboardRow();
//                row2.add(new KeyboardButton("goodbye"));
//
//                List<KeyboardRow> keyboardMarkup =
//                        Arrays.asList(row, row2);
//
//                ReplyKeyboard keyboard = ReplyKeyboardMarkup.builder()
//                        .keyboard(keyboardMarkup)
//                        .selective(true)
//                        .keyboardRow(new KeyboardRow())
//                        .keyboardRow(new KeyboardRow())
//                        .keyboardRow(new KeyboardRow())
//                        .keyboardRow(new KeyboardRow())
//                        .build();
//
//
//                SendMessage keyboardMsg = new SendMessage();
//                keyboardMsg.setText("text");
//                keyboardMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
//                keyboardMsg.setReplyMarkup(keyboard);
//                keyboardMsg.setReplyMarkup(inlineKeyboardMarkup);
//
//
//                try {
//                    execute(keyboardMsg);
////                execute(msg);
////                execute(message); // Call method to send the message
//                } catch (TelegramApiException e) {
//                }
//                    e.printStackTrace();
        }
    }
*/

}
