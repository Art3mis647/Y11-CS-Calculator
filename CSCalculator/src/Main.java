import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.*;
public class Main {
    static String exp;

    public static void main(String[] args) {

        System.out.println("Calculator");
        System.out.println("=".repeat(100) + "\n");
        System.out.println("Supports [0-9], [()], [+-*/]");
        System.out.println("Type 'exit' to terminate the program");
        System.out.println("Type 'help' to display the help manual");
        System.out.println("Note: Calculator does not support decimal operations.");
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Input expression: \n> ");
            exp = input.nextLine();
            if (exp.toLowerCase().contains("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            if (exp.toLowerCase().contains("help")) {
                System.out.println("Calculator");
                System.out.println("=".repeat(100) + "\n");
                System.out.println("Supports [0-9], [()], [+-*/], [^]");
                System.out.println("Type 'exit' to terminate the program");
                System.out.println("Type 'help' to display the help manual");
                System.out.println("Note: Calculator does not support decimal operations.");
                continue;
            }
            if (!isValidExpression(exp)) {
                System.out.println("Error: Please enter a valid expression.\n");
                continue;
            }
            if (!hasMatchedBrackets(exp)) {
                System.out.println("Error: Expression has unmatched brackets.\n");
                continue;
            }
            System.out.println(evaluateRPN(infixToRPN(exp)));
        }
    }

    public static boolean isValidExpression(String exp) {
        String valid = "0123456789+-*/().^";
        for (int i = 0; i < exp.length(); i++) {
            if (!valid.contains(exp.charAt(i) + "")) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasMatchedBrackets(String exp) {
        String ex = exp.replaceAll("[^()]", "");
        while (ex.length() > 2) {
            ex = ex.replaceAll("\\(\\)", "");
            if (!ex.contains("()")) {
                return false;
            }
        }
        return ex.equals("()") || ex.equals("");
    }


    public static int evaluateRPN(String expression) {
        String[] tokens = expression.split(" ");
        Stack<Integer> stack = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                stack.push(Integer.parseInt(token));
            } else {
                int num2 = stack.pop();
                int num1 = stack.pop();
                int result = performOperation(token, num1, num2);
                stack.push(result);
            }
        }

        return stack.pop();
    }

    private static final Map<Character, Integer> precedence = new HashMap<>();

    static {
        precedence.put('(', 0);
        precedence.put(')', 0);
        precedence.put('+', 1);
        precedence.put('-', 1);
        precedence.put('*', 2);
        precedence.put('/', 2);
        precedence.put('^', 3);
    }

    public static String infixToRPN(String infixExpression) {
        StringBuilder rpnExpression = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        StringBuilder currentNumber = new StringBuilder();
        for (char c : infixExpression.toCharArray()) {
            if (Character.isWhitespace(c)) {
                continue;
            } else if (Character.isDigit(c)) {
                currentNumber.append(c);
            } else {
                if (currentNumber.length() > 0) {
                    rpnExpression.append(currentNumber.toString()).append(" ");
                    currentNumber.setLength(0);
                }

                if (c == '(') {
                    operatorStack.push(c);
                } else if (c == ')') {
                    while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                        rpnExpression.append(operatorStack.pop()).append(" ");
                    }
                    if (!operatorStack.isEmpty() && operatorStack.peek() == '(') {
                        operatorStack.pop();
                    }
                } else if (precedence.containsKey(c)) {
                    while (!operatorStack.isEmpty() && precedence.get(c) <= precedence.get(operatorStack.peek())) {
                        rpnExpression.append(operatorStack.pop()).append(" ");
                    }
                    operatorStack.push(c);
                }
            }
        }

        if (currentNumber.length() > 0) {
            rpnExpression.append(currentNumber.toString()).append(" ");
        }

        while (!operatorStack.isEmpty()) {
            rpnExpression.append(operatorStack.pop()).append(" ");
        }

        return rpnExpression.toString().trim();
    }


    public static int performOperation(String operator, int num1, int num2) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
