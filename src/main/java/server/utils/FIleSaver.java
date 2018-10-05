package server.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FIleSaver {
    private static final String AVATAR_FOLDER = "D:/files/avatars/";
    public static void saveAvatar(byte[] avatar, String name)
    {
        try(FileOutputStream outputStream = new FileOutputStream(new File(AVATAR_FOLDER+name))) {
            outputStream.write(avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static byte[] loadAvatarFor(String name)
    {
        byte[] result = null;

        if (name == null)
        {
            System.out.println("Получил null");
            return null;
        }

        try {
            Path p = Paths.get(AVATAR_FOLDER+name+".png");
            result = Files.readAllBytes(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
}
