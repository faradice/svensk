package com.raggi.svenska;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Word {
    public static final String DEFAULT_FILE_NAME = "svenska.csv";
    public static final String SEPARATOR = ";";

    public final String svenska;
    public final String english;
    public final String sentence;
    public final String imageName;
    private int count;
    private int correct;

    public Word(String svenska, String english, String imageName, String sentence, int count, int correct) {
        this.svenska = svenska.trim();
        this.english = english.trim();
        this.imageName = imageName;
        this.count = count;
        this.correct = correct;
        this.sentence = sentence;
    }

    private static boolean isPhoto(String str) {
        if (str == null) return false;
        if (str.toLowerCase().contains("http")) return true;
        if (str.toLowerCase().contains(".jpg")) return true;
        if (str.toLowerCase().contains(".jpeg")) return true;
        return false;
    }

    public static List<Word> loadArray(String filename) {
        List<Word> result = new ArrayList<>();
        List<String> rows = Common.loadRows(filename, -1, true);
        for (String row : rows) {
            if (row.trim().length() < 1 || !row.contains(SEPARATOR)) {
                continue;
            }
            String[] cols = row.trim().split(";");
            String svenska = cols[0];
            String english = (cols.length > 1) ? cols[1] : "";
            String imageName = null;
            String sentance = null;

            for (int i=2; i < cols.length; i++) {
                String col = cols[i];
                if (isPhoto(col)) {
                    imageName = col;
                } else  {
                    sentance = col;
                }
            }

            if (cols.length > 0) {
                Word word = new Word(svenska, english, imageName, sentance, 0, 0);
                result.add(word);
            }
        }
        System.out.println(result.size() + " words loaded!");
        return result;
    }

    public String toString() {
        return svenska + "; " + english + "; " + sentence + "; " + imageName;
    }

    public String getImage() {
        if (imageName == null) {
            return "";
        }
        String result = "";
        String image = imageName.trim();
        if (image.startsWith("http")) {
            result = image.replace("jpeg", "jpg"); // Fix errors in some old input files
        } else {
            File f = new File("./image/" + image);
            result = "file://" + f.getAbsolutePath();
        }
        if (result.contains(" ")) {
            result = "";
        }
        return result;
    }

    public int increment(boolean isCorrect) {
        count++;
        if (isCorrect) {
            correct++;
        }
        return count;
    }

    public int getCorrect()  {
        return correct;
    }

    public int getCount()  {
        return correct;
    }

    public boolean isCorrect(String answer, boolean inSwedish) {
        String testStr = answer.toLowerCase().trim();
        String word = (inSwedish) ? svenska : english;
        boolean isCorrect = testStr.equals(word.toLowerCase().trim());
        return isCorrect;
    }

    public static void main(String[] args) {
    }
}
