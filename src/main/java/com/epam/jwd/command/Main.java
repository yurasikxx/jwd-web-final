package com.epam.jwd.command;

public class Main {
    public static void main(String[] args) {
        final String name = "maksim_hushchyn";
        final String[] splitName = name.split("_");
        final String firstname = splitName[0].substring(0, 1).toUpperCase() + splitName[0].substring(1);
        System.out.println(firstname);
    }
}
