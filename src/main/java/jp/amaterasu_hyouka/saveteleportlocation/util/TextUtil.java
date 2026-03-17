package jp.amaterasu_hyouka.saveteleportlocation.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class TextUtil {

    public static Component plain(String string){
        return Component.text(string, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    }

    public static Component componentFrom(String string){
        return Component.text(string);
    }
    public static Component componentListFrom(List<String> strings){
        if(strings == null || strings.isEmpty()) return Component.empty();
        return Component.join(JoinConfiguration.separator(Component.newline()), strings.stream().map(s -> s == null ? Component.empty() : componentFrom(s)).toList());
    }


    public static Component clearItalic(String name){
        return Component.text(name).decoration(TextDecoration.ITALIC, false);
    }
    public static Component clearItalic(Component component){
        return component.decoration(TextDecoration.ITALIC, false);
    }
    public static List<Component> clearItalic(Component[] components){
        if(components == null || components.length == 0) return List.of(Component.empty());;
        return Arrays.stream(components).map(c -> c == null ? Component.empty() : clearItalic(c)).toList();
    }
    public static List<Component> clearItalic(List<Component> components){
        if(components == null || components.isEmpty()) return List.of(Component.empty());;
        return components.stream().map(c -> c == null ? Component.empty() : clearItalic(c)).toList();
    }

}
