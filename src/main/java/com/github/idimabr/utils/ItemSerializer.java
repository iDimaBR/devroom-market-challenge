package com.github.idimabr.utils;

import org.bukkit.inventory.ItemStack;
import java.io.*;
import java.util.Base64;

public class ItemSerializer {

    public static String serialize(ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream)) {

            objectOutput.writeObject(item);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack deserialize(String base64) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
             ObjectInputStream objectInput = new ObjectInputStream(inputStream)) {

            return (ItemStack) objectInput.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
