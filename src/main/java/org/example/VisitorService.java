package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

@Getter
public class VisitorService {
    private List<Visitor> visitors;

    // Конструктор принимает InputStream
    public VisitorService(InputStream inputStream) {
        loadVisitors(inputStream);
    }

    // Метод принимает InputStream
    private void loadVisitors(InputStream inputStream) {
        Gson gson = new Gson();
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            Type visitorListType = new TypeToken<List<Visitor>>() {}.getType();
            visitors = gson.fromJson(reader, visitorListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printVisitors() {
        System.out.println("Список посетителей:");
        visitors.forEach(System.out::println);
        System.out.println("Общее количество посетителей: " + visitors.size());
    }
}


