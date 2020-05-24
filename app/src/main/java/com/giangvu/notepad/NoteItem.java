package com.giangvu.notepad;

import java.io.Serializable;

public class NoteItem implements Serializable {
    private int id;
    private String title;
    private String content;

    public NoteItem() {
    }

    public NoteItem(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String toString() {
        return "'Id: '" + this.id + ", 'Title: '" + this.title + ", 'Content:' " + this.content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
