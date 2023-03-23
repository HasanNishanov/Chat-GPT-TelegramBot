package com.company;


import com.company.container.ComponentContainer;
import com.company.controller.Controller;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;

import java.sql.SQLException;


public class AvtoBot extends TelegramLongPollingBot {
    public Controller userController = new Controller();


    public AvtoBot() {
    }

    @Override
    public String getBotUsername() {
        return ComponentContainer.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return ComponentContainer.BOT_TOKEN;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {

            System.out.println("Message");
            Message message = update.getMessage();
            String message1 = update.getMessage().getText();
            User user = message.getFrom();

            try {
                userController.handleMessage(user, message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
        if (update.hasCallbackQuery()) {
            System.out.println("CallbackQuery");
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();
            User user = callbackQuery.getFrom();
            String data = callbackQuery.getData();
            try {
                userController.handleCallBack(user, message, data);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public void sendMsg(Object message) {

        try {
            if (message instanceof SendMessage) {
                execute((SendMessage) message);
            }
            if (message instanceof SendDocument) {
                execute((SendDocument) message);
            }
            if (message instanceof DeleteMessage) {
                execute((DeleteMessage) message);
            }
            if (message instanceof EditMessageText) {
                execute((EditMessageText) message);
            }
            if (message instanceof SendSticker) {
                execute((SendSticker) message);
            }
            if (message instanceof SendPhoto) {
                execute((SendPhoto) message);
            }
            if (message instanceof SendDice) {
                execute((SendDice) message);
            }
            if (message instanceof SendVenue) {
                execute((SendVenue) message);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Xatolik bor");
        }
    }
}


