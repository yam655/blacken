/* blacken - a library for Roguelike games
 * Copyright © 2012 Steven Black <yam655@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.googlecode.blacken.resources;

import com.googlecode.blacken.terminal.TerminalScreenSize;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Steven Black
 */
public class BlackenConfig {
    private Map<String, String> internalConfigMap = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackenConfig.class);
    public static final int MIN_ROWS = 25;
    public static final int MIN_COLS = 80;
    public static final TerminalScreenSize INITIAL_SIZE = TerminalScreenSize.SIZE_MEDIUM;
    private int minRows = 0;
    private int minCols = 0;
    private TerminalScreenSize initialSize = null;
    private float maxFontSize = 0;
    private String gameName = null;
    public BlackenConfig() {
        preload();
        setup(null);
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void override() {
        override(gameName);
    }

    /**
     * Set the gameName and override internal Blacken defaults with
     * user-provided (possibly game-specific) settings.
     *
     * @param gameName
     */
    public void override(String gameName) {
        this.gameName = gameName;
        if (gameName == null) {
            preload();
        }
        setup(gameName);
    }
    private void setup(String gameName) {
        String filename = "blacken.properties";
        if (gameName != null && gameName.endsWith(".properties")) {
            Path p = Paths.get(gameName);
            filename = p.getFileName().toString();
            gameName = p.getParent().toString();
        }
        String configName;
        try {
            configName = getSystemConfigFileName(gameName, filename);
        } catch (PlannedSecurityException ex) {
            LOGGER.warn("Could not get system configuration file location: {}", ex);
            LOGGER.info("Skipping further config attempts due to security exception.");
            return;
        }
        try {
            if (configName != null) {
                load(WriteUtils.readProperties(configName), configName);
            }
        } catch (IOException ex) {
            LOGGER.warn("Could not read system configuration file: {} {}", configName, ex);
        }
        try {
            configName = getUserConfigFileName(gameName, filename);
        } catch (PlannedSecurityException ex) {
            LOGGER.warn("Could not get user configuration file location: {}", ex);
        }
        try {
            if (configName != null) {
                load(WriteUtils.readProperties(configName), configName);
            }
        } catch (IOException ex) {
            LOGGER.warn("Could not read user configuration file: {} {}", configName, ex);
        }
    }

    private void preload() {
        Properties base;
        try {
            base = ResourceUtils.getResourceAsProperties(BlackenConfig.class, "blacken.properties");
            LOGGER.debug("Base blacken.properties loaded.");
        } catch (ResourceMissingException ex) {
            base = new Properties();
        }
        load(base, "internal blacken.properties resource.", true);
    }

    private void setMinRows(String s, String source) {
        if (s == null || s.isEmpty()) {
            return;
        }
        try {
            setMinRows(Integer.parseInt(s));
        } catch(NumberFormatException ex) {
            LOGGER.error("File {} contained invalid MIN_ROWS: {}", source, s);
        }
    }

    private void setMinCols(String s, String source) {
        if (s == null || s.isEmpty()) {
            return;
        }
        try {
            setMinCols(Integer.parseInt(s));
        } catch(NumberFormatException ex) {
            LOGGER.error("File {} contained invalid MIN_COLS: {}", source, s);
        }
    }

    private void setMinRows(int r) {
        if (r < 0) {
            r = 0;
        }
        this.minRows = r;
    }
    public int getMinRows() {
        return minRows;
    }

    private void setMinCols(int c) {
        if (c < 0) {
            c = 0;
        }
        this.minCols = c;
    }
    public int getMinCols() {
        return minCols;
    }

    private void load(Properties props, String source) {
        load(props, source, false);
    }

    private void setInternalConfig(String name, Properties props, String def) {
        this.internalConfigMap.put(name, props.getProperty(name, def));
    }
    public String getUserConfigFolder() throws PlannedSecurityException {
        return getUserConfigFolder(this.gameName);
    }

    /**
     * Get the best per-user configuration and save-game folder.
     *
     * <p>The "best" configuration and save-game folder varies by operating
     * system. In Linux and Mac OS X, it is easy -- just ~/.Blacken. In Windows
     * it prefers to use the "Saved Games" special directory, if present. It
     * will fall back to ~/Blacken -- but only if it can't find both
     * <code>~/Saved Games</code> and <code>%APPDATA%</code>.
     *
     * <p>On Windows, it always puts the Blacken configuration files in
     * <code>%APPDATA%/Blacken</code>. It will only put saved games there if
     * (1) a <code>gameName</code> directory already exists there, or
     * (2) the "Saved Games" directory does not exist.
     *
     * <p>This can be overridden with the "BLACKEN_HOME" variable. If this
     * variable is used it defines the ~/.Blacken directory, so it could be
     * used to switch between two alternate configurations. (Like
     * <code>~/.Blacken.dev</code> and <code>~/.Blacken.stable</code>.)
     *
     * <p>If <code>BLACKEN_OPT</code> is used, it completely overrides the
     * above logic and the only directory used is <code>${BLACKEN_OPT}/home</code>.
     * That location is used like the <code>~/.Blacken</code> directory.
     *
     * @param gameName system directory if null; otherwise game-specific directory
     * @return
     * @throws PlannedSecurityException
     */
    public String getUserConfigFolder(String gameName) throws PlannedSecurityException {
        String check;
        if (System.getenv("BLACKEN_OPT") != null) {
            check = System.getenv("BLACKEN_OPT") + "/home";
            if (gameName != null) {
                check += "/" + gameName;
            }
        } else if (System.getenv("BLACKEN_HOME") != null) {
            check = System.getenv("BLACKEN_HOME");
            if (gameName != null) {
                check += "/" + gameName;
            }
        } else {
            return getConfigFolder(gameName,
                    BlackenConfig.USER_CONFIG_FOLDER,
                    BlackenConfig.USER_CONFIG_FOLDER_SYSTEM,
                    BlackenConfig.USER_CONFIG_FOLDER_GAMES);
        }
        return check;
    }
    /**
     * If the directory exists (or can be created) return the directory name,
     * otherwise return null.
     *
     * <p>Returns <code>null</code> when <code>dirName</code> is <code>null</code>.
     *
     * @param dirName
     * @return
     */
    public String prepareDirectory(String dirName) {
        if (dirName == null) {
            return null;
        }
        File c = new File(dirName);
        if (!c.isDirectory()) {
            c.mkdirs();
        }
        if (!c.isDirectory()) {
            return null;
        }
        return dirName;
    }
    private String cleanGameName(String gameName) {
        if (gameName == null) {
            return null;
        }
        return gameName.replaceAll("[\\\\/:;'\"?*-]+", "-");
    }
    public String getSystemConfigFolder(String gameName) throws PlannedSecurityException {
        String check;
        gameName = cleanGameName(gameName);
        if (System.getenv("BLACKEN_OPT") != null) {
            check = System.getenv("BLACKEN_OPT") + "/etc";
            if (gameName != null) {
                check += "/" + gameName;
            }
        } else if (System.getenv("BLACKEN_ETC") != null) {
            check = System.getenv("BLACKEN_ETC");
            if (gameName != null) {
                check += "/" + gameName;
            }
        } else {
            return getConfigFolder(gameName,
                    BlackenConfig.SYSTEM_CONFIG_FOLDER,
                    null, null);
        }
        return check;
    }
    public String getSharedConfigFolder(String gameName) throws PlannedSecurityException {
        String check;
        if (System.getenv("BLACKEN_OPT") != null) {
            check = System.getenv("BLACKEN_OPT") + "/var";
            if (gameName != null) {
                check += "/" + gameName;
            }
        } else if (System.getenv("BLACKEN_VAR") != null) {
            check = System.getenv("BLACKEN_VAR");
            if (gameName != null) {
                check += "/" + gameName;
            }
        } else {
            return getConfigFolder(gameName,
                    BlackenConfig.SHARED_CONFIG_FOLDER,
                    null, null);
        }
        return check;
    }
    private String getConfigFolder(String gameName, String folder,
            String winSystem, String winGame) throws PlannedSecurityException {
        if (winSystem == null) {
            winSystem = folder;
        }
        if (winGame == null) {
            winGame = winSystem;
        }
        String var;
        String check;
        try {
            if (System.getProperty("os.name", "Other").contains("Win")) {
                if (gameName == null) {
                    var = WINDOWS_PREFIX + winSystem;
                } else {
                    var = WINDOWS_PREFIX + winGame;
                }
            } else {
                var = folder;
            }
            check = this.internalConfigMap.get(var);
            if (check != null && gameName != null) {
                check += "/" + gameName;
            }
        } catch(SecurityException ex) {
            throw new PlannedSecurityException(ex);
        }
        if (check != null) {
            check = expandVariables(check);
        }
        String fallback = null;
        if (check == null) {
            var += FALLBACK_SUFFIX;
            fallback = this.internalConfigMap.get(var);
            if (fallback != null && gameName != null) {
                fallback += "/" + gameName;
            }
            if (fallback != null) {
                fallback = expandVariables(fallback);
            }
        }
        if (fallback != null && new File(fallback).isDirectory()) {
            check = fallback;
        }
        return check;
    }

    static final private String WINDOWS_PREFIX = "windows.";
    static final private String FALLBACK_SUFFIX = ".fallback";
    static final private String USER_CONFIG_FOLDER = "user-config-folder";
    static final private String USER_CONFIG_FOLDER_GAMES = USER_CONFIG_FOLDER + ".games";
    static final private String USER_CONFIG_FOLDER_SYSTEM = USER_CONFIG_FOLDER + ".system";
    static final private String SHARED_CONFIG_FOLDER = "shared-config-folder";
    static final private String SYSTEM_CONFIG_FOLDER = "system-config-folder";

    private void load(Properties props, String source, boolean extended) {
        if (props == null) {
            return;
        }
        LOGGER.debug("Loading {}", source);
        this.setMinRows(props.getProperty("MIN_ROWS"), source);
        this.setMinCols(props.getProperty("MIN_COLS"), source);
        this.setMaxFontSize(props.getProperty("MAX_FONT_SIZE"), source);
        this.setInitialSize(props.getProperty("INITIAL_SIZE"), source);
        if (extended) {
            // These can get extremely messy so we're only implementing what we know we need.
            setInternalConfig(USER_CONFIG_FOLDER, props, null);
            setInternalConfig(WINDOWS_PREFIX + USER_CONFIG_FOLDER_GAMES, props, "~/Saved Games");
            setInternalConfig(WINDOWS_PREFIX + USER_CONFIG_FOLDER_SYSTEM, props, "${APPDATA}/Blacken");
            setInternalConfig(WINDOWS_PREFIX + USER_CONFIG_FOLDER_GAMES + FALLBACK_SUFFIX, props, "${APPDATA}/Blacken");

            setInternalConfig(WINDOWS_PREFIX + SHARED_CONFIG_FOLDER, props, "${ProgramData}/Blacken/Shared");
            setInternalConfig(WINDOWS_PREFIX + SHARED_CONFIG_FOLDER + FALLBACK_SUFFIX, props, "${ALLUSERSPROFILE}/Blacken/Shared");

            setInternalConfig(SHARED_CONFIG_FOLDER, props, "/var/games/blacken");
            setInternalConfig(SHARED_CONFIG_FOLDER + FALLBACK_SUFFIX, props, "/var/local/games/blacken");

            setInternalConfig(WINDOWS_PREFIX + SYSTEM_CONFIG_FOLDER, props, "${ProgramData}/Blacken/System");
            setInternalConfig(WINDOWS_PREFIX + SYSTEM_CONFIG_FOLDER + FALLBACK_SUFFIX, props, "${ALLUSERSPROFILE}/Blacken/System");

            setInternalConfig(SYSTEM_CONFIG_FOLDER, props, "/etc/blacken");
            setInternalConfig(SYSTEM_CONFIG_FOLDER + FALLBACK_SUFFIX, props, "/usr/local/etc/blacken");
        }
    }

    private void setInitialSize(String size, String source) {
        if (size == null || size.isEmpty()) {
            return;
        }
        String cleanSize = size;
        if (!cleanSize.startsWith("SIZE_")) {
            cleanSize = "SIZE_" + cleanSize;
        }
        try {
            TerminalScreenSize iSize = TerminalScreenSize.valueOf(cleanSize);
            setInitialSize(iSize);
        } catch(IllegalArgumentException ex) {
            LOGGER.error("File {} contained invalid INITIAL_SIZE: {}", source, size);
        }
    }

    private void setInitialSize(TerminalScreenSize initialSize) {
        this.initialSize = initialSize;
    }
    public TerminalScreenSize getInitialSize() {
        return initialSize;
    }

    private void setMaxFontSize(String s, String source) {
        if (s == null || s.isEmpty()) {
            return;
        }
        try {
            setMaxFontSize(Float.parseFloat(s));
        } catch(NumberFormatException ex) {
            LOGGER.error("File {} contained invalid MAX_FONT_SIZE: {}", source, s);
        }
    }

    public float getMaxFontSize() {
        return maxFontSize;
    }

    private void setMaxFontSize(float fsize) {
        if (fsize < 0f) {
            fsize = 0f;
        }
        this.maxFontSize = fsize;
    }
    /**
     * Get the user's home directory.
     *
     * <p>In general, games should always favor {@link #getConfigFolder(String)}
     * over ever using the results of this function.
     *
     * <p>Apparently, on Windows "user.home" is actually the Desktop directory's
     * parent directory, so on Windows we favor USERPROFILE and fallback to
     * HOMEDRIVE + HOMEPATH.
     *
     * @return the user's home directory
     * @throws PlannedSecurityException environment or properties are unavailable.
     * @see #getConfigFolder(String)
     */
    public String getUserHome() throws PlannedSecurityException {
        String ret = null;
        try {
            if (System.getProperty("os.name", "Other").contains("Win")) {
                // On Windows, user.home is the Desktop directory's parent directory
                ret = System.getenv("USERPROFILE");
                if (ret == null) {
                    String hpath = System.getenv("HOMEPATH");
                    String hdrive = System.getenv("HOMEDRIVE");
                    if (hpath != null && hdrive != null) {
                        ret = hpath + hdrive;
                    }
                }
            }
            if (ret == null) {
                ret = System.getProperty("user.home");
            }
        } catch(SecurityException ex) {
            throw new PlannedSecurityException(ex);
        }
        return ret;
    }

    /**
     * Expand a leading ~/ to the user's home directory, and
     * ${environment-variable} to environment variables.
     *
     * @param var
     * @return
     * @throws PlannedSecurityException 
     */
    public String expandVariables(String var) throws PlannedSecurityException {
        if (var.startsWith("~/")) {
            var = getUserHome() + var.substring(1);
        }
        if (var.contains("${")) {
            try {
                Pattern varPattern = Pattern.compile("[$][{]([^}]+)[}]");
                Matcher matcher = varPattern.matcher(var);
                StringBuffer buf = new StringBuffer();
                while (matcher.find()) {
                    String replace = System.getenv(matcher.group(1));
                    if (replace == null) {
                        return null;
                    }
                    matcher.appendReplacement(buf, Matcher.quoteReplacement(replace));
                }
                matcher.appendTail(buf);
                var = buf.toString();
            } catch(SecurityException ex) {
                throw new PlannedSecurityException(ex);
            }
        }
        return var;
    }
    public String getUserConfigFileName(String gameName, String subpath) throws PlannedSecurityException {
        String check = getUserConfigFolder(gameName);
        if (check == null) {
            return null;
        }
        if (!subpath.startsWith("/")) {
            return check + "/" + subpath;
        } else {
            return check + subpath;
        }
    }
    public String getSharedConfigFileName(String gameName, String subpath) throws PlannedSecurityException {
        String check = getSharedConfigFolder(gameName);
        if (check == null) {
            return null;
        }
        if (!subpath.startsWith("/")) {
            return check + "/" + subpath;
        } else {
            return check + subpath;
        }
    }
    public String getSystemConfigFileName(String gameName, String subpath) throws PlannedSecurityException {
        String check = getSystemConfigFolder(gameName);
        if (check == null) {
            return null;
        }
        if (!subpath.startsWith("/")) {
            return check + "/" + subpath;
        } else {
            return check + subpath;
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("MIN_ROWS=");
        buf.append(minRows);
        buf.append("\nMIN_COLS=");
        buf.append(minCols);
        buf.append("\nINITIAL_SIZE=");
        buf.append(initialSize.name());
        buf.append("\nMAX_FONT_SIZE=");
        buf.append(maxFontSize);
        buf.append("\nInternal configuration:\n");
        for (String name : internalConfigMap.keySet()) {
            buf.append(name);
            buf.append(": ");
            buf.append(internalConfigMap.get(name));
            buf.append("\n");
        }
        return buf.toString();
    }

}
