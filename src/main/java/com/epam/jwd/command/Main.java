package com.epam.jwd.command;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("application", new Locale("ru"));
        System.out.println(Locale.getDefault());
        final Locale ru = new Locale("ru");
        System.out.println(ru.getLanguage());
        final Locale fr = new Locale("fr");
        System.out.println(fr.getLanguage());
        final Locale en = new Locale("en");
        System.out.println(en.getLanguage());
        System.out.println(new String(bundle.getString("main").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
    }
}