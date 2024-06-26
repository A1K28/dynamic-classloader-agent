package com.github.a1k28.dclagent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Logger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String WHITE_BRIGHT = "\033[0;97m";
    private static final String BLUE_BRIGHT = "\033[0;94m";
    private static final String RED_BRIGHT = "\033[0;91m";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    static {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+4"));
//        try {
//            PrintStream ps = new PrintStream(
//                    new BufferedOutputStream(new FileOutputStream("server.log")), true);
//            System.setOut(ps);
//            System.setErr(ps);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    public enum Level {
        INFO, DEBUG, TRACE;
    }

    private static final ConcurrentMap<Class<?>, Logger> cache = new ConcurrentHashMap<>();
    private static Level level = Level.TRACE;
    private final Class<?> clazz;

    public static Logger getInstance(Class<?> clazz) {
        return cache.computeIfAbsent(clazz, Logger::new);
    }

    private Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static void setLoggingLevel(Level level) {
        Logger.level = level;
    }

    public void info(String message) {
        System.out.println(getDate() + info() + this.getClassName() + message);
    }

    public void warn(String message) {
        System.out.println(getDate() + warn() + this.getClassName() + message);
    }

    public void debug(String message) {
        if (level.ordinal() < Level.DEBUG.ordinal()) return;
        System.out.println(getDate() + debug() + this.getClassName() + message);
    }

    public void trace(String message) {
        if (level.ordinal() < Level.TRACE.ordinal()) return;
        System.out.println(getDate() + trace() + this.getClassName() + message);
    }

    public void agent(String message) {
        System.out.println(getDate() + agent() + this.getClassName() + message);
    }

    public void error(String message) {
        System.out.println(getDate() + error() + this.getClassName() + message);
    }

    public void error(String message, Throwable e) {
        System.out.println(getDate() + error() + this.getClassName() + message);
        e.printStackTrace();
    }

    private String getDate() {
        return sdf.format(new Date()) + " [Thread " + Thread.currentThread().getId() + "] ";
    }

    private String getClassName() {
        return ANSI_WHITE + this.clazz.getName() + ANSI_RESET + " ";
    }

    private static String info() {
        return WHITE_BRIGHT + "INFO " + ANSI_RESET;
    }

    private static String warn() {
        return ANSI_YELLOW + "WARN " + ANSI_RESET;
    }

    private static String debug() {
        return ANSI_BLACK + "DEBUG " + ANSI_RESET;
    }

    private static String trace() {
        return ANSI_WHITE + "TRACE " + ANSI_RESET;
    }

    private static String agent() {
        return BLUE_BRIGHT + "AGENT " + ANSI_RESET;
    }

    private static String error() {
        return RED_BRIGHT + "ERROR " + ANSI_RESET;
    }
}
