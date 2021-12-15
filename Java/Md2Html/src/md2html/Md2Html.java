package md2html;

import java.io.*;
import java.lang.StringBuilder;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Md2Html {
    private static final Map<String, String> m2hOpenTag = Map.of(
            "*", "em",
            "_", "em",
            "__", "strong",
            "**", "strong",
            "`", "code",
            "--", "s",
            "++", "u"
    );

    private static final Map<String, String> m2hSpecialSymbols = Map.of(
            "<", "&lt;",
            ">", "&gt;",
            "&", "&amp;"
    );

    private final static int TAG_MAX_LENGTH = 2;
    private final static int IS_OPEN_TAG = -2;

    public static void initOpenTagList(Map<String, Integer> t) {
        for (Map.Entry<String, String> elem : m2hOpenTag.entrySet()) {
            t.put(elem.getKey(), IS_OPEN_TAG);
        }
    }

    public static int getHeaderNum(String t) {
        int num = 0;
        char c = t.charAt(num);
        while (c == '#' && num < t.length() - 1) {
            num++;
            c = t.charAt(num);
        }
        if (c == ' ') {
            return num;
        }
        return 0;
    }

    public static String getHeaderNumOrParagraph(StringBuilder t, int num) {
        return num != 0 && num != t.length() && t.charAt(num) == ' ' ? "h" + num : "p";
    }

    public static String getOpenTag(String t) {
        return "<" + t + ">";
    }

    public static String getClosedTag(String t) {
        return "</" + t + ">";
    }

    public static boolean isVoid(String s) {
        return s == null || s.isEmpty();
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(args[0]),
                                "utf8"
                        )
                );
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(args[1]),
                                "utf8"
                        )
                )
        ) {
            String line = in.readLine();
            while (line != null) {
                //пропуск пустых строк
                while (line != null && line.isEmpty()) {
                    line = in.readLine();
                }

                if (line == null) {
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                while (!isVoid(line)) { //чтение абзаца
                    buf.append(line);
                    line = in.readLine();
                    if (!isVoid(line)) {
                        buf.append('\n');
                    }
                }

                int i = getHeaderNum(buf.toString()); //i - отступ слева(число решеток, если абзац - заголовок, иначе 0)
                String h = getHeaderNumOrParagraph(buf, i); //Записали заголовок, добавим его в начале и позже в конце буфера
                List<StringBuilder> inHtml = new ArrayList<>(); // буфер для перевода строки из markdown в html
                inHtml.add(new StringBuilder(getOpenTag(h))); //добавили тег заголовка\параграфа в начало
                Map<String, Integer> OpenTagNumList = new HashMap<>();//Лист элементов с номерами открывающих (или одиночных открывающих) тегов
                initOpenTagList(OpenTagNumList);
                if (!h.equals("p")) { //если абзац-заголовок, после # идет лишний пробел
                    i++; //(легко заменить на пропуск всех пробелов, если понадобится)
                }
                while (i < buf.length()) {
                    if (i < buf.length() - 1 && buf.charAt(i) == '\\') { // несколько экранированных символов подряд \*\*\*
                        i++;
                        inHtml.get(inHtml.size() - 1).append(buf.charAt(i));
                        i++;
                        continue;
                    }
                    String tag = "";
                    boolean isTag = false;
                    for (int j = TAG_MAX_LENGTH; j > 0; j--) {
                        if (i + j > buf.length()) {
                            continue;
                        }
                        tag = buf.substring(i, i + j);
                        if (m2hSpecialSymbols.containsKey(tag)) { //Проверка, что это особый символ
                            inHtml.get(inHtml.size() - 1).append(m2hSpecialSymbols.get(tag));
                            isTag = true;
                            break;
                        } else {
                            int k = OpenTagNumList.getOrDefault(tag, -1); //Проверка, что это тег
                            if (k == -1) {
                                continue;
                            } else if (k == IS_OPEN_TAG) { //Если к = IS_OPEN_TAG, то мы встретили открывающий тег, запоминаем его позицию
                                OpenTagNumList.put(tag, inHtml.size() - 1);
                                inHtml.add(new StringBuilder());
                            } else { //иначе k - позиция открывающего тега, запомненного ранее
                                String time = m2hOpenTag.get(tag); //Добавим открывающий и закрывающий тег
                                inHtml.get(k).append(getOpenTag(time));
                                inHtml.get(inHtml.size() - 1).append(getClosedTag(time));
                                OpenTagNumList.put(tag, IS_OPEN_TAG);
                            }
                            isTag = true;
                            break;
                        }
                    }
                    if (!isTag) { //Если не нашли тег, запишем текущий символ
                        inHtml.get(inHtml.size() - 1).append(buf.charAt(i));
                        i++;
                    } else { //иначе прибавим длину найденого тега
                        i += tag.length();
                    }
                }
                inHtml.get(inHtml.size() - 1).append(getClosedTag(h)); //Заполним одиночные открывающие теги
                for (Map.Entry<String, Integer> elem : OpenTagNumList.entrySet()) {
                    if (elem.getValue() != IS_OPEN_TAG) {
                        inHtml.get(elem.getValue()).append(elem.getKey());
                    }
                }
                for (StringBuilder stringBuilder : inHtml) { //запишем все из буфера в файл
                    out.write(stringBuilder.toString());
                }
                out.newLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input not found" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println("Encoding exception" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Input or output error" + e.getMessage());
        }
    }
}