package com.github.fernthedev.fernapi.server.spigot.util

import com.github.fernthedev.fernapi.universal.data.chat.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GuiUtil {

    @JvmStatic
    fun build(material: Material, amount: Int = 1): ItemStack {
        return build(material = material, amount = amount, name = null, lore = null)
    }

    @JvmStatic
    fun build(material: Material, amount: Int = 1, name: String? = null): ItemStack {
        return build(material = material, amount = amount, name = name, lore = null)
    }

    @JvmStatic
    fun build(material: Material, amount: Int = 1, lore: List<String?>? = null): ItemStack {
        return build(material = material, amount = amount, name = null, lore = lore)
    }

    @JvmStatic
    fun build(material: Material, name: String? = null): ItemStack {
        return build(material = material, amount = 1, name = name, lore = null)
    }

    @JvmStatic
    fun build(material: Material, lore: List<String?>? = null): ItemStack {
        return build(material = material, amount = 1, name = null, lore = lore)
    }

    @JvmStatic
    fun build(material: Material, name: String? = null, lore: List<String?>? = null): ItemStack {
        return build(material = material, amount = 1, name = name, lore = lore)
    }

    @JvmStatic
    fun build(material: Material?, amount: Int = 1, name: String? = null, lore: List<String?>? = null): ItemStack {
        val itemStack = ItemStack(material!!, amount)
        val meta = itemStack.itemMeta!!

        if (name != null)
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name))

        if (lore != null)
            meta.lore = lore

        itemStack.itemMeta = meta
        return itemStack
    }
}