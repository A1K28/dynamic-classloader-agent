package com.github.a1k28.dclagent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.ClassDefinition;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicClassAgent implements ClassReloaderAPI {
    private static final Logger log = Logger.getInstance(DynamicClassAgent.class);
    private static DynamicClassAgent INSTANCE;
    private static Instrumentation instrumentation;
    private final ConcurrentHashMap<String, String> classesToReload = new ConcurrentHashMap<>();

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
        INSTANCE = new DynamicClassAgent();
    }

    public static DynamicClassAgent getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("DynamicClassAgent not initialized. Make sure the agent is loaded.");
        }
        return INSTANCE;
    }

    @Override
    public void addClassToReload(String className, String pathToNewClass) {
        classesToReload.put(className, pathToNewClass);
    }

    @Override
    public void reloadClasses() {
        if (instrumentation == null) {
            throw new RuntimeException("Instrumentation not initialized. Make sure the agent is loaded.");
        }

        for (String className : classesToReload.keySet()) {
            try {
                Class<?> classToReload = Class.forName(className);
                String pathToNewClass = classesToReload.get(className);
                byte[] newClassBytes = Files.readAllBytes(Paths.get(pathToNewClass));
                
                ClassDefinition definition = new ClassDefinition(classToReload, newClassBytes);
                instrumentation.redefineClasses(definition);
                
                log.agent("Reloaded class: " + className);
            } catch (Exception e) {
                log.error("Failed to reload class: " + className, e);
            }
        }
        
        classesToReload.clear();
    }
}