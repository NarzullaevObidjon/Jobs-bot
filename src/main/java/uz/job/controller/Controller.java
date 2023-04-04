package uz.job.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import uz.job.enums.Region;
import uz.job.enums.VacancyCreationState;
import uz.job.dto.VacancyDTO;
import uz.job.service.MarkupService;

import java.util.concurrent.ConcurrentHashMap;

import static uz.job.container.ComponentContainer.ADMIN_ID;
import static uz.job.container.MessageContainer.NEED_EMPLOYEE;
import static uz.job.container.MessageContainer.WELCOME;

public class Controller {
    public static ConcurrentHashMap<Long, VacancyDTO> map = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, VacancyCreationState> userState = new ConcurrentHashMap<>();
    private final TelegramBot bot = new TelegramBot("6063624310:AAEQimNvy15OlePlVTeRXR6uvtsX_KzsdQo");
    private final MarkupService markupService = new MarkupService();

    public void handleUpdate(Update update) {
        if (update.callbackQuery() != null) {
            handleCallback(update);
        } else if (update.message() != null) {
            handleMessage(update);
        }
    }

    private void handleMessage(Update update) {
        String text = update.message().text();
        Long chatId = update.message().chat().id();

        if (chatId.equals(ADMIN_ID)) {
            AdminController.handleUpdate(update);
            return;
        }

        if (text.equals("/start")) {
            SendMessage sendMessage = new SendMessage(chatId, WELCOME.formatted(update.message().chat().firstName()));
            sendMessage.replyMarkup(markupService.mainMenu());
            bot.execute(sendMessage);
        } else if (text.equals(NEED_EMPLOYEE) && !userState.containsKey(chatId)) {
            map.put(chatId, new VacancyDTO());
            userState.put(chatId, VacancyCreationState.REGION);
            bot.execute(new SendMessage(chatId, "Vakansiya joylash menyusiga hush kelibsiz!").replyMarkup(new ReplyKeyboardRemove()));
            SendMessage sendMessage = new SendMessage(chatId, "Viloyatingizni tanlang \uD83D\uDC47");
            sendMessage.replyMarkup(markupService.regions());
            bot.execute(sendMessage);
        }else if (userState.containsKey(chatId)){
            VacancyCreationState state = userState.get(chatId);
            if (state.equals(VacancyCreationState.DISTRICT)) {
                map.get(chatId).setDistrict(text);
                userState.replace(chatId,VacancyCreationState.COMPANY);
                bot.execute(new SendMessage(chatId,"Kompaniya yoki ishxona nomini kiriting"));
            }else if (state.equals(VacancyCreationState.COMPANY)){
                map.get(chatId).setCompany(text);
                userState.replace(chatId,VacancyCreationState.VACANCYNAME);
                bot.execute(new SendMessage(chatId,"Ish nomini kiriting\nMasalan : <u>Matematika fani o'qituvchisi</u>").parseMode(ParseMode.HTML));
            }else if (state.equals(VacancyCreationState.VACANCYNAME)){
                map.get(chatId).setJobTitle(text);
                userState.replace(chatId,VacancyCreationState.FULLNAME);
                bot.execute(new SendMessage(chatId,"Ma'sul ism familiyasini kiriting ..."));
            }else if (state.equals(VacancyCreationState.FULLNAME)){
                map.get(chatId).setFullName(text);
                userState.replace(chatId,VacancyCreationState.SALARY);
                bot.execute(new SendMessage(chatId,"Taklif qilinadigan ish haqqini kiriting\nMasalan : <u>300$</u>, <u>3.000.000 so'm</u>, <u>kelishiladi</u>").parseMode(ParseMode.HTML));
            }else if (state.equals(VacancyCreationState.SALARY)){
                map.get(chatId).setSalary(text);
                userState.replace(chatId,VacancyCreationState.PHONE);
                bot.execute(new SendMessage(chatId,"Murojaat qilish uchun telefon raqam kiriting"));
            }else if (state.equals(VacancyCreationState.PHONE)){
                map.get(chatId).setPhone(text);
                userState.replace(chatId,VacancyCreationState.USERNAME);
                bot.execute(new SendMessage(chatId,"Murojaat qilish uchun telegram 'username' kiriting\nMasalan : <u>@abc123</u>").parseMode(ParseMode.HTML));
            }else if (state.equals(VacancyCreationState.USERNAME)){
                map.get(chatId).setUsername(text);
                userState.replace(chatId,VacancyCreationState.ACCEPTTIME);
                bot.execute(new SendMessage(chatId,"Murojaat qilish vaqtini kiriting\nMasalan : <u>09:00 - 20:00</u>").parseMode(ParseMode.HTML));
            }else if (state.equals(VacancyCreationState.ACCEPTTIME)){
                map.get(chatId).setAcceptTime(text);
                userState.replace(chatId,VacancyCreationState.WORKTIME);
                bot.execute(new SendMessage(chatId,"Ish vaqtini kiriting\nMasalan : <u>Dushanba - Juma, 08:00 - 16:00</u>").parseMode(ParseMode.HTML));
            }else if (state.equals(VacancyCreationState.WORKTIME)){
                map.get(chatId).setWorkTime(text);
                userState.replace(chatId,VacancyCreationState.ADDITIONAL);
                bot.execute(new SendMessage(chatId,"Qo'shimcha ma'lumotlar ..."));
            }else if (state.equals(VacancyCreationState.ADDITIONAL)){
                map.get(chatId).setAdditional(text);
                userState.replace(chatId,VacancyCreationState.FINISH);
                String message = getPrettyMessage(map.get(chatId));
                SendMessage sendMessage = new SendMessage(chatId, message).parseMode(ParseMode.HTML).disableWebPagePreview(true);
                bot.execute(sendMessage);
                bot.execute(new SendMessage(chatId,"Ajoyib! Yakunlashga bir qadam qoldi \uD83D\uDFE2 \n\nBarcha ma'lumotlar to'g'rimi?").replyMarkup(markupService.isFinish()));
            }
        }


    }

    private void handleCallback(Update update) {
        Long chatId = update.callbackQuery().from().id();
        String data = update.callbackQuery().data();
        if (chatId.equals(ADMIN_ID)) {
            AdminController.handleUpdate(update);
            return;
        }

        if (userState.containsKey(chatId)){
            VacancyCreationState state = userState.get(chatId);
            if(state.equals(VacancyCreationState.REGION)){
                bot.execute(new DeleteMessage(chatId, update.callbackQuery().message().messageId()));
                map.get(chatId).setRegion(Region.getRegion(data));

                userState.replace(chatId,VacancyCreationState.DISTRICT);
                SendMessage sendMessage = new SendMessage(chatId, "Tuman(shahar) nomini kiriting\nMasalan : <u>Baxmal tumani</u>").parseMode(ParseMode.HTML);
                bot.execute(sendMessage);
            }
        }
    }

    private String getPrettyMessage(VacancyDTO vacancyDTO) {
        String s = """
                <b>Xodim kerak</b> : <b>%s</b>
                                
                📍<b>Manzil</b> : %s, %s
                🏢<b>Korxona</b> : %s
                💰<b>Maosh</b> :  %s
                ⏱ <b>Ish vaqti</b> : %s
                ✍️<b>Ma'sul</b> : %s
                ‼️<b>Qo'shimcha ma'lumotlar</b> : %s
                                
                ☎️<b>Telefon raqam</b> : %s
                📨<b>Telegram</b> :  %s
                ‼️<b>Murojaat qilish vaqti</b> :  %s           
                                
                 <a href='https://t.me/%s'>%s bo'sh ish o'rinlari kanali</a>
                """.formatted(
                vacancyDTO.getJobTitle(),
                vacancyDTO.getRegion().getName(), vacancyDTO.getDistrict(),
                vacancyDTO.getCompany(),
                vacancyDTO.getSalary(),
                vacancyDTO.getWorkTime(),
                vacancyDTO.getFullName(),
                vacancyDTO.getAdditional(),
                vacancyDTO.getPhone(),
                vacancyDTO.getUsername(),
                vacancyDTO.getAcceptTime(),
                vacancyDTO.getRegion().getUsername(), vacancyDTO.getRegion().getName()
        );
        return s;
    }

    public static Controller getInstance() {
        return new Controller();
    }
}
