import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Calculator");
            System.out.println("=".repeat(100) + "\n");
            System.out.println("Supports [0-9], [()], [+-*/]");
            System.out.println("Type 'exit' to terminate the program");
            System.out.println("Type 'help' to display the help manual");
            System.out.println("Note: Calculator does not support decimal operations.");

            while (true) {
                System.out.print("Input expression: \n> ");
                String expression = input.nextLine();
                if (expression.toLowerCase().contains("exit")) {
                    System.out.println("Goodbye!");
                    break;
                }
                if (expression.toLowerCase().contains("help")) {
                    displayHelpManual();
                    continue;
                }
                if (expression.equals("")){
                    System.out.println("Error: Empty Expression.\n");
                    continue;
                }
                if (!isValidExpression(expression)) {
                    System.out.println("Error: Please enter a valid expression.\n");
                    continue;
                }
                if (!hasMatchedBrackets(expression)) {
                    System.out.println("Error: Expression has unmatched brackets.\n");
                    continue;
                }
                System.out.println(evaluateExpression(expression));
            }
        }
    }

    public static void displayHelpManual() {
        System.out.println("Calculator");
        System.out.println("=".repeat(100) + "\n");
        System.out.println("Supports [0-9], [()], [+-*/], [^]");
        System.out.println("Type 'exit' to terminate the program");
        System.out.println("Type 'help' to display the help manual");
        System.out.println("Note: Calculator does not support decimal operations.");
    }

    public static boolean isValidExpression(String expression) {
        String validChars = "0123456789+-*/().^";
        for (char c : expression.toCharArray()) {
            if (!validChars.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasMatchedBrackets(String expression) {
        String brackets = expression.replaceAll("[^()]", "");
        while (brackets.length() > 2) {
            brackets = brackets.replaceAll("\\(\\)", "");
            if (!brackets.contains("()")) {
                return false;
            }
        }
        return brackets.equals("()") || brackets.isEmpty();
    }

    public static int evaluateExpression(String expression) {
        String rpnExpression = infixToRPN(expression);
        String[] tokens = rpnExpression.split(" ");
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
                    rpnExpression.append(currentNumber).append(" ");
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
            rpnExpression.append(currentNumber).append(" ");
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
                if (num2 == 0) {
                    throw new ArithmeticException("Division by zero is not allowed.");
                }
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
