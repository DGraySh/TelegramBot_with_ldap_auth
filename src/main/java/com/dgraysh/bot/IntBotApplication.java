package com.dgraysh.bot;

import com.dgraysh.bot.configs.BotProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableConfigurationProperties(BotProperties.class)
public class IntBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.dgraysh.bot.IntBotApplication.class, args);
//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
////            botsApi.registerBot(intBot);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }

}
