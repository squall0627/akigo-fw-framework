/*
 * ReflectionUtils.java
 * Created on  2020/10/20 18:55
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.util;

import com.akigo.core.exception.SystemException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Reflectionユーティリティー<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class ReflectionUtils {

    public interface ScanPackageFilter {
        boolean accept(Class<?> clazz);
    }

    public static Set<Class<?>> getAllClassesWith(String packageName, Class<? extends Annotation> annotationClass) {
        return scanPackage(packageName, c -> c.isAnnotationPresent(annotationClass));
    }

    public static Set<Class<?>> scanPackage(String packageName) {
        return scanPackage(packageName, c -> true);
    }

    public static Set<Class<?>> scanPackage(String packageName, ScanPackageFilter filter) {
        Set<Class<?>> results = new LinkedHashSet<>();

        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs = null;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    results.addAll(findAndAddClassesInPackageByFile(packageName, filePath, filter));
                } else if ("jar".equals(protocol)) {
                    JarFile jar = null;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
                                            if (filter.accept(clazz)) {
                                                results.add(clazz);
                                            }
                                        } catch (ClassNotFoundException e) {
                                            throw new SystemException(e);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new SystemException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new SystemException(e);
        }
        return results;
    }

    private static Set<Class<?>> findAndAddClassesInPackageByFile(String packageName, String packagePath, ScanPackageFilter filter) {
        Set<Class<?>> results = new LinkedHashSet<>();

        Path dir = Paths.get(packagePath);

        if (Files.exists(dir) && Files.isDirectory(dir)) {
            File[] files = dir.toFile().listFiles(file -> file.isDirectory() || file.getName().endsWith(".class"));

            for (File file : files) {
                if (file.isDirectory()) {
                    results.addAll(findAndAddClassesInPackageByFile(packageName + '.' + file.getName(), file.getAbsolutePath(), filter));
                } else {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
                        if (filter.accept(clazz)) {
                            results.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new SystemException(e);
                    }
                }
            }
        }
        return results;
    }
}
