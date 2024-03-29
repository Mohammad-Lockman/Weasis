/*
 * Copyright (c) 2009-2020 Weasis Team and other contributors.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0, or the Apache
 * License, Version 2.0 which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package org.weasis.core.api.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weasis.core.util.StringUtil;

public class ResourceUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtil.class);

  private static AtomicReference<String> path = new AtomicReference<>(StringUtil.EMPTY_STRING);

  private ResourceUtil() {}

  public static String getResource(String resource, Class<?> c) {
    URL url = getResourceURL(resource, c);
    return url == null ? null : url.toString();
  }

  public InputStream getResourceAsStream(String name, Class<?> c) {
    URL url = getResourceURL(name, c);
    try {
      return url != null ? url.openStream() : null;
    } catch (IOException e) {
      LOGGER.error("Cannot read resource", e);
    }
    return null;
  }

  public static URL getResourceURL(String resource, Class<?> c) {
    URL url = null;
    if (c != null) {
      ClassLoader classLoader = c.getClassLoader();
      if (classLoader != null) {
        url = classLoader.getResource(resource);
      }
    }
    if (url == null) {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader != null) {
        url = classLoader.getResource(resource);
      }
    }
    if (url == null) {
      url = ClassLoader.getSystemResource(resource);
    }
    return url;
  }

  public static ImageIcon getLargeLogo() {
    return getLogo("images" + File.separator + "about.png"); // NON-NLS
  }

  public static ImageIcon getIconLogo64() {
    return getLogo("images" + File.separator + "logo-button.png"); // NON-NLS
  }

  public static ImageIcon getLogo(String filename) {
    File file = getResource(filename);
    if (file.canRead()) {
      try {
        return new ImageIcon(file.toURI().toURL());
      } catch (Exception e) {
        LOGGER.error("Cannot read logo image:{}", filename, e);
      }
    }
    return null;
  }

  public static void setResourcePath(String path) {
    if (!StringUtil.hasText(path)) {
      throw new IllegalArgumentException("No value for property: weasis.resources.path");
    }
    ResourceUtil.path.set(path);
  }

  private static String getResourcePath() {
    return path.get();
  }

  public static File getResource(String filename) {
    if (!StringUtil.hasText(filename)) {
      throw new IllegalArgumentException("Empty filename");
    }
    return new File(getResourcePath(), filename);
  }

  public static File getResource(String filename, String... subFolderName) {
    if (!StringUtil.hasText(filename)) {
      throw new IllegalArgumentException("Empty filename");
    }
    String path = getResourcePath();
    if (subFolderName != null) {
      StringBuilder buf = new StringBuilder(path);
      for (String s : subFolderName) {
        buf.append(File.separator);
        buf.append(s);
      }
      path = buf.toString();
    }
    return new File(path, filename);
  }
}
