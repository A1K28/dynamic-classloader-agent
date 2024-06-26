package com.github.a1k28.dclagent;

public interface ClassReloaderAPI {
    void addClassToReload(String className, String pathToNewClass);
    void reloadClasses();
}