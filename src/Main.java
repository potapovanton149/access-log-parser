import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        int numberOfFiles = 0;
        ArrayList<LogEntry> requestsList = new ArrayList<>();

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

                try {
                    while ((line = reader.readLine()) != null) {
                        int length = line.length();

                        if (length > 1024) {
                            throw new LineTooLongException("Обнаружена строка " +
                                    "в файле " + file + " с длинной символов более 1024 (длинна" +
                                    " строки " + length + ")");
                        }
                        requestsList.add(new LogEntry(line));
                    }
                } catch (LineTooLongException e) {
                    System.out.println("\n\nВнимание! Событие с уровнем ERROR: " + e.getMessage());
                }

                int countYandex = 0;
                int countGoogle = 0;

                for (LogEntry logEntry : requestsList) {
                    if (logEntry.getUserAgent().equals("-")) {
                        continue;
                    }

                    String userAgent = logEntry.getUserAgent();
                    int index;

                    if ((index = userAgent.lastIndexOf("YandexBot")) != -1) {
                        countYandex++;
                        continue;
                    }

                    if ((index = userAgent.lastIndexOf("Googlebot")) != -1) {
                        countGoogle++;
                    }

                }
                System.out.printf("\nВ log файле количество запросов от YandexBot равна %s, от Googlebot равна %s", countYandex, countGoogle);
            }
        }
    }
}
// /home/anton/Рабочий стол/access.log