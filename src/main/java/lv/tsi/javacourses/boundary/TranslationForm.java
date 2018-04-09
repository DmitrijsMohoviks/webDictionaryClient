package lv.tsi.javacourses.boundary;

import lv.tsi.javacourses.control.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.*;
import java.net.*;

@ViewScoped
@Named
public class TranslationForm implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(TranslationForm.class);

    public static final String SERVER = "dict.org";
    public static final int PORT = 2628;
    public static final int TIMEOUT = 15000;
    private String[] words;
    private String input = "";
    private StringBuilder translatedWords = new StringBuilder();
    private String translation;
    String errorInput = "";

    public void translate() {
        errorInput = "";
        if (input.length()==0) {
            return;
        }
        words = input.split(" ");
        Socket socket = null;
        try {
            socket = new Socket(SERVER, PORT);
            socket.setSoTimeout(TIMEOUT);
            OutputStream out = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            writer = new BufferedWriter(writer);
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            logger.info("Words for translation " + input);
            translatedWords = new StringBuilder();
            for (String word : words) {
                String checkTranslationForNull = define(word, writer, reader);
                if (checkTranslationForNull != null) {
                    translatedWords.append(checkTranslationForNull);
                }
            }
            setTranslation(translatedWords.toString());
            if (errorInput.length() != 0) {
                Util.addError("translationForm:textInput", "No definition found for " + errorInput);
            }
            logger.info("Translation " + translation);
            writer.write("quit\r\n");
            writer.flush();
        } catch (IOException ex) {
            System.err.println(ex);
        } finally { // dispose
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }
    }

    private String define(String word, Writer writer, BufferedReader reader)
            throws IOException, UnsupportedEncodingException {
        writer.write("DEFINE fd-eng-rus " + word + "\r\n");
        writer.flush();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("250 ")) { // OK
                continue;
            } else if (line.startsWith("552 ")) { // no match
                errorInput = errorInput + " " + word;
                System.out.println("No definition found for " + word);
                return null;
            } else if (line.matches("\\d\\d\\d .*")) continue;
            else if (line.trim().equals(".")) continue;
            else if (line.endsWith("/")) continue; // this line contains transcription
            else {
                return line;
            }
        }
        return null;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}