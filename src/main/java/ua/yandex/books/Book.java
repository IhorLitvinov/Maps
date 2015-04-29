package ua.yandex.books;

import ua.yandex.tuple.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Book {
    private String title;
    private List<String> authors;
    private int yearOfPublishing;
    private Topic topic;

    public Book(List<String> authors, String title,
                Topic topic, int yearOfPublishing) {
        this.authors = authors;
        this.title = title;
        this.topic = topic;
        this.yearOfPublishing = yearOfPublishing;
    }

    public static Map<String, List<Book>> groupBooksByAuthors(
            List<Book> books) {
        Map<String, List<Book>> groupedBooks = new HashMap<>();
        for (Book book : books) {
            for (String author : book.authors) {
                List<Book> booksByAuthor = groupedBooks.get(author);
                if (booksByAuthor == null) {
                    booksByAuthor = new ArrayList<>();
                    groupedBooks.put(author, booksByAuthor);
                }
                booksByAuthor.add(book);
            }
        }
        return  groupedBooks;
    }

    public static Map<Integer, Set<String>> findTopAuthorsPerEachYear(
            List<Book> books) {
        Map<Integer, Set<String>> topAuthorsPerYear = new HashMap<>();
        Map<Integer, Map<String, Integer>> allAuthorsFrequencyPerYear =
                new HashMap<>();
        for (Book book : books) {
            int booksYear = book.yearOfPublishing;
            Map<String, Integer> authorsFrequencies =
                    allAuthorsFrequencyPerYear.get(booksYear);
            if (authorsFrequencies == null) {
                authorsFrequencies = new HashMap<>();
                allAuthorsFrequencyPerYear.put(booksYear, authorsFrequencies);
            }
            for (int authorIndex = 0;
                 authorIndex < book.authors.size(); authorIndex++) {
                String author = book.authors.get(authorIndex);
                int frequency;
                if (authorsFrequencies.get(author) != null) {
                    frequency = authorsFrequencies.get(author) + 1;

                } else {
                    frequency = 1;
                }
                authorsFrequencies.put(author, frequency);
            }
        }
        Set<Integer> years = allAuthorsFrequencyPerYear.keySet();
        for (int year : years) {
            Map<String, Integer> authorsFrequencies =
                    allAuthorsFrequencyPerYear.get(year);
            topAuthorsPerYear.put(year, findTopAuthors(authorsFrequencies));
        }
        return topAuthorsPerYear;
    }

    private static Set<String> findTopAuthors(
            Map<String, Integer> authorsFrequencies) {
        Set<String> topAuthors = new HashSet<>();
        int maxFrequency = 0;
        for (String author : authorsFrequencies.keySet()) {
            int frequency = authorsFrequencies.get(author);
            if (frequency == maxFrequency) {
                topAuthors.add(author);
            } else if (frequency > maxFrequency) {
                topAuthors.clear();
                topAuthors.add(author);
                maxFrequency = frequency;
            }
        }
        return topAuthors;
    }

    public static Set<Tuple<Topic,
            List<String>>> findTopicsWithTheMostNumberOfBooks(
                List<Book> books) {
        Map<Topic, List<String>> booksByTopic = new HashMap<>();
        for (Book book : books) {
            List<String> booksNames = booksByTopic.get(book.topic);
            if (booksNames == null) {
                booksNames = new ArrayList<>();
                booksByTopic.put(book.topic, booksNames);
            }
            booksNames.add(book.title);
        }
        int maxBooksNumber = 0;
        Set<Tuple<Topic, List<String>>> topTopics = new HashSet<>();
        for (Topic topic : booksByTopic.keySet()) {
            List<String> booksNames = booksByTopic.get(topic);
            if (booksNames.size() > maxBooksNumber) {
                topTopics.clear();
                Tuple<Topic, List<String>> nextTuple = new Tuple(topic, booksNames);
                topTopics.add(nextTuple);
                maxBooksNumber = booksNames.size();
            } else if (booksNames.size() == maxBooksNumber) {
                Tuple<Topic, List<String>> nextTuple = new Tuple(topic, booksNames);
                topTopics.add(nextTuple);
            }
        }
        return topTopics;
    }

    @Override
    public String toString() {
        return title;
    }
}
