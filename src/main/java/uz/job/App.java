package uz.job;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import uz.job.controller.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(20);
    private static final ThreadLocal<Controller> controllers = ThreadLocal.withInitial(Controller::new);
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("6063624310:AAEQimNvy15OlePlVTeRXR6uvtsX_KzsdQo");
        bot.setUpdatesListener(updates->{
            for (Update update : updates) {
                executorService.execute(()->controllers.get().handleUpdate(update));
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
