package org.shimado.basicutils.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookUtil {

    public static void openBook(Player player, List<List<String>> bookText){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle(UUID.randomUUID().toString().substring(0, 6));
        meta.setAuthor(player.getName());
        colorBookText(meta, bookText);
        book.setItemMeta(meta);
        player.openBook(book);
    }


    private static void colorBookText(BookMeta meta, List<List<String>> text){
        List<String> result = new ArrayList<>();
        for (int k = 0; k < text.size(); k++) {
            String page = "";
            for (int i = 0; i < text.get(k).size(); i++) {
                page = ((page.length() > 0) ? page + "\n" : page) +  text.get(k).get(i);
            }
            result.add(ColorUtil.getColor(page));
        }
        meta.setPages(result);
    }

}
