package org.jabref.gui.entryeditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.scene.control.Tab;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.FieldProperty;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.field.UnknownCommentField;
import org.jabref.model.entry.types.EntryType;
import org.jabref.preferences.FilePreferences;

public abstract class EntryEditorTab extends Tab {

    protected BibEntry currentEntry;

    private FilePreferences filePreferences;

    /**
     * Needed to track for which type of entry this tab was build and to rebuild it if the type changes
     */
    private EntryType currentEntryType;

    /**
     * Decide whether to show this tab for the given entry.
     */
    public abstract boolean shouldShow(BibEntry entry);

    /**
     * Updates the view with the contents of the given entry.
     */
    protected abstract void bindToEntry(BibEntry entry);

    /**
     * The tab just got the focus. Override this method if you want to perform a special action on focus (like selecting
     * the first field in the editor)
     */
    protected void handleFocus() {
        // Do nothing by default
    }

    /**
     * Notifies the tab that it got focus and should display the given entry.
     */
    public void notifyAboutFocus(BibEntry entry) {
        if (!entry.equals(currentEntry) || !entry.getType().equals(currentEntryType)) {
            currentEntry = entry;
            currentEntryType = entry.getType();
            bindToEntry(entry);
        }
        handleFocus();
    }

    public void checkComment(BibEntry entry, FilePreferences filePreferences) {
        currentEntry = entry;
        String y = filePreferences.getUser();
        String currentComment = "comment-" + y;
//        UnknownCommentField commentA = new UnknownCommentField("johndoe");
//        UnknownCommentField commentB = new UnknownCommentField("janedoe");
//        currentEntry.setField(commentA, "This is comment a");
//        currentEntry.setField(commentB, "This is comment b");
        System.out.println("Switched to entry: " + currentEntry.getCitationKey());
        List<String> commentList = new ArrayList<>();
        Set<Field> fields = currentEntry.getFields();
        if (entry.hasField(StandardField.COMMENT)) {
            for (Field field:fields) {
                if (field instanceof UnknownCommentField) {
                    UnknownCommentField unknownField = (UnknownCommentField) field;
                    String name = unknownField.getName();
                    if (name.equals(currentComment)) {
                        System.out.println("Continue working on the comment!");
                    }
                } else {
                    System.out.println("Create a new user comment!");
                }
            }
        } else {
            System.out.println("Create the first user comment!");
        }
        Set<FieldProperty> commentSet = StandardField.COMMENT.getProperties();
        System.out.println(commentSet);
    }
    /**
     * Switch to next Preview style - should be overriden if a EntryEditorTab is actually showing a preview
     */
    protected void nextPreviewStyle() {
        // do nothing by default
    }

    /**
     * Switch to previous Preview style - should be overriden if a EntryEditorTab is actually showing a preview
     */
    protected void previousPreviewStyle() {
        // do nothing by default
    }
}
