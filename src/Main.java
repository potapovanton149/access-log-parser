import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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
            System.out.print("\nПуть к файлу указан верно.");

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
                    }
                }
            } catch (LineTooLongException e) {
                System.out.println("\n\nERROR! " + e.getMessage());
            }
            System.out.printf("\n\nОбщий объем часового трафика из файла: %s", statistics.getTrafficRate());
            System.out.println("\nСтитистика по операционным система: " + statistics.getInfoCountsOS());
            System.out.println("\nСтатистика по ОС в долях " + statistics.getInfoStatsOS());
            //System.out.println("\n Список всех существующих страниц сайта " + statistics.getSitePages());
        }
    }
}
// /home/anton/Рабочий стол/access.log