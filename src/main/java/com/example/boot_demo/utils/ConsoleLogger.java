package com.example.boot_demo.utils;

public class ConsoleLogger {
    // ANSI 转义序列，用于设置文本颜色
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void logError(String message) {
        System.out.println(ANSI_RED + "[TEST] " + message + ANSI_RESET);
    }
}
