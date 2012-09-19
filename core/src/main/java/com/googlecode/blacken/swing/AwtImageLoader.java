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

package com.googlecode.blacken.swing;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.exceptions.InvalidStringFormatException;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.resources.ResourceMissingException;
import com.googlecode.blacken.resources.ResourceUtils;
import com.googlecode.blacken.terminal.BlackenImageLoader;
import com.googlecode.blacken.terminal.PlainImageLoader;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 *
 * @author Steven Black
 */
public class AwtImageLoader implements BlackenImageLoader {
    private BufferedImage loadBufferedImage(Class resourceLoader,
            String resourceName) throws ResourceMissingException {
        BufferedImage img;
        try {
            img = ImageIO.read(resourceLoader.getResource(resourceName));
        } catch (IOException ex) {
            throw new ResourceMissingException(ex);
        }
        return img;
    }
    private Grid<Integer> convertToGrid(BufferedImage img, Properties prop) {
        Grid<Integer> grid = new Grid<>(0, img.getHeight(), img.getWidth(),
                                        0, 0, true);
        Set<Integer> transparent = new HashSet<>();
        for (String def : prop.getProperty("TRANSPARENT").split("\\s*;\\s*")) {
            int clr;
            try {
                clr = ColorHelper.makeColor(def);
            } catch (InvalidStringFormatException ex) {
                throw new RuntimeException(ex);
            }
            transparent.add(clr);
        }
        Integer alpha = null;
        String t = prop.getProperty("ALPHA");
        if (t != null) {
            if (t.isEmpty()) {
                alpha = 1;
            } else if ("*".equals(t)) {
                alpha = 255;
            } else {
                alpha = Integer.parseInt(t);
            }
        }
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Integer clr = img.getRGB(x, y);
                if (transparent.contains(clr)) {
                    clr = null;
                } else if (alpha != null && ColorHelper.getAlpha(clr) < alpha) {
                    clr = null;
                }
                grid.set(y, x, clr);
            }
        }
        return grid;
    }

    @Override
    public Grid<Integer> loadImage(Class resourceLoader, String resourceName) throws ResourceMissingException {
        if (resourceName.endsWith(".txt")) {
            return PlainImageLoader.getInstance().loadImage(resourceLoader, resourceName);
        }
        String propname = resourceName.replaceFirst("[.][^.]*$", ".properties");
        Properties prop = null;
        if (ResourceUtils.hasResource(resourceLoader, propname)) {
            prop = ResourceUtils.getResourceAsProperties(resourceLoader, propname);
        }
        BufferedImage img = loadBufferedImage(resourceLoader, resourceName);
        return convertToGrid(img, prop);
    }

    @Override
    public Grid<Integer> loadImage(Class resourceLoader, String resourceName,
                                   int height, int width) throws ResourceMissingException {
        if (resourceName.endsWith(".txt")) {
            return PlainImageLoader.getInstance().loadImage(resourceLoader, resourceName, height, width);
        }
        String propname = resourceName.replaceFirst("[.][^.]*$", ".properties");
        Properties prop = null;
        if (ResourceUtils.hasResource(resourceLoader, propname)) {
            prop = ResourceUtils.getResourceAsProperties(resourceLoader, propname);
        }
        BufferedImage src = loadBufferedImage(resourceLoader, resourceName);
        BufferedImage dest = new BufferedImage(width, height,
                                                   BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dest.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return convertToGrid(dest, prop);
    }

    @Override
    public Grid<Integer> loadImage(Class resourceLoader, String resourceName, String propertiesName) throws ResourceMissingException {
        if (resourceName.endsWith(".txt")) {
            return PlainImageLoader.getInstance().loadImage(resourceLoader, resourceName, propertiesName);
        }
        Properties prop = null;
        if (ResourceUtils.hasResource(resourceLoader, propertiesName)) {
            prop = ResourceUtils.getResourceAsProperties(resourceLoader, propertiesName);
        }
        BufferedImage img = loadBufferedImage(resourceLoader, resourceName);
        return convertToGrid(img, prop);
    }

    @Override
    public Grid<Integer> loadImage(Class resourceLoader, String resourceName, String propertiesName, int height, int width) throws ResourceMissingException {
        if (resourceName.endsWith(".txt")) {
            return PlainImageLoader.getInstance().loadImage(resourceLoader, resourceName, propertiesName, height, width);
        }
        Properties prop = null;
        if (ResourceUtils.hasResource(resourceLoader, propertiesName)) {
            prop = ResourceUtils.getResourceAsProperties(resourceLoader, propertiesName);
        }
        BufferedImage src = loadBufferedImage(resourceLoader, resourceName);
        BufferedImage dest = new BufferedImage(width, height,
                                                   BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dest.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return convertToGrid(dest, prop);
    }


}
