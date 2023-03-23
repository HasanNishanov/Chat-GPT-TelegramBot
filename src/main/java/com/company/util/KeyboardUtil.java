package com.company.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;

public class KeyboardUtil {


    private static KeyboardButton getButton(String demo){
        return new KeyboardButton(demo);
    }

    private static KeyboardRow getRow(KeyboardButton ... buttons){
        return new KeyboardRow(Arrays.asList(buttons));
    }

    private static List<KeyboardRow> getRowList(KeyboardRow ... rows){
        return Arrays.asList(rows);
    }

    private static ReplyKeyboardMarkup getMarkup(List<KeyboardRow> keyboard){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }
}
