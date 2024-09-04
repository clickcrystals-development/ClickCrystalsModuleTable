package io.github.itzispyder.clickcrystalsmoduletable;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static final String[] categories = {
            "anchoring",
            "clickcrystals",
            "crystalling",
            "misc",
            "optimization",
            "rendering"
    };

    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();

        result.append("| **Module** | **Description** |\n");
        result.append("|:----------:|:---------------:|\n");
        readFiles(result);

        System.out.println(result);
        StringSelection data = new StringSelection(result.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(data, null);
        System.out.println("The table above has been successfully copied to system clipboard!");
    }

    public static void readFiles(StringBuilder builder) {
        try {
            String path = "src/main/java/io/github/itzispyder/clickcrystals/modules/modules/";

            for (String cat : categories) {
                String subPath = path + cat;
                File file = new File(subPath);
                File[] subFiles = file.listFiles();

                if (subFiles == null)
                    continue;

                String info;
                for (File javaFile : subFiles)
                    if ((info = readFileToRow(javaFile)) != null)
                        builder.append(info);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String readFileToRow(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        String contents = new String(fis.readAllBytes());
        fis.close();

        Pattern regex = Pattern.compile("\s*super\\(\\\"(.*)\\\"\\s*,\\s*Categories\\.[A-Z]+\\s*,\\s*\\\"(.*)\\\"\\);\s*");
        Matcher match;

        for (String line : contents.lines().toList()) {
            if ((match = regex.matcher(line)).matches()) {
                String name = snake2pascalCase(match.group(1));
                String desc = match.group(2).replaceAll("\\\\\"", "\"");
                return "| %s | %s |%n".formatted(name, desc);
            }
        }
        return null;
    }

    public static String snake2pascalCase(String s) {
        StringBuilder builder = new StringBuilder();
        for (String section : s.trim().split("[ _-]"))
            builder.append(capitalize(section));
        return builder.toString();
    }

    public static String capitalize(String s) {
        int len = s.length();
        if (len == 1)
            return s.toUpperCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}