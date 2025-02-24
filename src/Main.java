import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int a, b;

        System.out.println("Введите первое число:");
        a = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        b = new Scanner(System.in).nextInt();

        System.out.println(
                "Сумма чисел: " + (a + b) + "\n"
                        + "Разность чисел: " + (a - b) + "\n"
                        + "Произведение чисел: " + (a * b) + "\n"
                        + "Частное чисел: " + (a / b) + "\n"
        );
    }
}
