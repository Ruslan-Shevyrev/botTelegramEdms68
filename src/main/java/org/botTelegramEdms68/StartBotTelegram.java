/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botTelegramEdms68;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.botTelegramEdms68.sql.SqlQuery;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 *
 * @author shrv
 */
public class StartBotTelegram extends TelegramLongPollingBot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new StartBotTelegram());
        } catch (TelegramApiException ex) {
            Logger.getLogger(StartBotTelegram.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getBotUsername() {
        return "edms68_bot";
    }

    @Override
    public String getBotToken() {
        return "520807919:AAF1LVcGXceUw46tMD2qPn0RMy2EK-Idr8g";
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        if ("/start".equals(message.getText())) {
            SqlQuery query = new SqlQuery();
            SendMessage sendMessage = new SendMessage();
            sendMessage
                    .setChatId(message.getChatId())
                    .setText("Вас приветствует бот Информационной системы \"Электронное делопроизводство и документооборот\" Тамбовской области");
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException ex) {
                Logger.getLogger(StartBotTelegram.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (!query.isChatId(message.getChatId())) {
                sendMessage
                        .setChatId(message.getChatId())
                        .setText("Пожалуйста подтвердите Ваш номер телефона для регистрации");
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                // Создаем список строк клавиатуры
                List<KeyboardRow> keyboard = new ArrayList<>();
                // Первая строчка клавиатуры
                KeyboardRow keyboardFirstRow = new KeyboardRow();
                // Добавляем кнопки в первую строчку клавиатуры
                KeyboardButton keyboardButton = new KeyboardButton();

                keyboardButton.setText("Подтвердить номер телефона").setRequestContact(true);
                keyboardFirstRow.add(keyboardButton);
                // Добавляем все строчки клавиатуры в список
                keyboard.add(keyboardFirstRow);
                // и устанваливаем этот список нашей клавиатуре
                replyKeyboardMarkup.setKeyboard(keyboard);
               // sendMessage.disableNotification();
                try {
                    sendMessage(sendMessage);                           // Отправим сообщение
                } catch (TelegramApiException ex) {
                    Logger.getLogger(StartBotTelegram.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (update.getMessage().getContact() != null) {
            SqlQuery query = new SqlQuery();
            if (query.isPhone(update.getMessage().getContact().getPhoneNumber(), update.getMessage().getChatId())) {
                SendMessage sendMessage = new SendMessage()
                        .setChatId(message.getChatId())
                        .setText("номер телефона успешно подтвержден");
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
                try {
                    sendMessage(sendMessage);
                } catch (TelegramApiException ex) {
                    Logger.getLogger(StartBotTelegram.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                SendMessage sendMessage = new SendMessage()
                        .setChatId(message.getChatId())
                        .setText("номер телефона отсутствует в базе");
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
                try {
                    sendMessage(sendMessage);
                } catch (TelegramApiException ex) {
                    Logger.getLogger(StartBotTelegram.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
