package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        // Получаем InputStream для books.json
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("books.json");
        if (inputStream == null) {
            throw new FileNotFoundException("Файл books.json не найден в ресурсах!");
        }

        // Создаем VisitorService с InputStream
        VisitorService visitorService = new VisitorService(inputStream);

        // Печатаем список посетителей
        visitorService.printVisitors();
        List<Visitor> visitors = loadVisitors(); // Загружаем посетителей из файла
        BookService bookService = new BookService(getAllFavoriteBooks(visitors));

        System.out.println("Введите номер задания (1-6):");
        int taskNumber = scanner.nextInt();

        switch (taskNumber) {
            case 1:
                visitorService.printVisitors();
                break;
            case 2:
                Set<Book> uniqueFavoriteBooks = bookService.getUniqueFavoriteBooks(visitors);
                System.out.println("Уникальные книги в избранном:");
                uniqueFavoriteBooks.forEach(book -> System.out.println(book.getName()));
                System.out.println("Количество уникальных книг: " + uniqueFavoriteBooks.size());
                break;
            case 3:
                List<Book> sortedBooks = bookService.getBooksSortedByYear();
                System.out.println("Книги, отсортированные по году издания:");
                sortedBooks.forEach(book -> System.out.println(book.getName() + ", Год издания: " + book.getPublishingYear()));
                break;
            case 4:
                boolean hasVisitorsWithJaneAusten = bookService.hasVisitorsWithBookByAuthor(visitors, "Jane Austen");
                if (!hasVisitorsWithJaneAusten) {
                    System.out.println("Нет посетителей с книгами автора 'Jane Austen'.");
                } else {
                    System.out.println("Посетители с книгами автора 'Jane Austen':");
                    for (Visitor visitor : visitors) {
                        accept(visitor);
                    }
                }
                break;
            case 5:
                int maxFavoriteBooksCount = bookService.getMaxFavoriteBooksCount(visitors);
                System.out.println("Максимальное количество книг в избранном у посетителей: " + maxFavoriteBooksCount);
                break;
            case 6:
                List<SmsMessage> smsMessages = bookService.generateSmsMessages(visitors);
                System.out.println("SMS сообщения:");
                smsMessages.forEach(sms -> System.out.println("На номер " + sms.getPhoneNumber() + ": " + sms.getMessage()));
                break;
            default:
                System.out.println("Неверный номер задания.");
        }
    }

    static List<Visitor> loadVisitors() {
        List<Visitor> visitors = new ArrayList<>();
        Gson gson = new Gson();
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("books.json")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Файл не найден!");
            }
            Type visitorListType = new TypeToken<List<Visitor>>() {}.getType();
            visitors = gson.fromJson(new InputStreamReader(inputStream), visitorListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return visitors; // Возвращаем загруженных посетителей
    }

    static List<Book> getAllFavoriteBooks(List<Visitor> visitors) {
        List<Book> allFavoriteBooks = new ArrayList<>();
        for (Visitor visitor : visitors) {
            allFavoriteBooks.addAll(visitor.getFavoriteBooks());
        }
        return allFavoriteBooks; // Возвращаем все любимые книги
    }

    private static void accept(Visitor visitor) {
        visitor.getFavoriteBooks().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase("Jane Austen"))
                .forEach(book -> System.out.println(visitor.getName() + ": " + book.getName()));
    }
}