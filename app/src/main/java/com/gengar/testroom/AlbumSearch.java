package com.gengar.testroom;


import com.deezer.sdk.player.AlbumPlayer;

public class AlbumSearch {

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public long getAlbumId() {
        return albumId;
    }

    public float getLenght() {
        return lenght;
    }

    private float lenght;
    private String title;
    private String artistName;
    private String albumName;
    private long albumId;

    public AlbumSearch(long albumId,String albumName,float lenght,String artistName,String title){
        this.albumId = albumId;
        this.albumName = albumName;
        this.title = title;
        this.artistName = artistName;
        this.lenght = lenght;
    }


}
