package dev.jsinco.emfaddons.commands.subcommands;

import dev.jsinco.emfaddons.EMFAddons;
import dev.jsinco.emfaddons.Util;
import dev.jsinco.emfaddons.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GiveJournalCommand implements SubCommand {
    @Override
    public void execute(EMFAddons plugin, CommandSender sender, String[] args) {
        Player target = null;
        if (sender instanceof Player) {
            target = (Player) sender;
        } else if (args.length > 1) {
            target = Bukkit.getPlayerExact(args[1]);
        } else {
            return;
        }


        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Util.colorcode("&#2b5dfb&lA&#2e69fb&lq&#3275fb&lu&#3580fb&la&#388cfc&lt&#3c98fc&li&#3fa4fc&lc &#42affc&lJ&#45bbfc&lo&#49c7fc&lu&#4cd3fd&lr&#4fdefd&ln&#53eafd&la&#56f6fd&ll"));
        meta.setLore(List.of(Util.colorcode("&7Right-click to view"), Util.colorcode("&7your collected fish!")));
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "fish-diary"), PersistentDataType.BOOLEAN, true);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        Util.giveItem(target, item);
    }

    @Override
    public List<String> tabComplete(EMFAddons plugin, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermission() {
        return "emfaddons.fishdiary.give";
    }
}
