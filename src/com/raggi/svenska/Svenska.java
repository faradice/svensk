package com.raggi.svenska;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import sun.font.FontScaler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.raggi.svenska.Word.loadArray;

public class Svenska extends Application {
    private ImageView iview;
    private Button ok;
    private Button skip;
    private Button settings;
    TextField anwser;
    TextArea sentence;

    Label english;
    CheckBox hint;
    int wordIndex = -1;
    List<Word> correctList = new ArrayList<>();
    List<Word> wrongList = new ArrayList<>();
    List<Word> wordList = new ArrayList<>();
    Label correct;
    Label wrong;
    Random random = new Random();
    GridPane root;
    boolean hintWasRequested = false;

 //  String fileName = Word.DEFAULT_FILE_NAME;
 //   String fileName = "Linda_L1.csv";
 //   String fileName = "Linda_L2.csv";
 //   String fileName = "may22.csv";
//    String fileName = "DenSomOverliver.csv";
    String fileName = "5val1.csv";

//    String fileName = "Joakim_L4.csv";
 //     String fileName = "svart.csv";
 //     String fileName = "Linda_L3.csv";
//String fileName = "football.csv";

    boolean showEnglish = true;
    int numberOfWords = 3;
    int firstWord = 0;


    @Override
    public void start(Stage stage) {
        loadData();
        initUI(stage);
        next();
    }

    private void loadData() {
        wordList = loadArray(fileName);
        if (numberOfWords < 1) {
            numberOfWords = wordList.size();
        }
     }

    private void initUI(Stage stage) {
        iview = new ImageView();
        root = new GridPane();
        root.setVgap(15);
        root.setHgap(15);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setMinWidth(650);
        iview.setSmooth(true);

        ok = new Button("Next");
        ok.setDefaultButton(true);
        skip = new Button("Skip");
        settings = new Button("Settings");
        english = new Label();
        english.setFont(Font.font(16));
        english.setFont(english.getFont().font(16));
        correct = new Label("Correct: 0");
        wrong = new Label("Wrong: 0");
        anwser = new TextField();
        english.setFont(Font.font(16));
        sentence = new TextArea();
        hint = new CheckBox("Hint");
        hint.setFont(Font.font(14));
        hint.setSelected(false);
        sentence.setFont(Font.font(14));
        correct.setTextFill(Color.GREEN);
        wrong.setTextFill(Color.RED);

        anwser.setOnKeyTyped((KeyEvent event) -> {
            if (hint.isSelected()) {
                hintWasRequested = true;
                hint.setText("Hint");
                hint.setSelected(false);
                sentence.setText("");
            }
        });

        ok.setOnAction((ActionEvent actionEvent) -> next());
        settings.setOnAction((ActionEvent actionEvent) -> settings());
        skip.setOnAction((ActionEvent actionEvent) -> skip());
        hint.setOnAction((ActionEvent event) -> {
            if (hint.isSelected()) {
                String hintWord = (showEnglish) ? current().svenska : current().english;
                hint.setText("Hint: " + hintWord);
                sentence.setText(current().sentence);
            } else {
                hint.setText("Hint");
                sentence.setText("");
            }
        });

        root.add(iview, 0, 0, 2, 2);
        root.add(english, 3, 0);
        root.add(anwser, 3, 1);
        root.add(ok, 4, 1);
        root.add(hint, 3, 2);
        root.add(skip, 4, 2);
        root.add(correct, 3, 3);
        root.add(wrong, 4, 3);
        root.add(settings, 4, 4);
        root.add(sentence, 0, 5, 5,1);

        GridPane.setHalignment(wrong, HPos.RIGHT);
        GridPane.setHalignment(ok, HPos.RIGHT);
        GridPane.setHalignment(skip, HPos.RIGHT);
        GridPane.setHalignment(settings, HPos.RIGHT);

        GridPane.setValignment(iview, VPos.BASELINE);
        GridPane.setValignment(hint, VPos.TOP);
        GridPane.setValignment(correct, VPos.TOP);
        GridPane.setValignment(wrong, VPos.TOP);
        GridPane.setValignment(anwser, VPos.BASELINE);
        GridPane.setValignment(ok, VPos.BASELINE);
        GridPane.setValignment(settings, VPos.BASELINE);

        GridPane.setHgrow(iview, Priority.ALWAYS);
        GridPane.setHgrow(anwser, Priority.ALWAYS);
        GridPane.setHgrow(sentence, Priority.ALWAYS);
        GridPane.setVgrow(sentence, Priority.ALWAYS);

        GridPane.setMargin(wrong, new Insets(0, 10, 0, 0));
        GridPane.setMargin(ok, new Insets(0, 10, 0, 0));
        GridPane.setMargin(skip, new Insets(0, 10, 0, 0));
        GridPane.setMargin(settings, new Insets(0, 10, 0, 0));

        iview.setPreserveRatio(true);
        iview.setFitWidth(150);
        //       iview.fitWidthProperty().bind(main.widthProperty());
        //       iview.fitHeightProperty().bind(main.heightProperty());

        Scene scene = new Scene(root);
        stage.setTitle("Svenska");
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(400);
        stage.setMinHeight(200);
    }

    private void skip() {
        pickNextWord();
    }

    private void settings() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Settings");

        // Set the button types.
        ButtonType okBt = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBt, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 10));

        TextField first = new TextField();
        first.setPromptText("First word");
        TextField count = new TextField();
        count.setPromptText("Number of words");
        first.setText(String.valueOf(firstWord));
        count.setText(String.valueOf(numberOfWords));
        CheckBox inEnglish = new CheckBox("Show English");
        inEnglish.setSelected(showEnglish);

        gridPane.add(first, 0, 0);
        gridPane.add(new Label("words:"), 1, 0);
        gridPane.add(count, 2, 0);
        gridPane.add(inEnglish, 0,1);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> first.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okBt) {
                return new Pair<>(first.getText(), count.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            System.out.println("From=" + pair.getKey() + ", To=" + pair.getValue());
            firstWord = Integer.parseInt(first.getText());
            numberOfWords = Integer.parseInt(count.getText());
            showEnglish = inEnglish.isSelected();
        });
    }


    private void next() {
        if (wordIndex >= 0) {
            String text = anwser.getText();
            current().increment(current().isCorrect(text, showEnglish));
            wrongList.remove(current());
            correctList.remove(current());
            if (current().isCorrect(text, showEnglish)) {
                correctList.add(current());
            } else {
                wrongList.add(current());
            }
        }
        correct.setText("Correct: " + correctList.size());
        wrong.setText("Wrong: " + wrongList.size());
        pickNextWord();
    }

    private void pickNextWord() {
        boolean takeWrong = ((wrongList.size() > 4) || (wrongList.size() > 0 && random.nextInt(10) > 4));
        if (hintWasRequested) {
            // Use the same word again
            // wordIndex = wordIndex;
        } else if (takeWrong) {
            int wrongWordIndex = random.nextInt(wrongList.size());
            Word word = wrongList.get(wrongWordIndex);
            wordIndex = wordIndexOf(word);
        } else {
            wordIndex = random.nextInt(numberOfWords) + firstWord;
        }
        if (wordIndex >= wordList.size()) {
            wordIndex = wordList.size() - 1;
        }
        Word w = wordList.get(wordIndex);

        String imageName = w.getImage();
        if (imageName.length() > 1) {
            Image image = new Image(imageName);
            iview.setImage(image);
        }

        String wordToShow = (showEnglish) ? w.english : w.svenska;
        english.setText(wordToShow);
        anwser.setText("");
        sentence.setText("");
        anwser.requestFocus();
        hint.setSelected(false);
        hint.setText("Hint");
        hintWasRequested = false;
    }

    private int wordIndexOf(Word word) {
        for (int i = 0; i < wordList.size(); i++) {
            if (word.svenska.equalsIgnoreCase(wordList.get(i).svenska)) {
                return i;
            }
        }
        return -1;
    }

    private Word current() {
        return wordList.get(wordIndex);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
