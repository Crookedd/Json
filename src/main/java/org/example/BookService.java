package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookService {
    private final List<Book> books;

    public BookService(List<Book> books) {
        this.books = books;
    }

    // Задание 2: Получить список уникальных книг в избранном
    public Set<Book> getUniqueFavoriteBooks(List<Visitor> visitors) {
        return visitors.stream()
                .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                .collect(Collectors.toSet());
    }

    // Задание 3: Сортировать книги по году издания
    public List<Book> getBooksSortedByYear() {
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getPublishingYear))
                .collect(Collectors.toList());
    }

    // Задание 4: Проверить, есть ли у кого-то в избранном книга автора "Jane Austen"
    public boolean hasVisitorsWithBookByAuthor(List<Visitor> visitors, String author) {
        return visitors.stream()
                .anyMatch(visitor -> visitor.getFavoriteBooks().stream()
                        .anyMatch(book -> book.getAuthor().equalsIgnoreCase(author)));
    }

    // Задание 5: Получить максимальное количество книг в избранном у посетителей
    public int getMaxFavoriteBooksCount(List<Visitor> visitors) {
        return visitors.stream()
                .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                .max()
                .orElse(0); // Если нет посетителей, возвращаем 0
    }

    // Задание 6: Группировка посетителей и создание SMS-сообщений
    public List<SmsMessage> generateSmsMessages(List<Visitor> visitors) {
        int average = (int) Math.ceil(visitors.stream().mapToInt(visitor -> visitor.getFavoriteBooks().size()).average().orElse(0));

        return visitors.stream()
                .map(visitor -> {
                    int favoriteCount = visitor.getFavoriteBooks().size();
                    String message;

                    if (favoriteCount > average) {
                        message = "you are a bookworm";
                    } else if (favoriteCount < average) {
                        message = "read more";
                    } else {
                        message = "fine";
                    }

                    return new SmsMessage(visitor.getPhone(), message);
                })
                .collect(Collectors.toList());
    }
}
