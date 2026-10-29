package kz.university.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Praktika9 {
    private final String dbUrl;
    public Praktika9(String dbUrl) {
        this.dbUrl = dbUrl;
    }
    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    isbn TEXT NOT NULL UNIQUE,
                    price_grams INTEGER NOT NULL
                )""";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы: " + e.getMessage(), e);
        }
    }
    public void save(Book book) {
        String sql = "INSERT INTO books (title, isbn, price_grams) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.title());
            ps.setString(2, book.isbn());
            ps.setInt(3, book.priceGrams());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения книги: " + e.getMessage(), e);
        }
    }
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "SELECT id, title, isbn, price_grams FROM books WHERE isbn = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Book(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("isbn"),
                            rs.getInt("price_grams")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска книги: " + e.getMessage(), e);
        }
        return Optional.empty();
    }
    public void deleteAll() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM books");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}