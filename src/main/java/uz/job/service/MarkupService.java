package uz.job.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import uz.job.enums.Region;

import static uz.job.container.MessageContainer.NEEDED_JOB;
import static uz.job.container.MessageContainer.NEED_EMPLOYEE;


public class MarkupService {
    private final Keyboard mainMenu;
    private final Keyboard regionsKeyboard;
    private final Keyboard isFinish;
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (int i = 0; i < Region.values().length; i++) {
            markup.addRow(new InlineKeyboardButton(Region.values()[i].getName()).callbackData(Region.values()[i].getChatId()));
        }
        regionsKeyboard = markup;
        mainMenu = new ReplyKeyboardMarkup(new String[][]{{NEED_EMPLOYEE}, {NEEDED_JOB}}).resizeKeyboard(true);
        isFinish = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Ha✅").callbackData("_yes_"),
                new InlineKeyboardButton("Yo'q❌").callbackData("_no_")
        );
    }


    public Keyboard mainMenu(){
        return mainMenu;
    }

    public Keyboard regions() {
        return regionsKeyboard;
    }

    public Keyboard isFinish() {
        return isFinish;
    }
}
