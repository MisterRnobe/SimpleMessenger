package server.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSaver {
    private static final String HOME_FOLDER = "D:/files/";
    private static final String AVATAR_FOLDER = "avatars/";
    private static final String USER_AVATAR_FOLDER = "usr/";
    private static final String GROUP_AVATAR_FOLDER = "grp/";
    private static final String AVATAR_FORMAT = ".png";
    public static void saveUserAvatar(byte[] avatar, String name)
    {
        try(FileOutputStream outputStream = new FileOutputStream(new File(HOME_FOLDER+AVATAR_FOLDER+USER_AVATAR_FOLDER+name + AVATAR_FORMAT))) {
            outputStream.write(avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveGroupAvatar(byte[] avatar, Integer dialogId)
    {
        try(FileOutputStream outputStream = new FileOutputStream(new File(HOME_FOLDER+AVATAR_FOLDER+GROUP_AVATAR_FOLDER+dialogId + AVATAR_FORMAT))) {
            outputStream.write(avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getPathForUserAvatar(String login)
    {
        return AVATAR_FOLDER + USER_AVATAR_FOLDER + login + AVATAR_FORMAT;
    }
    public static String getPathForGroupAvatar(Integer dialogId)
    {
        return AVATAR_FOLDER + GROUP_AVATAR_FOLDER + dialogId + AVATAR_FORMAT;
    }
    public static byte[] loadAvatar(String path)
    {
        byte[] result = null;

        if (path == null)
        {
            System.out.println("Получил null");
            return null;
        }

        try {
            Path p = Paths.get(HOME_FOLDER + path);
            result = Files.readAllBytes(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
}