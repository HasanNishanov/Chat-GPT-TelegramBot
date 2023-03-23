package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.database.Database;
import com.company.model.Request;
import com.company.service.RequestService;
import com.company.util.InlineButtonUtil;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Controller {
    public static final String QUERYFORREQUEST = "";
    public static final String QUERYFORDELETE = " ";
    public static String answer = new String();
    public static String globalchat_id;

    public Controller()   {

    }
    public static void chatGPT(String text, String chatId) throws Exception {

        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        SendMessage sendMessage = new SendMessage();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer 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");

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("max_tokens", 4000);
        data.put("temperature", 1.0);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        sendMessage.setText(new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
        sendMessage.setChatId(chatId);
        ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        answer= new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }

    public void handleMessage(User user, Message message) throws Exception {
        if (message.hasText()) {
            handleText(user, message);
        }
    }
    private void handleText(User user, Message message) throws Exception {

        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        EditMessageText editMessage = new EditMessageText ();
        Long contact = message.getChatId();

        if(text.equals("/start")){
            sendMessage.setText("\uD83D\uDC4B * Привет красатульчики и красатулички! Этот бот написанный @hasannishanov основанный" +
                    " на технологии от OpenAI ,а в частности того самого CHAT-GPT. Думаю функционал бота объяснять не нужно." +
                    "Начитните фантазировать вопросы и задавайте их с префиксом /gpt. Пример: '/gpt В чём смысл жизни?' *");

            sendMessage.setParseMode("Markdown");

            sendMessage.setChatId(chatId);
            InlineKeyboardButton rlist = InlineButtonUtil.button("Список моих запрсоов", "СписокЗапросов");
            List<InlineKeyboardButton> row = InlineButtonUtil.row(rlist);
            List<List<InlineKeyboardButton>> rowcollection = InlineButtonUtil.collection(row);
            sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(rowcollection));
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }
        if(text.startsWith("/gpt")){
            chatGPT(text,chatId);

            Database.getConnection().createStatement().execute
                    (QUERYFORREQUEST + "(" + "'" +  text + "'" + "," + "'" + answer + "'" + "," + "'" + message.getFrom().getId()  + "' )");
        }
        if(text.startsWith("/delete")){
            System.out.println(QUERYFORDELETE + message.getText().substring(8) +  " and tg_id = '"+ message.getFrom().getId() + "'");
            Database.getConnection().createStatement().execute(QUERYFORDELETE + message.getText().substring(8) +  " and tg_id = '"+ message.getFrom().getId() + "'");
            sendMessage.setText("Успешно удалено!");
        }

    }
    public void handleCallBack(User user, Message message, String data) throws SQLException {
        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        Long contact = message.getChatId();
        ResultSet resultSet = Database.getConnection().createStatement().executeQuery
                ("select * from requests where tg_id =" +  "'" + contact.toString() + "'");
        globalchat_id = contact.toString();
        if (data.equals("СписокЗапросов")) {
            RequestService.loadRequestList();
          for(Request request : Database.requestList) {
              sendMessage.setText("Номер запроса: " + request.getId() + "\n\n *Ваш вопрос: " + request.getQuestion().toString().substring(5) + "*" + "\n \n*Ответ: *" + "" + request.getAnswer());
              InlineKeyboardButton so = InlineButtonUtil.button("Очистить историю", "Очистить", "\uD83D\uDEAE");
              List<InlineKeyboardButton> row = InlineButtonUtil.row(so);
              List<List<InlineKeyboardButton>> rowcollection = InlineButtonUtil.collection(row);
              sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(rowcollection));
              sendMessage.setParseMode("Markdown");
              sendMessage.setChatId(chatId);
              ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
          }
            if (!resultSet.next()){
                sendMessage.setText("У вас пустой список запросов. Сделайте запрос и тут появяться данные о нём.");
                sendMessage.setChatId(chatId);
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        }
        if(data.equals("Очистить")){
            RequestService.loadRequestList();
            SendMessage sendMessage1 = new SendMessage();
            for(Request request : Database.requestList) {
                sendMessage.setText("Номер запроса: *" + request.getId() + "*\n\n *Ваш вопрос: " + request.getQuestion().toString().substring(5) + "*");
                sendMessage.setParseMode("Markdown");
                sendMessage.setChatId(chatId);
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
            sendMessage1.setText("Выберите номер запроса который вы хотите удалить и введите команду /delete id-запроса \n Пример: /delete 3");
            sendMessage1.setChatId(chatId);
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage1);
            if (!resultSet.next()){
                sendMessage.setText("У вас пустой список запросов. Сделайте запрос и тут появяться данные о нём.");
                sendMessage.setChatId(chatId);
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        }
    }
}

