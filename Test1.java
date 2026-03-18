package calculator;
import java.util.*;


public class Test1 {
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("🧮 Advanced Smart Calculator (No ScriptEngine)");
        System.out.println("Type 'exit' to quit");

        while (true) {
            try {
                System.out.print("\nEnter expression: ");
                String input = sc.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                double result = evaluate(input);
                System.out.println("Result: " + result);

            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    // Main evaluator
    public static double evaluate(String expression) {
        expression = expression.replaceAll(" ", "");
        return evaluateExpression(expression);
    }

    private static double evaluateExpression(String expr) {
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {

            char ch = expr.charAt(i);

            // Number parsing
            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() &&
                      (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                i--;
                values.push(Double.parseDouble(sb.toString()));
            }

            // Opening bracket
            else if (ch == '(') {
                ops.push(ch);
            }

            // Closing bracket
            else if (ch == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            }

            // Operator
            else if (isOperator(ch)) {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(ch);
            }

            // Functions (sin, cos, etc.)
            else if (Character.isLetter(ch)) {
                StringBuilder func = new StringBuilder();
                while (i < expr.length() && Character.isLetter(expr.charAt(i))) {
                    func.append(expr.charAt(i++));
                }
                i--; // adjust index

                if (expr.charAt(i + 1) == '(') {
                    int j = i + 2, count = 1;
                    while (j < expr.length() && count != 0) {
                        if (expr.charAt(j) == '(') count++;
                        if (expr.charAt(j) == ')') count--;
                        j++;
                    }

                    double val = evaluateExpression(expr.substring(i + 2, j - 1));
                    values.push(applyFunction(func.toString(), val));
                    i = j - 1;
                }
            }
        }

        while (!ops.isEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static boolean isOperator(char op) {
        return op == '+' || op == '-' || op == '*' || op == '/';
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private static double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
        }
        return 0;
    }

    private static double applyFunction(String func, double value) {
        switch (func) {
            case "sin": return Math.sin(Math.toRadians(value));
            case "cos": return Math.cos(Math.toRadians(value));
            case "tan": return Math.tan(Math.toRadians(value));
            case "log": return Math.log10(value);
            case "sqrt": return Math.sqrt(value);
            default: throw new IllegalArgumentException("Unknown function: " + func);
        }
    }
}

	