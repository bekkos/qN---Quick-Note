package com.example.qn.models;

public class Note {

    private int id;
    private String name;
    private String content;
    private int notebook_id;

    public Note () {}

    public Note(String name, String content, int notebook_id) {
        this.name = name;
        this.content = content;
        this.notebook_id = notebook_id;
    }

    public Note(int id, String name, String content, int notebook_id) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.notebook_id = notebook_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNotebook_id() {
        return notebook_id;
    }

    public void setNotebook_id(int notebook_id) {
        this.notebook_id = notebook_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
