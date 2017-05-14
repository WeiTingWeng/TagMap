
package com.timweng.util;

import java.util.Arrays;
import java.util.HashSet;

public class Demo {

    public static void main(String[] args) {
        TagMap<Integer, String, String> tagMap = new TagMap<>();
        tagMap.put(1, new HashSet<String>(Arrays.asList("a", "b", "c")), "abc");
        printTagMap(tagMap);
        tagMap.put(2, new HashSet<String>(Arrays.asList("a", "b")), "abc");
        printTagMap(tagMap);
        tagMap.put(2, new HashSet<String>(Arrays.asList("a", "c")), "abc");
        printTagMap(tagMap);
        tagMap.remove(1);
        printTagMap(tagMap);
        tagMap.remove(2);
        printTagMap(tagMap);
    }

    private static void printTagMap(TagMap<Integer, String, String> tagMap) {
        System.out.println("===============");
        System.out.println("== Key to Value");
        for (Integer key : tagMap.keySet()) {
            System.out.println("" + key + "\t" + tagMap.getByKey(key));
        }
        System.out.println("== Tag to Value");
        for (String tag : tagMap.tagSet()) {
            for (String value : tagMap.getByTag(tag))
                System.out.println("" + tag + "\t" + value);
        }
    }
}
