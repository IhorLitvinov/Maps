package ua.yandex.books;

import org.junit.Assert;
import org.junit.Test;
import ua.yandex.tuple.Tuple;

import java.util.*;

public class BookTest {

    Book bookOne = new Book(Arrays.asList("Толстой"), "Война и мир", Topic.CLASSICS, 1869);
    Book bookTwo = new Book(Arrays.asList("Достоевский"), "Идиотъ", Topic.CLASSICS, 1869);
    Book bookThree = new Book(Arrays.asList("Достоевский"), "Преступление и наказание", Topic.CLASSICS, 1866);
    Book bookFour = new Book(Arrays.asList("Маркс"), "Капитал", Topic.FICTION, 1867);
    Book bookFive = new Book(Arrays.asList("Ницше"), "Так говорил Заратустра", Topic.FANTASY, 1885);
    Book bookSix = new Book(Arrays.asList("Eckel"), "Thinking in Java", Topic.COMPUTING, 2006);
    Book bookSeven = new Book(Arrays.asList("Брайан Керниган", "Деннис Ритчи"), "Язык программирования C", Topic.COMPUTING, 2006);
    Book bookEight = new Book(Arrays.asList("Some author1", "Some author2"), "Some book1", Topic.FICTION, 1869);
    Book bookNine = new Book(Arrays.asList("Some author1", "Some author2"), "Some book2", Topic.COMPUTING, 1869);
    Book bookNullAuthor = new Book((List)new ArrayList<>(), "null", null, 0);
//    Book nullBook = null;

    @Test
    public void testGroupBooksByAuthors() throws Exception {
        List<Book> books = new ArrayList<>();
        books.addAll(Arrays.asList(bookOne, bookTwo, bookThree, bookNullAuthor));
        Map<String, List<Book>> grouped = Book.groupBooksByAuthors(books);
        Assert.assertTrue(grouped.get("Толстой").containsAll(Arrays.asList(bookOne)));
        Assert.assertTrue(grouped.get("Достоевский").containsAll(Arrays.asList(bookTwo, bookThree)));
    }

    @Test
    public void testGroupBooksByAuthorsTwoAuthors() throws Exception {
        List<Book> books = new ArrayList<>();
        books.addAll(Arrays.asList(bookFour, bookFive, bookSeven));
        Map<String, List<Book>> grouped = Book.groupBooksByAuthors(books);
        Assert.assertTrue(grouped.get("Брайан Керниган").containsAll(Arrays.asList(bookSeven)));
        Assert.assertTrue(grouped.get("Деннис Ритчи").containsAll(Arrays.asList(bookSeven)));
        Assert.assertTrue(grouped.get("Ницше").containsAll(Arrays.asList(bookFive)));
    }

    @Test
    public void testFindTopAuthorsPerEachYear() throws Exception {
        List<Book> books = new ArrayList<>();
        books.addAll(Arrays.asList(bookOne, bookTwo, bookEight, bookNine));
        Map<Integer, Set<String>> grouped = Book.findTopAuthorsPerEachYear(books);
        Assert.assertTrue(grouped.get(1869).containsAll(Arrays.asList("Some author1", "Some author2")));
    }

    @Test
    public void testFindTopicsWithTheMostNumberOfBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        books.addAll(Arrays.asList(bookOne, bookTwo, bookThree, bookFive, bookSix, bookSeven, bookEight, bookNine));
        Set<Tuple<Topic, List<String>>> topTopics = Book.findTopicsWithTheMostNumberOfBooks(books);
        Map<Topic, List<String>> topTopicsMap = new HashMap<>();
        System.out.println(topTopics);
        for (Tuple<Topic, List<String>> tuple : topTopics) {
            topTopicsMap.put(tuple.getFirstObject(), tuple.getSecondObject());
        }
        Assert.assertTrue(topTopicsMap.get(Topic.CLASSICS).containsAll(Arrays.asList("Война и мир", "Преступление и наказание", "Идиотъ")));
        Assert.assertTrue(topTopicsMap.get(Topic.COMPUTING).containsAll(Arrays.asList("Язык программирования C", "Some book2", "Thinking in Java")));
    }
}