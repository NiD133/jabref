package org.jabref.gui.entryeditor;

import java.util.Set;

import javafx.scene.control.Tab;

import org.jabref.logic.preferences.OwnerPreferences;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.Field;
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

    public void checkComment(BibEntry entry, OwnerPreferences ownerPreferences) {
        currentEntry = entry;
        String currentUser = "comment-" + ownerPreferences.getDefaultOwner();
        String currentUser1 = "-" + ownerPreferences.getDefaultOwner();
        String commentValue = currentEntry.getField(StandardField.COMMENT).orElse("");
        Set<Field> fields = currentEntry.getFields();
        for (Field field:fields) {
            if (field instanceof UnknownCommentField) {
                UnknownCommentField unknownField = (UnknownCommentField) field;
                String name = unknownField.getName();
                if (name.equals(currentUser)) {
                    System.out.println("Continue working on the comment!");
                }
            } else {
                System.out.println("Create a new user comment!" + currentUser);
                UnknownCommentField myCommentField = new UnknownCommentField(currentUser1);
//                currentEntry.clearField(StandardField.COMMENT);
                currentEntry.setField(myCommentField, commentValue);
            }
        }
//        if (entry.hasField(StandardField.COMMENT)) {
//            for (Field field:fields) {
//                if (field instanceof UnknownCommentField) {
//                    UnknownCommentField unknownField = (UnknownCommentField) field;
//                    String name = unknownField.getName();
//                    if (name.equals(currentComment)) {
//                        System.out.println("Continue working on the comment!");
//                    }
//                } else {
//                    System.out.println("Create a new user comment!");
//                }
//            }
//        } else {
//            System.out.println("Create the first user comment!");
//        }
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
