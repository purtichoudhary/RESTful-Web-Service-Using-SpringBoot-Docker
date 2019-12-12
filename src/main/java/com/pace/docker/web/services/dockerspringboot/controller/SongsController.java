package com.pace.docker.web.services.dockerspringboot.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pace.docker.web.services.dockerspringboot.entity.Song;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/docker/songs")
public class SongsController {

    ObjectMapper mapper = new ObjectMapper();
    private List<Song> songs = null;

    public SongsController() {
        getAllSongs();
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, name = "getAllSongs")
    public List<Song> getAllSongs() {
        if (songs != null) {
            return songs;
        } else {
            try {
                songs = new ArrayList<>();
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("songs.json");

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                }

                String content = stringBuilder.toString();
                JsonNode songsRootNode = mapper.readTree(content).get("Songs");
                songs = Arrays.asList(mapper.readValue(songsRootNode.toString(), Song[].class));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return songs;
        }
    }

    @RequestMapping(path = "/{artist}", method = RequestMethod.GET, name = "getSongsByArtist")
    public List<Song> getSongsByArtist(@PathVariable("artist") String artist) {
        List<Song> songsByArtist = new ArrayList<>();
        for (Song s : songs) {
            if (s.getArtist().toLowerCase().startsWith(artist.toLowerCase())) {
                songsByArtist.add(s);
            }
        }
        if (songsByArtist.size() == 0) {
            Song s = new Song();
            s.setAdditionalProperty("Error", "Artist starting with name '" + artist + "' not found");
            songsByArtist.add(s);
        }
        return songsByArtist;
    }

    @RequestMapping(path = "/{artist}/{year}", method = RequestMethod.GET, name = "getSongByArtistAndYear")
    public List<Song> getSongByArtistAndYear(@PathVariable("artist") String artist, @PathVariable("year") String year) {
        List<Song> songsByArtistAndYear = getSongsByArtist(artist);

        // If no artist with name is present
        if (songsByArtistAndYear.size() == 1 && songsByArtistAndYear.get(0).getAdditionalProperties().containsKey("Error")) {
            return songsByArtistAndYear;
        }

        songsByArtistAndYear = songsByArtistAndYear
                .stream()
                .filter(song -> song.getYear().equals(year)).collect(Collectors.toList());

        if (songsByArtistAndYear.isEmpty()) {
            Song song = new Song();
            song.setAdditionalProperty("Error", "Artist with name '" + artist + "' " +
                    "and year '" + year + "'not found");
            return Collections.singletonList(song);
        }

        return songsByArtistAndYear;
    }

    @RequestMapping(path = "/{artist}/{year}/{id}", method = RequestMethod.GET, name = "getSongsByYearIdArtist")
    public Song getSongsByYearIdArtist(@PathVariable("artist") String artist,
                                        @PathVariable("year") String year, @PathVariable("id") int id) {
        List<Song> songsByArtistAndYearAndId = getSongByArtistAndYear(artist, year);

        // If no artist with asked name and year is present
        if (songsByArtistAndYearAndId.size() == 1 && songsByArtistAndYearAndId.get(0).getAdditionalProperties().containsKey("Error")) {
            return songsByArtistAndYearAndId.get(0);
        }
        for (Song song : songsByArtistAndYearAndId) {
            if (song.getId() == id) {
                return song;
            }
        }

        Song songByArtistYearId = new Song();
        songByArtistYearId.setAdditionalProperty("Error", "Artist with name '" + artist + "' " +
                "and year '" + year + "' and id '" + id + "' not found");
        return songByArtistYearId;
    }
}