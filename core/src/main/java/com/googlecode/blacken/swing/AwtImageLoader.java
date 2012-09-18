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

import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.terminal.BlackenImageLoader;
import com.googlecode.blacken.terminal.UnboundTerminal;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Steven Black
 */
public class AwtImageLoader implements BlackenImageLoader {
    private BufferedImage loadBufferedImage(Class resourceLoader, String resourceName) {
        BufferedImage img;
        try {
            img = ImageIO.read(resourceLoader.getResource(resourceName));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return img;
    }
    private Grid<Integer> convertToGrid(BufferedImage img) {
        Grid<Integer> grid = new Grid<>(0, img.getHeight(), img.getWidth());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                grid.set(y, x, img.getRGB(x, y));
            }
        }
        return grid;
    }

    @Override
    public Grid<Integer> loadImage(Class resourceLoader, String resourceName) {
        BufferedImage img = loadBufferedImage(resourceLoader, resourceName);
        return convertToGrid(img);
    }

    @Override
    public Grid<Integer> loadImage(Class resourceLoader, String resourceName,
                                   int height, int width) {
        BufferedImage src = loadBufferedImage(resourceLoader, resourceName);
        BufferedImage dest = new BufferedImage(width, height,
                                                   BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dest.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return convertToGrid(dest);
    }


}
