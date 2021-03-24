package com.codecool.database;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RadioCharts {

    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;


    public RadioCharts(String DB_URL, String DB_USER, String DB_PASSWORD) {
        this.DB_URL = DB_URL;
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;
    }


    public String getMostPlayedSong() {

        List<Song> songs = new ArrayList<>();

        String q =
            "SELECT * " +
            "FROM music_broadcast " +
            "GROUP BY artist, song " +
            "ORDER BY times_aired DESC, COUNT(song) DESC;";

        try(Connection connection = this.getConnection();
            PreparedStatement ps = connection.prepareStatement(q)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                songs.add(new Song(rs.getString(2), rs.getInt(3)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }

        return
            songs.isEmpty() ? "" : songs.get(0).getTitle();
    }


    public String getMostActiveArtist() {

        List<Artist> artists = new ArrayList<>();

        String q =
            "SELECT *" +
            "FROM music_broadcast;";

        try(Connection connection = this.getConnection();
            PreparedStatement ps = connection.prepareStatement(q)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                Artist newArtist = new Artist(rs.getString(1));
                newArtist.addSongTitle(rs.getString(1));
                artists.add(newArtist);

            }

        } catch (SQLException e) {
            return "";
        }

        return artists.isEmpty()
            ? ""
            : artists.get(0).getName();

    }


    public Connection getConnection() throws SQLException {

        Connection connection;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new SQLException();
        }

        return connection;
    }
}
