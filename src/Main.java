import java.io.*;
import java.util.Scanner;
import java.util.SortedMap;

public class Main {
    public static void main(String[] args) throws IOException{
        int numberOfFiles = 0;

        while (true) {
            System.out.print("Введите полный путь к файлу:");

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
                int countLine = 0;
                int bigLengthLine = reader.readLine().length();
                int smallLengthLine = reader.readLine().length();


               try {
                   while ((line = reader.readLine()) != null) {
                       int length = line.length();

                       if (length > 1024){
                           throw new LineTooLongException("Обнаружена строка " +
                                   "в файле " + file + " с длинной символов более 1024 (длинна" +
                                   " строки "+ length +")");
                       }

                       if (length > bigLengthLine){
                           bigLengthLine = length;
                       }

                       if (length < smallLengthLine){
                           smallLengthLine = length;
                       }
                       countLine++;
                   }
               } catch (LineTooLongException e){
                   System.out.println("\n\nВнимание! Событие с уровнем ERROR: " + e.getMessage());
               }
                System.out.println("\nОбщее количество строк в файле " + countLine +
                        "\nДлинна самой длинной строки " + bigLengthLine +
                        "\nДлинна самой короткой строки " + smallLengthLine + "\n");
            }
        }
    }
}