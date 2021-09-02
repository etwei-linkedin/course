package com.example.course.ioc;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 *  Dependency injection => IOC (IOC container)
 *  why IOC ?
 * @Component -> mark bean -> container
 * @Autowired -> inject bean
 */
public class Container {
    private final Map<String, Object> objectFactory = new HashMap<>();

    public static void start() throws Exception {
        Container c = new Container();
        List<Class<?>> classes = c.scan();
        c.register(classes);
        c.injectObjects(classes);
    }

    private List<Class<?>> scan() {
        return Arrays.asList(StudentRegisterService.class, StudentApplication.class, Starter.class);
    }

    private boolean register(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            Annotation[] annotations = clazz.getAnnotations();
            for (Annotation a : annotations) {
                if (a.annotationType() == Component.class) {
                    objectFactory.put(clazz.getSimpleName(), clazz.getDeclaredConstructor(null).newInstance());
                }
            }
        }
        return true;
    }

    private boolean injectObjects(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            Field[] fields = clazz.getDeclaredFields();
            Object curInstance = objectFactory.get(clazz.getSimpleName());

            // filter all fields that are annotated with @Autowired
            List<Field> autowiredFields = Arrays.stream(fields)
                .filter(f -> {
                    Annotation[] annotations = f.getAnnotations();
                    return Arrays.stream(annotations)
                        .anyMatch(annotation -> annotation.annotationType() == Autowired.class);
                })
                .collect(Collectors.toList());

            List<Field> kualifierFields = autowiredFields.stream()
                .filter(f -> {
                    Annotation[] annotations = f.getAnnotations();
                    return Arrays.stream(annotations)
                        .anyMatch(annotation -> annotation.annotationType() == Kualifier.class);
                })
                .collect(Collectors.toList());

            // After this removal, autowiredFields only contains fields with @Autowired annotated
            autowiredFields.removeAll(kualifierFields);

            // Inject all Autowired only fields
            for (Field f: autowiredFields)
                injectInstance(curInstance, f);

            // Inject all Beans with both @Autowired and @Kualifier
            for (Field f: kualifierFields) {
                injectInstance(curInstance, f, classes);
            }
        }
        return true;
    }

    /**
     * Inject the autowired only instance to the targeting field of the targeting object.
     */
    private void injectInstance(Object curInstance, Field f) throws IllegalAccessException {
        Class<?> type = f.getType();
        Object injectInstance = objectFactory.get(type.getSimpleName());
        f.setAccessible(true);
        f.set(curInstance, injectInstance);
    }

    /**
     * Inject instance for the field with @Kualifier annotated to the targeting object.
     */
    private void injectInstance(Object curInstance, Field f, List<Class<?>> injectedClasses) throws IllegalAccessException {
        Optional<Class<?>> targetClassOption = injectedClasses.stream()
            .filter(clazz -> Optional.ofNullable(clazz.getAnnotation(Kualifier.class))
                .map(kualifierAnnotation -> kualifierAnnotation.equals(f.getAnnotation(Kualifier.class)))
                .orElse(false)
            )
            .findFirst();

        if (targetClassOption.isPresent()) {
            Object injectInstance = objectFactory.get(targetClassOption.get().getSimpleName());
            f.setAccessible(true);
            f.set(curInstance, injectInstance);
        }
    }
}