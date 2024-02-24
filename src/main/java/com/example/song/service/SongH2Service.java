package com.example.song.service;

import com.example.song.repository.*;
import com.example.song.model.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
/*
 * 
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class SongH2Service implements SongRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Song> getSongs() {
        List<Song> songList = db.query("SELECT * FROM PLAYLIST", new SongRowMapper());
        ArrayList<Song> songs = new ArrayList<>(songList);
        return songs;
    }

    @Override
    public Song getSongById(int songId) {
        try {
            Song song = db.queryForObject("SELECT * FROM PLAYLIST WHERE songId = ?", new SongRowMapper(), songId);
            return song;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Song addSong(Song song) {
        db.update("INSERT INTO PLAYLIST (songName,lyricist,singer,musicDirector) VALUES (?,?,?,?)", song.getSongName(),
                song.getLyricist(), song.getSinger(), song.getMusicDirector());
        Song newSong = db.queryForObject(
                "SELECT * FROM PLAYLIST WHERE songName LIKE ? AND lyricist Like ? AND singer Like ? AND musicDirector Like ?",
                new SongRowMapper(), song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());
        return newSong;
    }

    public Song updateSong(int songId, Song song) {
        if (song.getSongName() != null) {
            db.update("UPDATE PLAYLIST SET songName = ?", song.getSongName());
        }
        if (song.getLyricist() != null) {
            db.update("UPDATE PLAYLIST SET lyricist = ?", song.getLyricist());
        }
        if (song.getSinger() != null) {
            db.update("UPDATE PLAYLIST SET singer = ?", song.getSinger());
        }
        if (song.getMusicDirector() != null) {
            db.update("UPDATE PLAYLIST SET musicDirector = ?", song.getMusicDirector());
        }
        return getSongById(songId);
    }

    public void deleteSong(int songId) {
        db.update("DELETE FROM PLAYLIST WHERE songId = ?", songId);
    }

}