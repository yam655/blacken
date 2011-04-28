package com.googlecode.blacken.colors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.exceptions.InvalidStringFormatException;
import com.googlecode.blacken.exceptions.NoSuchColorException;



public class ColorProperties extends Properties {

    /**
     * 
     */
    private static final long serialVersionUID = 1617188851302820170L;

    /**
     * If you call loadNameMap(), it will process the entire properties file
     * (including defaults) to read in all the color names and cache them in
     * to the <code>completeNamesMap</code> private variable which is then used 
     * to perform all future name to color lookups. Additionally, all 
     * <code>ColorPalette</code>s that are created will get a common map,
     * which is especially handy as otherwise even though they came from the
     * same properties file they would only contain the names of the colors
     * that they use.
     */
    private HashMap<String, Integer> completeNameMap = null;

    /**
     * Create a new (empty) ColorProperties instance.
     */
    public ColorProperties() {
        super();
    }

    /**
     * Create a ColorProperties version of a Properties instance.
     * 
     * @param old an existing Properties instance
     */
    public ColorProperties(Properties old) {
        super(old);
    }

    /**
     * A private function to actually perform the loading of a set of palettes.
     * 
     * This loads a set of palettes.
     * 
     * @param start the starting property
     * @param palettes A map of the names of the palettes and the palettes
     */
    private void extendPalettes(String start, 
                                LinkedHashMap<String, ColorPalette> palettes) {
        if (start == null) {
            start = "__palettes__";
        }
        String found = getProperty(start);
        if (!found.startsWith("{")) {
            return;
        }
        if (palettes.containsKey(start)) {
            return;
        } else {
            palettes.put(start, null);
        }
        found = found.substring(1, found.length() -1);
        String[] allNames = found.split("[,; ]*");
        for(String key1 : allNames) {
            if (key1 == null || key1.isEmpty()) continue;
            found = getProperty(key1);
            if (found == null) {
                continue;
            } else if (found.startsWith("{")) {
                extendPalettes(key1, palettes);
            } else if (found.startsWith("[")) {
                if (!palettes.containsKey(key1)) {
                    palettes.put(key1, getColorPalette(key1));
                }
            }
        }
    }

    /**
     * get a Integer from data contained in the properties file.
     * 
     * Of note, a color can state that it is a copy of an existing color
     * by referencing another color name instead of a color value.
     * 
     * @param key property key for the color
     * @return color value
     * @throws NoSuchColorException 
     */
    public int getColor(String key) throws NoSuchColorException{
        if (completeNameMap != null) {
            if (!completeNameMap.containsKey(key)) {
                throw new NoSuchColorException(key);
            }
            return completeNameMap.get(key);
        }
        HashSet<String> refs = new HashSet<String>();
        String ret = null;
        ret = key;
        refs.add(key);
        do{
            key = ret;
            ret = getProperty(key);
            if (ret == null || ret.startsWith("[") || ret.startsWith("{")) {
                if (ret != null){
                    ret = null;
                }
            } else if (!ColorHelper.isColorDefinition(ret)){
                if (refs.contains(ret)){
                    ret = null;
                } else {
                    refs.add(ret);
                }
            }
        }while(ret != null && !!ColorHelper.isColorDefinition(ret));
        int color;
        if(ret != null) {
            try {
                color = ColorHelper.makeColor(ret);
            } catch (InvalidStringFormatException e) {
                throw new NoSuchColorException(key, e);
            }
        } else throw new NoSuchColorException(key);
        return color;
    }

    public ArrayList<Integer> getColorList(String key) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        HashSet<String> refs = new HashSet<String>();
        referenceColorList(ret, refs, key, null);
        return ret;
    }

    /**
     * Get the default color map. (specified with __default_palette__)
     * @return the current color map.
     */
    public LinkedHashMap<String, Integer> getColorMap() {
        String key = null;
        return getColorMap(key);
    }

    /**
     * Get a palette, represented as a LinkedHashMap.
     * 
     * @param key The name of the palette
     * @return a palette, stored as a LinkedHashMap.
     */
    public LinkedHashMap<String, Integer> getColorMap(String key) {
        if (key == null) {
            key = "__default_palette__";
        }
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        HashSet<String> refs = new HashSet<String>();
        referenceColorList(null, refs, key, map);
        return map;
    }

    /**
     * Get a palette, represented as a ColorPalette
     * 
     * @param key The name of the palette
     * @return a palette, stored as a ColorPalette
     */
    public ColorPalette getColorPalette(String key) {
        if (completeNameMap == null){
            LinkedHashMap<String, Integer> map = getColorMap(key);
            return new ColorPalette(map);
        } else {
            HashSet<String> refs = new HashSet<String>();
            ColorPalette pal = new ColorPalette();
            referenceColorList(pal, refs, key);
            return pal;
        }
    }

    /**
     * Return the default set of palettes from a properties file.
     * 
     * The default set is specified with the __palettes__ key.
     * 
     * @return a palette_name to palette mapping
     */
    public LinkedHashMap<String, ColorPalette> getPalettes() {
        return getPalettes(null);
    }

    /**
     * We support getting a list of palettes sharing a common set of names
     * from a properties file.
     * 
     * @param start the starting properties file element
     * @return a palette_name to palette mapping
     */
    public LinkedHashMap<String, ColorPalette> getPalettes(String start) {
        LinkedHashMap<String, ColorPalette> palettes =
            new LinkedHashMap<String, ColorPalette>();
        extendPalettes(start, palettes);
        return palettes;
    }

    public HashMap<String, Integer> loadNameMap(){
        completeNameMap = null;
        HashMap<String, Integer> newCompleteNameMap = new HashMap<String, Integer>();
        Integer color;
        for (String name : stringPropertyNames()) {
            try {
                color = getColor(name);
            } catch (NoSuchColorException e) {
                continue;
            }
            if(color != null){
                newCompleteNameMap.put(name, color);
            }
        }
        completeNameMap = newCompleteNameMap;
        if (completeNameMap.isEmpty()) {
            completeNameMap = null;
        }
        return completeNameMap;
    }

    private void referenceColorList(ColorPalette pal,  
                                    HashSet<String> processed, 
                                    String key) {
        referenceColorList(pal, null, processed, key, null);
    }
    private void referenceColorList(List<Integer> list, 
                                    HashSet<String> processed, 
                                    String key, Map<String, Integer> map) {
        referenceColorList(null, processed, key, map);
    }

    
    /**
     * Private function to recursively process new palettes.
     * 
     * @param list The ordered list of colors
     * @param processed indicator for whether we've processed an palette
     * @param key The name of the palette in the file
     * @param map The nameMap for the color names.
     */
    private void referenceColorList(ColorPalette pal, List<Integer> list, 
                                    HashSet<String> processed, 
                                    String key, Map<String, Integer> map) {
        String found = null;
        found = key;
        processed.add(found);
        found = getProperty(key);
        String[] allNames = {key};
        if (list != null && completeNameMap != null){
            // If we have a completeNameMap we can ignore the nameMap,
            // but only if we have another way to return the order.
            // Otherwise we return the order through a LinkedHashMap
            // with all of the values coming from the completeNameMap.
            map = null;
        }
        if (found.startsWith("[")) {
            found = found.substring(1, found.length() -1);
            allNames = found.split("[,; ]*");
        }
        int color;
        for(String key1 : allNames) {
            if (key1 == null || key1.isEmpty()) continue;
            found = getProperty(key1);
            if (found == null || found.startsWith("{")) {
                continue;
            } else if (found.startsWith("[")) {
                if (!processed.contains(key1)){
                    referenceColorList(pal, list, processed, key1, map);
                }
            } else if (!ColorHelper.isColorDefinition(found)){
                try {
                    color =  ColorHelper.makeColor(found);
                } catch (InvalidStringFormatException e) {
                    continue;
                }
                if (pal != null) {
                    pal.put(key1, color);
                } else {
                    if (map != null) {
                        map.put(key1, color);
                    }
                    if (list != null) {
                        list.add(color);
                    }
                }
            } else {
                try {
                    color = ColorHelper.makeColor(found);
                } catch (InvalidStringFormatException e) {
                    continue;
                }
                if (pal != null) {
                    pal.put(key1, color);
                } else {
                    if (map != null) {
                        map.put(key1, color);
                    }
                    if (list != null) {
                        list.add(color);
                    }
                }
            }
        }
    }

    /**
     * Set a property to a value of a Integer.
     * 
     * @param key The name of the color
     * @param color The color value
     * @return results from setProperty.
     */
    public Object setColor(String key, Integer color) {
        if (completeNameMap != null) {
            completeNameMap.put(key, color);
        }
        return setProperty(key, color.toString());
    }

}
