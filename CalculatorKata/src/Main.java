import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    private static final TreeMap<Character, Integer> ROMAN_TO_ARABIC = new TreeMap<>();
    private static final TreeMap<Integer, String> ARABIC_TO_ROMAN = new TreeMap<>();

    static {
        ROMAN_TO_ARABIC.put('I', 1);
        ROMAN_TO_ARABIC.put('V', 5);
        ROMAN_TO_ARABIC.put('X', 10);
        ROMAN_TO_ARABIC.put('L', 50);
        ROMAN_TO_ARABIC.put('C', 100);

        ARABIC_TO_ROMAN.put(100, "C");
        ARABIC_TO_ROMAN.put(90, "XC");
        ARABIC_TO_ROMAN.put(50, "L");
        ARABIC_TO_ROMAN.put(40, "XL");
        ARABIC_TO_ROMAN.put(10, "X");
        ARABIC_TO_ROMAN.put(9, "IX");
        ARABIC_TO_ROMAN.put(5, "V");
        ARABIC_TO_ROMAN.put(4, "IV");
        ARABIC_TO_ROMAN.put(1, "I");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение (например, 3 + 4 или III * IV):");
        String input = scanner.nextLine();
        try {
            String result = calculate(input);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static String calculate(String input) throws Exception {
        input = input.replaceAll("\\s+", "");
        char operator = findOperator(input);
        String[] operands = input.split("[+\\-*/]");
        if (operands.length != 2) {
            throw new Exception("Неверный формат выражения.");
        }

        boolean isRoman = isRoman(operands[0]) && isRoman(operands[1]);
        boolean isArabic = isArabic(operands[0]) && isArabic(operands[1]);

        if (!(isRoman || isArabic)) {
            throw new Exception("Используются разные системы счисления.");
        }

        int a = isRoman ? romanToInt(operands[0]) : Integer.parseInt(operands[0]);
        int b = isRoman ? romanToInt(operands[1]) : Integer.parseInt(operands[1]);

        if (a < 1 || a > 10 || b < 1 || b > 10) {
            throw new Exception("Числа должны быть в диапазоне от 1 до 10.");
        }

        int result;
        switch (operator) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                result = a / b;
                break;
            default:
                throw new Exception("Неизвестная операция.");
        }

        if (isRoman) {
            if (result < 1) {
                throw new Exception("Результат работы с римскими числами должен быть положительным.");
            }
            return intToRoman(result);
        } else {
            return String.valueOf(result);
        }
    }

    private static char findOperator(String input) throws Exception {
        for (char c : input.toCharArray()) {
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                return c;
            }
        }
        throw new Exception("Не найдена операция.");
    }

    private static boolean isRoman(String input) {
        return input.matches("[IVXLCDM]+");
    }

    private static boolean isArabic(String input) {
        return input.matches("\\d+");
    }

    private static int romanToInt(String roman) {
        int result = 0;
        int prevValue = 0;
        for (int i = roman.length() - 1; i >= 0; i--) {
            int currentValue = ROMAN_TO_ARABIC.get(roman.charAt(i));
            if (currentValue < prevValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }
            prevValue = currentValue;
        }
        return result;
    }

    private static String intToRoman(int number) {
        StringBuilder result = new StringBuilder();
        while (number > 0) {
            int highest = ARABIC_TO_ROMAN.floorKey(number);
            result.append(ARABIC_TO_ROMAN.get(highest));
            number -= highest;
        }
        return result.toString();
    }
}
