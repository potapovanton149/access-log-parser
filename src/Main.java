import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        int numberOfFiles = 0;
        ArrayList<LogEntry> entriesList = new ArrayList<>();
        Statistics statistics = new Statistics();

        while (true) {
            System.out.print("\n\nВведите полный путь к файлу:");

            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists || isDirectory) {
                System.out.println("Указанный файл не существует или указанный путь является путём к папке");
            } else {
                numberOfFiles++;
                System.out.print("\nПуть к файлу указан верно. Это файл номер №" + numberOfFiles + ".");

                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                String line;
                int count = 0;

                try {
                    while ((line = reader.readLine()) != null) {
                        int length = line.length();

                        if (length > 1024) {
                            throw new LineTooLongException("ERROR! Обнаружена строка " +
                                    "в файле " + file + " с длинной символов более 1024 (длинна" +
                                    " строки " + length + ")");
                        }
                        try {
                            LogEntry entry = new LogEntry(line);
                            entriesList.add(entry);
                        } catch (IllegalArgumentException e) {
                            System.out.println("ERROR! Неизвестная ошибка при парсинге строки: " + e.getMessage());
                        }
                        LogEntry logEntry = new LogEntry(line);
                        entriesList.add(logEntry);
                        statistics.addEntry(logEntry);
                    }
                } catch (LineTooLongException e) {
                    System.out.println("\n\nERROR! " + e.getMessage());
                }
                System.out.printf("\n\nОбщий объем часового трафика из файла: %s", statistics.getTrafficRate());
                System.out.println("\nСтитистика по операционным система: " + statistics.getCountsOS());
                System.out.println("\nСтатистика по ОС в долях " + statistics.getStatsOS());
                //System.out.println("\n Список всех существующих страниц сайта " + statistics.getSitePages());
            }
        }
    }
}
// /home/anton/Рабочий стол/access.log