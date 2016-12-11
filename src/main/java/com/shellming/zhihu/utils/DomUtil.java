package com.shellming.zhihu.utils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by ruluo1992 on 10/1/2016.
 */
public class DomUtil {
    public static Element findElement(Element element, List<String> cls) {
        for (String c : cls) {
            if (element.hasClass(c)) {
                element = element.getElementsByClass(c).first();
            }
        }
        return element;
    }

    public static Element getFirstElementByClass(Element root, String className) {
        Elements elements = root.getElementsByClass(className);
        if (elements != null && elements.first() != null) {
            return elements.first();
        } else {
            return null;
        }
    }
}
