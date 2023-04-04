package uz.job.enums;

import lombok.Getter;

@Getter
public enum Region {
    TOSHKENT_SHAHRI("Toshkent shahri", "+R53UK_TOnEczxHVA", "-1001201525803"),
    TOSHKENT("Toshkent viloyati", "+UmL1eOAiKQBBVVpS", "-1001382217080"),
    ANDIJON("Andijon viloyati", "+UAvSDOD06JMov5Vb", "-1001342951948"),
    NAMANGAN("Namangan viloyati", "+ScqGOb53NCW4OgPw", "-1001238009401"),
    FARGONA("Farg'ona viloyati", "+UNc7vGAOPLR8Koz_", "-1001356282812"),
    SIRDARYO("Sirdaryo viloyati", "+VO7yeDA7DqdYyXYl", "-1001424945784"),
    JIZZAX("Jizzax viloyati", "+RiGG84us9PhdlCxF", "-1001176602355"),
    SAMARQAND("Samarqand viloyati", "+ReiebY4DkfqHW-oF", "-1001172872813"),
    QASHQADARYO("Qashqadaryo viloyati", "+ReO7E-x14UJMIxLK", "-1001172552467"),
    SURXONDARYO("Surxondaryo viloyati", "+V2_HLfb0frNpdSzb", "-1001466943277"),
    BUXORO("Buxoro viloyati", "+RhZReTIRkyh8M8pQ", "-1001175867769"),
    NAVOIY("Navoiy viloyati", "+TBgIrK4u_n2f_t9S", "-1001276643500"),
    XORAZM("Xorazm viloyati", "+T2aHHwVQy5Xtdaz1", "-1001332119327"),
    QORAQALPOQ("Qoraqalpog'iston Respublikasi", "+S5mXY80FPr9s_zpw", "-1001268356963");

    private final String name;
    private final String chatId;
    private final String username;

    Region(String name, String username, String chatId) {
        this.name = name;
        this.chatId = chatId;
        this.username = username;
    }

    public static Region getRegion(String chatId){
        for (Region value : Region.values()) {
            if (value.getChatId().equals(chatId)) {
                return value;
            }
        }
        return TOSHKENT_SHAHRI;
    }
}
