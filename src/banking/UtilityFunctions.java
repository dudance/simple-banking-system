package banking;


import java.util.Arrays;
import java.util.Random;

public class UtilityFunctions {

    static boolean isLuhnAlgCorrect(String number) {
        int size = number.length();
        int sum = 0;
        int[] array = new int[size];
        for (int i = 0; i < number.length(); i++) {
            array[i] = Integer.parseInt(String.valueOf(number.charAt(i)));
        }
        for (int i = 0; i < size - 1; i++) {
            if ((i + 1) % 2 == 1)
                array[i] *= 2;

            if (array[i] > 9)
                array[i] -= 9;
        }
        for (int i = 0; i < size; i++) {
            sum += array[i];
        }
        return sum % 10 == 0;
    }

    static String generateCardNumber() {
        Random random = new Random();
        int[] cardNumber = new int[16];
        Arrays.fill(cardNumber, 0);
        cardNumber[0] = 4;
        for (int i = 6; i < 15; i++) {
            cardNumber[i] = random.nextInt(10);
        }
        String number = Arrays.toString(cardNumber)
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "");
        for (int i = 1; i < 10; i++) {
            if (isLuhnAlgCorrect(number))
                break;
            else {
                number = number.substring(0, 15) + i;
            }
        }
        return number;
    }

    static String generatePin() {
        Random random = new Random();
        int number;
        int[] pin = new int[4];

        for (int i = 0; i < 4; i++) {
            number = random.nextInt(10);
            pin[i] = number;
        }

        return Arrays.toString(pin)
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "");
    }
}
