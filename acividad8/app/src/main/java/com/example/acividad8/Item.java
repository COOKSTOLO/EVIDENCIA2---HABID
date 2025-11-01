package com.example.acividad8;

public class Item {
    private long id;
    private String title;
    private int seasons;
    private String releaseDate; // store as ISO date string yyyy-MM-dd

    public Item() {}

    public Item(long id, String title, int seasons, String releaseDate) {
        this.id = id;
        this.title = title;
        this.seasons = seasons;
        this.releaseDate = releaseDate;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getSeasons() { return seasons; }
    public void setSeasons(int seasons) { this.seasons = seasons; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}

