import java.util.ArrayList;
import java.util.List;

public class TextEditor {

    private StringBuilder finalString= new StringBuilder(); // The text on which we will perform operations will be stored here
    private int startIndex = -1, endIndex = -1; // Maintain the text selection
    private int cursor = -1; // Current position of the cursor in the text editor
    private final List<String> clipboard = new ArrayList<>(); // Store history of texts that were copied

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    private void updateCursor(int offset) {
        this.cursor += offset;
    }

    private void updateFinalString() {
        String string = finalString.substring(0, startIndex) + finalString.substring(endIndex + 1);
        finalString.setLength(0);
        finalString.append(string);
    }

    // Checks whether a part of the text is currently selected
    private boolean textSelected() {
        return startIndex > -1 && endIndex > -1;
    }

    // Clears the selection
    private void resetSelection() {
        setStartIndex(-1);
        setEndIndex(-1);
    }

    // Add text in the text editor
    private void type(String text) {
        if(cursor + 1 < finalString.length()) {
            finalString.insert(cursor + 1, text);
        }
        else {
            finalString.append(text);
        }
        updateCursor(text.length());
    }

    // Select a part of the text
    private void select(String operation) {
        if(finalString.length() > 0) {
            setStartIndex(Integer.parseInt(operation.split(" ")[1]));
            setEndIndex(Integer.parseInt(operation.split(" ")[2]));
            setCursor(endIndex);
        }
    }

    // Move the in right/left direction
    private void moveCursor(int offset) {
        if(cursor + offset < finalString.length() || cursor + offset >= 0) {
            updateCursor(offset);
        }
        else if(cursor + offset < 0) {
            setCursor(-1);
        }
        else {
            setCursor(finalString.length() - 1);
        }

        // If there is a text selected, reset the selection
        if(startIndex > -1 && endIndex > -1) {
            resetSelection();
        }
    }

    // Copy the selected text
    private void copy() {
        if(textSelected()) {
            clipboard.add(finalString.substring(startIndex, endIndex + 1));
        }
    }

    /**
     * Paste a text from the clipboard
     *
     * @param steps_back : specifies which of the stored copied texts to insert.
     *                   If steps_back is 1, insert the last copied text
     *                   If steps_back is 2, insert the text copied before the last copied text
     * */
    private void paste(int steps_back) {
        String copiedText = clipboard.get(clipboard.size() - steps_back);
        if(steps_back <= clipboard.size()) {
            if(startIndex > -1 && endIndex > -1) {
                updateFinalString();
                finalString.insert(startIndex, copiedText);
                setCursor(startIndex + copiedText.length() - 1);
                resetSelection();
            }
            else {
                finalString.append(copiedText);
                updateCursor(copiedText.length() - 1);
            }
        }
    }

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();

        String[] operations = new String[]{"TYPE Exceed Expectations", "MOVE_CURSOR -19",
                "TYPE Always garbage text ", "MOVE_CURSOR 100", "TYPE try to", "SELECT 39 44", "COPY", "SELECT 7 18",
                "PASTE 1", "MOVE_CURSOR 20", "TYPE !", "SELECT 0 5", "COPY", "SELECT 6 6", "COPY", "SELECT 34 39", "PASTE 2",
                "SELECT 34 39", "PASTE"};

        for (String operation : operations){
            if (operation.startsWith("TYPE")){
                editor.type(operation.split("TYPE ")[1]);
            }
            else if (operation.startsWith("SELECT")) {
                editor.select(operation);
            }
            else if (operation.startsWith("MOVE_CURSOR")) {
                editor.moveCursor(Integer.parseInt(operation.split("MOVE_CURSOR ")[1]));
            }
            else if (operation.equals("COPY")) {
                editor.copy();
            }
            else if (operation.startsWith("PASTE")) {
                int steps_back = (operation.equals("PASTE")) ? 1 : Integer.parseInt(operation.split(" ")[1]);
                editor.paste(steps_back);
            }
        }

        System.out.println(editor.finalString.toString());
    }
}
