import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<LogEntry> logEntriesList = new ArrayList<>(); //лист объектов access логов
        Statistics statistics = new Statistics();//инициируем объект для сбора статистики

        System.out.print("\n\nВведите полный путь к файлу:");

        //создаем объект сканера и передаем путь к файлу
        String path = new Scanner(System.in).nextLine();
        File file = new File(path);

        //булеан ключи для проверки, что файл существует и не является папкой
        boolean fileExists = file.exists();
        boolean isDirectory = file.isDirectory();

        if (!fileExists || isDirectory) {
            System.out.println("Указанный файл не существует или указанный путь является путём к папке");
        } else {
            System.out.print("\nПуть к файлу указан верно. Ожидайте подсчета статистических данных.");

            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);

            String line;
            int count = 0;

            //построчно считываем лог
            try {
                while ((line = reader.readLine()) != null) {
                    int length = line.length();

                    //проверка, что не превышаем 1024 символов
                    if (length > 1024) {
                        throw new LineTooLongException("ERROR! Обнаружена строка в файле " + file + " с длинной символов более 1024 (длинна строки " + length + ")");
                    }
                    try {
                        LogEntry logEntry = new LogEntry(line);
                        logEntriesList.add(logEntry);
                        statistics.addEntry(logEntry);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR! Неизвестная ошибка при парсинге строки: " + e.getMessage());
                    } catch (URISyntaxException e) {
                        System.out.println("ERROR! В refer некорректный URL");
                    }
                }
            } catch (LineTooLongException e) {
                System.out.println("\n\nERROR! " + e.getMessage());
            }
            System.out.println("\nКоличество посещений пользователей за каждую секунду: " + statistics.getCountVisitsPerSecond());
            System.out.println("\nВсе домены из рефер: " + statistics.getInfoAllRefer());
            System.out.println(("\nIP пользователя с самым большим количеством запрсов: " + statistics.getMaxUserTraffic()));
        }
    }
}
// /home/anton/Рабочий стол/access.log