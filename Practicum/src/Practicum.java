import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Practicum {

    public static Optional<String> find(String text, List<Optional<String>> maybeWords) {

        String regex = ".*";
        for (Optional<String> maybeWord : maybeWords) {
            regex = regex + maybeWord.orElse(" .* ");
        }
        regex = regex + ".*";

        System.out.println(regex);
        String textToLowerCase = text.toLowerCase();
        System.out.println(textToLowerCase);
        boolean is = textToLowerCase.matches(regex);
        return Optional.empty();
    }

    public static void findInBooks(List<String> books, List<Optional<String>> maybeWords) {
        //напишите стримы поиска фрагмета и вывода результата
        for (String book : books) {
            find(book, maybeWords).ifPresent(System.out::println);
        }
    }

    public static void main(String[] args) {
        List<String> books = Arrays.asList(
                "Фараон желает доброе утро! Доброе утро всем читающим эти строки!",
                "Солнце печёт людей; бабушка печёт пирожки; печь греет дом",
                "Наскальная живопись может приглянуться всем, особенно может всем запомниться"
        );


        findInBooks(
                books,
                List.of(Optional.of("доброе"), Optional.empty(), Optional.of("всем"))
        ); // "доброе утро всем"

        findInBooks(
                books,
                List.of(Optional.of("доброе"), Optional.empty(), Optional.empty(), Optional.of("всем"))
        ); // empty

        findInBooks(
                books,
                List.of(Optional.empty(), Optional.of("печёт"), Optional.of("пирожки"))
        ); // "бабушка печёт пирожки"

        findInBooks(
                books,
                List.of(Optional.of("может"), Optional.of("всем"), Optional.empty())
        ); // "может всем запомниться"
    }
}