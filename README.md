# Dynamic Class Loader Agent

## Usage
```java
Class<?> reloaderClass = Class.forName("com.github.a1k28.dclagent.DynamicClassAgent");
Method getInstanceMethod = reloaderClass.getMethod("getInstance");
ClassReloaderAPI reloader = (ClassReloaderAPI) getInstanceMethod.invoke(null);
reloader.addClassToReload(classname, getTargetPath(classname));
reloader.reloadClasses();
```

Make sure to include the POM dependency:
```XML
<dependency>
    <groupId>com.github.a1k28</groupId>
    <artifactId>dynamic-classloader-agent</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/libs/com/github/a1k28/dclagent/dynamic-classloader-agent-1.0-SNAPSHOT.jar</systemPath>
</dependency>
```