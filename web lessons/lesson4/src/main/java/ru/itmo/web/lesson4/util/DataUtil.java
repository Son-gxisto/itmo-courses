package ru.itmo.web.lesson4.util;

import ru.itmo.web.lesson4.model.Post;
import ru.itmo.web.lesson4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", Color.RED),
            new User(6, "pashka", "Pavel Mavrin", Color.GREEN),
            new User(9, "geranazarov555", "Georgiy Nazarov", Color.BLUE),
            new User(11, "tourist", "Gennady Korotkevich", Color.RED)
    );
    public static enum Color {
        RED, GREEN, BLUE
    }
    private static final List<Post> POSTS = Arrays.asList(
            new Post(2, 1, "web homework task №4", "Поддержите новый объект предметной области Post. У Post должно быть четыре поля id (long), title (String), text (String) и user_id (long). Создайте в системе по аналогии с User серию постов с разумными содержаниями (модифицируйте DataUtil). Используя вашу разметку из второго ДЗ отобразите на главной список всех постов в обратном порядке (от последнего к первому). Если длина text превышает 250 символов, то обрезайте его и используйте символ многоточия в конце (сокращайте длинные тексты). Страницу со списком пользователей перенесите в отдельную страницу /users. Измените её разметку так, чтобы использовать вёрстку таблицы из второго ДЗ для их отображения. " +
                    "Добавьте в меню пункт USERS.\n"),
            new Post(4, 1, "web homework #5", "Добавьте в профиль пользователя количество его постов ссылкой на новую страницу /posts?user_id=?,"),
            new Post(6, 11, "рандомный текст", "тскет йынмоднар"),
            new Post(13, 9, "post#4", "Добавьте пользователю свойство color (как цвет на Codeforces), которое должно быть enum с одним из значений: {RED, GREEN, BLUE}. Измените userlink, чтобы он отображал\n" +
                    "пользователей по окрашенному хэндлу (прям как на Codeforces). То есть уберите подчеркивание, поменяйте чуток шрифт, навесьте правильный цвет в зависимости от color. Старый режим тоже сохраните, сделав дополнительный" +
                    " параметр у userlink (назовите его nameOnly).\n")
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);
        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }
}
