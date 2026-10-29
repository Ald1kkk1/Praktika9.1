package kz.university.db;

public class Main {
    public static void main(String[] args) {
        Praktika9 dao = new Praktika9("jdbc:sqlite:books.db");
        dao.createTable();
        dao.deleteAll();

        dao.save(new Book(0, "Effective Java", "978-0134685991", 1500000));
        dao.save(new Book(0, "Clean Code", "978-0132350884", 1200000));
        dao.save(new Book(0, "Spring in Action", "978-1617297571", 1350000));

        dao.findByIsbn("978-0132350884")
                .ifPresentOrElse(
                        b -> System.out.println("Найдена книга: " + b),
                        () -> System.out.println("Книга не найдена"));
        System.out.println(dao.findByIsbn("000-0000000000").isPresent()
                ? "Неожиданно найдена" : "Книга с ISBN 000-0000000000 отсутствует (Optional.empty)");
    }
}