package com.github.a1k28.dclagent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicClassAgent {
    private static final ConcurrentHashMap<String, String> classesToReplace = new ConcurrentHashMap<>();

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new DynamicClassFileTransformer());
    }

    public static void addClassToReplace(String className, String pathToNewClass) {
        classesToReplace.put(className.replace('.', '/'), pathToNewClass);
    }

    private static class DynamicClassFileTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String pathToNewClass = classesToReplace.get(className);
            if (pathToNewClass != null) {
                try {
                    return Files.readAllBytes(Paths.get(pathToNewClass));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null; // Return null to indicate no transformation
        }
    }
}