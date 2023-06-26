package com.raggi.svenska;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Common {
    private static Charset charset = StandardCharsets.UTF_8;

    public static synchronized List<String> loadRows(String fileName, int maxRows, boolean skipComentRows) {
        ArrayList<String> reads = new ArrayList<String>();
        File file = new File(fileName);
        if (!file.exists()) {
            return reads;
        }
        if (maxRows < 1) {
            maxRows = Integer.MAX_VALUE;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            String row = null;
            int counter = 0;
            while ((row = br.readLine()) != null && counter < maxRows) {
                row = row.trim();
                if (skipComentRows && (row.startsWith("#") || row.startsWith("/") || (row.startsWith("<")))) {
                    continue;
                }
                reads.add(row);
                counter++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reads;
    }

    public static void writeRowsToFile(String fileName, List<String> rows, boolean append) {
        if (!append) {
            deleteFile(fileName);
        }
        initFile(fileName);
        try (PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), charset))) {
            for (String row : rows) {
                bw.println(row);
            }
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        file.delete();
    }

    public static File initFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file;
        }
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try {
            if (!file.exists()) {
                File newFile = new File(fileName);
                newFile.setReadable(true, false);
                newFile.setWritable(true, false);
                newFile.createNewFile();
                file = newFile;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }
}
