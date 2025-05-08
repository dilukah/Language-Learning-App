package com.redigo.languagelearningapp.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhraseManager {
    private static final String DB_URL = "jdbc:sqlite:phrases.db";

    public PhraseManager() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS phrases (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                phrase TEXT NOT NULL,
                translation TEXT NOT NULL,
                category TEXT,
                masteryLevel INTEGER DEFAULT 0,
                structure TEXT
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPhrase(Phrase phrase) {
        String sql = "INSERT INTO phrases (phrase, translation, category, masteryLevel, structure) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phrase.getPhrase());
            pstmt.setString(2, phrase.getTranslation());
            pstmt.setString(3, phrase.getCategory());
            pstmt.setInt(4, phrase.getMasteryLevel());
            pstmt.setString(5, phrase.getStructure());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Phrase> getAllPhrases() {
        List<Phrase> list = new ArrayList<>();
        String sql = "SELECT * FROM phrases";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Phrase phrase = new Phrase(
                        rs.getInt("id"),
                        rs.getString("phrase"),
                        rs.getString("translation"),
                        rs.getString("category"),
                        rs.getInt("masteryLevel"),
                        rs.getString("structure")
                );
                list.add(phrase);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void deletePhrase(int id) {
        String sql = "DELETE FROM phrases WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePhrase(Phrase phrase) {
        String sql = "UPDATE phrases SET phrase = ?, translation = ?, category = ?, masteryLevel = ? , structure = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phrase.getPhrase());
            pstmt.setString(2, phrase.getTranslation());
            pstmt.setString(3, phrase.getCategory());
            pstmt.setInt(4, phrase.getMasteryLevel());
            pstmt.setString(5, phrase.getStructure());
            pstmt.setInt(6, phrase.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}