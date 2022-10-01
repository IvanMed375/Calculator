package calculator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.regex.Pattern;

public class Calculator extends JFrame {



    static int getPrecedence (char operator) {
        return switch (operator) {
            case '^' -> 3;
            case '\u00D7', '\u00F7' -> 2;
            case '\u002D', '\u002B' -> 1;
            default -> 0;
        };
    }

    public Calculator() {
        super("Calculator");
        setTitle("Calculator");
        setName("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 663);
        setLocationRelativeTo(null);

        var leftParenthesesCounterWrapper = new Object(){int counter = 0;};
        var rightParenthesesCounterWrapper = new Object(){int counter = 0;};

        JLabel heartLabel = new JLabel("\u2764");
        heartLabel.setBounds(140, 200, 105, 50);
        heartLabel.setName("HeartLabel");
        heartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        heartLabel.setFont(new Font("Monospace", Font.BOLD, 48));

        add(heartLabel);

        JLabel ResultLabel = new JLabel("0");
        ResultLabel.setBounds(25, 25, 450, 75);
        ResultLabel.setName("ResultLabel");
        ResultLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        ResultLabel.setFont(new Font("Monospace", Font.BOLD, 48));
        add(ResultLabel);

        JLabel EquationLabel = new JLabel();
        EquationLabel.setBounds(25, 125, 450, 25);
        EquationLabel.setName("EquationLabel");
        EquationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        EquationLabel.setFont(new Font("Monospace", Font.BOLD, 16));
        add(EquationLabel);

        ArrayList<JButton> digitButtons = new ArrayList<>(10);
        digitButtons.add(0, new JButton("0"));
        digitButtons.add(1, new JButton("1"));
        digitButtons.add(2, new JButton("2"));
        digitButtons.add(3, new JButton("3"));
        digitButtons.add(4, new JButton("4"));
        digitButtons.add(5, new JButton("5"));
        digitButtons.add(6, new JButton("6"));
        digitButtons.add(7, new JButton("7"));
        digitButtons.add(8, new JButton("8"));
        digitButtons.add(9, new JButton("9"));

        digitButtons.get(0).setName("Zero");
        digitButtons.get(1).setName("One");
        digitButtons.get(2).setName("Two");
        digitButtons.get(3).setName("Three");
        digitButtons.get(4).setName("Four");
        digitButtons.get(5).setName("Five");
        digitButtons.get(6).setName("Six");
        digitButtons.get(7).setName("Seven");
        digitButtons.get(8).setName("Eight");
        digitButtons.get(9).setName("Nine");

        digitButtons.get(0).setBounds(140, 515, 105, 50);
        digitButtons.get(1).setBounds(25, 452, 105, 50);
        digitButtons.get(2).setBounds(140, 452, 105, 50);
        digitButtons.get(3).setBounds(255, 452, 105, 50);
        digitButtons.get(4).setBounds(25, 389, 105, 50);
        digitButtons.get(5).setBounds(140, 389, 105, 50);
        digitButtons.get(6).setBounds(255, 389, 105, 50);
        digitButtons.get(7).setBounds(25, 326, 105, 50);
        digitButtons.get(8).setBounds(140, 326, 105, 50);
        digitButtons.get(9).setBounds(255, 326, 105, 50);

        digitButtons.forEach(this::add);

        for (int i = 0; i < 10; i++) {
            digitButtons.get(i).setFont(new Font("Monospace", Font.BOLD, 16));
        }

        digitButtons.forEach(jButton ->
                jButton.addActionListener(e ->
                {
                        EquationLabel.setForeground(Color.GREEN.brighter());
                        EquationLabel.setText(EquationLabel.getText() + jButton.getText());
                }));

        JButton dot = new JButton(".");

        dot.setBounds(255, 515, 105, 50);
        dot.setName("Dot");
        dot.setFont(new Font("Monospace", Font.BOLD, 16));
        add(dot);

        dot.addActionListener(e -> {
            EquationLabel.setForeground(Color.GREEN.brighter());
            EquationLabel.setText(EquationLabel.getText() + dot.getText());
        });

        ArrayList<JButton> MathOperatorButton = new ArrayList<>(4);

        MathOperatorButton.add(0, new JButton("\u00F7"));
        MathOperatorButton.add(1, new JButton("\u00D7"));
        MathOperatorButton.add(2, new JButton("\u002D"));
        MathOperatorButton.add(3, new JButton("\u002B"));

        MathOperatorButton.get(0).setName("Divide");
        MathOperatorButton.get(1).setName("Multiply");
        MathOperatorButton.get(2).setName("Subtract");
        MathOperatorButton.get(3).setName("Add");

        MathOperatorButton.get(0).setBounds(370, 263,105, 50);
        MathOperatorButton.get(1).setBounds(370, 326, 105, 50);
        MathOperatorButton.get(2).setBounds(370, 389, 105, 50);
        MathOperatorButton.get(3).setBounds(370, 452, 105, 50);

        MathOperatorButton.forEach(this::add);

        for (int i = 0; i < 4; i++) {
            MathOperatorButton.get(i).setFont(new Font("Monospace", Font.BOLD, 16));
        }

        MathOperatorButton.forEach(jButton ->
                jButton.addActionListener(e -> {
                    EquationLabel.setForeground(Color.GREEN.brighter());
                    String expression = EquationLabel.getText();
                    if (!expression.isEmpty()) {
                        char[] charArray = expression.toCharArray();
                        for (int i = charArray.length - 1; i > -1; i--) {
                            char c = charArray[i];
                            if (!Character.isDigit(c) && c != '.') {
                                break;
                            }
                            if (c == '.') {
                                if (i == 0 || !Character.isDigit(charArray[i - 1])) {
                                    expression = expression.substring(0, i) + "0" + expression.substring(i);
                                }
                                if (i == charArray.length - 1 || !Character.isDigit(charArray[i + 1])) {
                                    expression = expression.substring(0, i + 1) + "0" +
                                            expression.substring(i + 1);
                                }
                            }
                        }
                        char lastInExpression = expression.charAt(expression.length() - 1);
                        if (!Character.isDigit(lastInExpression) && lastInExpression != '.' &&
                                lastInExpression != ')') {
                            expression = expression.substring(0, expression.length() - 1);
                        }
                        if (lastInExpression != '(') {
                            EquationLabel.setText(expression + jButton.getText());
                        }
                    }
                }));




        JButton parentheses = new JButton("()");
        parentheses.setBounds(25, 200, 105, 50);
        parentheses.setName("Parentheses");
        parentheses.setFont(new Font("Monospace", Font.BOLD, 16));

        parentheses.addActionListener(e -> {
            EquationLabel.setForeground(Color.GREEN.brighter());
            String expression = EquationLabel.getText();
            int length = expression.length();
            if (!expression.isEmpty() && expression.charAt(length - 1) == ')'  &&
                    rightParenthesesCounterWrapper.counter == leftParenthesesCounterWrapper.counter ) {
                return;
            }
            if (expression.isEmpty() || expression.charAt(length - 1) == '(' ||
                    rightParenthesesCounterWrapper.counter == leftParenthesesCounterWrapper.counter ||
                    expression.charAt(length - 1) == '\u00D7' ||
                    expression.charAt(length - 1) == '\u00F7' ||
                    expression.charAt(length - 1) == '\u002D' ||
                    expression.charAt(length - 1) == '\u002B') {
                leftParenthesesCounterWrapper.counter++;
                EquationLabel.setText(expression + "(");
            } else {
                rightParenthesesCounterWrapper.counter++;
                EquationLabel.setText(expression + ")");
            }
        });

        add(parentheses);

        JButton squareRoot = new JButton("√");
        squareRoot.setBounds(255, 263, 105, 50);
        squareRoot.setName("SquareRoot");
        squareRoot.setFont(new Font("Monospace", Font.BOLD, 16));

        squareRoot.addActionListener(e -> {
            EquationLabel.setForeground(Color.GREEN.brighter());
            EquationLabel.setText(EquationLabel.getText() + "√(");
            leftParenthesesCounterWrapper.counter++;
        });

        add(squareRoot);

        JButton powerTwo = new JButton("x\u00B2");
        powerTwo.setBounds(25, 263, 105, 50);
        powerTwo.setName("PowerTwo");
        powerTwo.setFont(new Font("Monospace", Font.BOLD, 16));

        powerTwo.addActionListener(e -> {
            EquationLabel.setForeground(Color.GREEN.brighter());
            EquationLabel.setText(EquationLabel.getText() + "^(2)");
        });

        add(powerTwo);

        JButton powerY = new JButton("x\u02B8");
        powerY.setBounds(140, 263, 105, 50);
        powerY.setName("PowerY");
        powerY.setFont(new Font("Monospace", Font.BOLD, 16));

        powerY.addActionListener(e -> {
            EquationLabel.setForeground(Color.GREEN.brighter());
            EquationLabel.setText(EquationLabel.getText() + "^(");
            leftParenthesesCounterWrapper.counter++;
        });

        add(powerY);

        JButton plusMinus = new JButton("±");
        plusMinus.setBounds(25, 515, 105, 50);
        plusMinus.setName("PlusMinus");
        plusMinus.setFont(new Font("Monospace", Font.BOLD, 16));

        add(plusMinus);


        plusMinus.addActionListener(e -> {
            String expression = EquationLabel.getText();
            int length = expression.length();
            if (expression.isEmpty()) {
                EquationLabel.setText("(-");
                leftParenthesesCounterWrapper.counter++;
                return;
            }
            if (expression.equals("(-")) {
                EquationLabel.setText("");
                leftParenthesesCounterWrapper.counter--;
                return;
            }
            if (expression.charAt(length - 1) == '(') {
                EquationLabel.setText(expression + "(-");
                leftParenthesesCounterWrapper.counter++;
            }
            char[] charArray = expression.toCharArray();
            for (int i = charArray.length - 1; i > -1; i--) { // adding 0. or .0 if needed
                char c = charArray[i];
                if (!Character.isDigit(c) && c != '.') {
                    break;
                }
                if (c == '.') {
                    if (i == 0 || !Character.isDigit(charArray[i - 1])) {
                        expression = expression.substring(0, i) + "0" + expression.substring(i);
                    }
                    if (i == charArray.length - 1 || !Character.isDigit(charArray[i + 1])) {
                        expression = expression.substring(0, i + 1) + "0" +
                                expression.substring(i + 1);
                    }
                    char lastInExpression = expression.charAt(expression.length() - 1);
                    if (!Character.isDigit(lastInExpression) && lastInExpression != '.') {
                        expression = expression.substring(0, expression.length() - 1);
                    }
                }
            }
            int i = length - 1;
            if (expression.charAt(length - 1) == ')') { // in this case i becomes the position of
                // the ( before -
                int counter = 1;
                while (counter > 0) {
                    /*if (i < 0) {
                        System.out.println("Error in plusMinus action");
                        break;
                    }*/
                    i--;
                    switch (charArray[i]) {
                        case '(' -> counter--;
                        case ')' -> counter++;
                    }
                }
            } else if (Character.isDigit(expression.charAt(length - 1)) || expression.charAt(length - 1) == '.') {
                // in this case i becomes the position of the first digit of the first number in parentheses
                while (Character.isDigit(charArray[i]) || charArray[i] == '.') {
                    if (i == 0) {
                        break;
                    }
                    i--;
                }
                if (charArray[i] == '-') {
                    i--;
                }
            } else {
                return;
            }
            if (length == i + 1  || charArray[i + 1] != '-') {
                expression = expression.substring(0, i) + "(" + "\u002D" +
                        expression.substring(i, length) /*+ ")"*/;
                leftParenthesesCounterWrapper.counter++;
            } else {
                if (charArray[length - 1] == ')') {
                    expression = expression.substring(0, i) + expression.substring(i + 2, length - 1);
                    rightParenthesesCounterWrapper.counter--;
                } else {
                    expression = expression.substring(0, i) + expression.substring(i + 2, length);
                }
            }

            EquationLabel.setText(expression);
        });


        JButton equals = new JButton("=");
        equals.setBounds(370, 515, 105, 50);
        equals.setName("Equals");
        equals.setFont(new Font("Monospace", Font.BOLD, 16));

        add(equals);

        equals.addActionListener(e -> {
            String expression = EquationLabel.getText();
            Deque<String> operators = new ArrayDeque<>();
            ArrayList<String> postfix = new ArrayList<>();
            StringBuilder operand = new StringBuilder();


            char[] charArray = expression.toCharArray();
            for (int i = charArray.length - 1; i > -1; i--) { // adding 0. or .0 if needed
                char c = charArray[i];
                if (!Character.isDigit(c) && c != '.') {
                    break;
                }
                if (c == '.') {
                    if (i == 0 || !Character.isDigit(charArray[i - 1])) {
                        expression = expression.substring(0, i) + "0" + expression.substring(i);
                    }
                    if (i == charArray.length - 1 || !Character.isDigit(charArray[i + 1])) {
                        expression = expression.substring(0, i + 1) + "0" + expression.substring(i + 1);
                    }
                    char lastInExpression = expression.charAt(expression.length() - 1);
                    if (!Character.isDigit(lastInExpression) && lastInExpression != '.') {
                        expression = expression.substring(0, expression.length() - 1);
                    }
                }
            }

            EquationLabel.setText(expression);

            expression = expression + "=";

            char firstInExpression = expression.charAt(0);
            char lastInExpression = expression.charAt(expression.length() - 2);


            if (!Character.isDigit(firstInExpression) && firstInExpression != '.' && firstInExpression != '('
                    && firstInExpression != '√' || !Character.isDigit(lastInExpression) && lastInExpression != '.'
                    && lastInExpression != ')') { // checking for errors made by the user
                EquationLabel.setForeground(Color.RED.darker());
                return;
            }

            boolean onlyNumber = true;
            boolean squareRootProblem = false;
            boolean plusMinusProblem = false;

            char[] charArrayExpression = expression.toCharArray();

            for (int i = 0; i < charArrayExpression.length; i++) { // converting infix to postfix
                char c = charArrayExpression[i];
                if (Character.isDigit(c) || c == '.') {
                    operand.append(c);
                } else {
                    if (c == '=') {
                        if (!operand.isEmpty()) {
                            postfix.add(operand.toString());
                        }
                        while (!operators.isEmpty()) {
                            postfix.add(operators.pop());
                        }

                    } else {
                        onlyNumber = false;
                        switch (c) {
                            case '(':
                                if (!squareRootProblem) {
                                    operators.push(String.valueOf(c));
                                } else {
                                    squareRootProblem = false;
                                }
                                break;
                            case ')':
                                if (!operand.isEmpty()) {
                                    if (plusMinusProblem) {
                                        plusMinusProblem = false;
                                        postfix.add(String.valueOf(Integer.parseInt(operand.toString()) * -1));

                                    } else {
                                        postfix.add(operand.toString());
                                    }
                                    operand = new StringBuilder();
                                }
                                String nextOperator = " ";
                                if (!operators.isEmpty()) {
                                    nextOperator = operators.getFirst();
                                }
                                while (!operators.isEmpty() && !nextOperator.equals("(") &&
                                        !nextOperator.equals("√")) {
                                    postfix.add(operators.pop());
                                    nextOperator = operators.getFirst();
                                }
                                operators.pop();
                                if (nextOperator.equals("√")) {
                                    operators.push("^");
                                    postfix.add("0.5");
                                }
                                break;

                           // subtraction, adding, multiplication and division
                            case '\u002D':
                                if (i != 0 && charArrayExpression[i - 1] == '(' ) {
                                    plusMinusProblem = true;
                                    continue;
                                }
                            case '\u002B':
                            case '\u00D7':
                            case '\u00F7':
                            case '^':
                                if (!operand.isEmpty()) {
                                    if (plusMinusProblem) {
                                        plusMinusProblem = false;
                                        postfix.add(String.valueOf(Integer.parseInt(operand.toString()) * -1));

                                    } else {
                                        postfix.add(operand.toString());
                                    }
                                    operand = new StringBuilder();
                                }
                                int currentPrecedence = getPrecedence(c);
                                char next = ' ';
                                if (!operators.isEmpty()) {
                                    next = operators.getFirst().charAt(0);
                                }
                                while (!operators.isEmpty() && currentPrecedence <= getPrecedence(next)) {
                                    postfix.add(operators.pop());
                                    if (!operators.isEmpty()) {
                                        next = operators.getFirst().charAt(0);
                                    }
                                }
                                operators.push(String.valueOf(c));
                                break;
                            case '√':
                                squareRootProblem = true;
                                operators.push("√");
                                break;
                        }

                    }
                }
            }

            if (onlyNumber) {
                EquationLabel.setForeground(Color.RED.darker());
                return;
            }

                Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

                Deque<Double> operands = new ArrayDeque<>();

                for (String postfix_i : postfix) { // calculating answer for a postfix
                    if (pattern.matcher(postfix_i).matches()) {
                        operands.push(Double.parseDouble(postfix_i));
                        continue;
                    }


                    switch (postfix_i) {
                        case "\u002B" -> operands.push(operands.pop() + operands.pop());
                        case "\u002D" -> {
                            double subtrahend = operands.pop();
                            double minuend = operands.pop();
                            operands.push(minuend - subtrahend);
                        }
                        case "\u00D7" -> operands.push(operands.pop() * operands.pop());
                        case "\u00F7" -> {
                            double denominator = operands.pop();
                            if (denominator == 0) {
                                EquationLabel.setForeground(Color.RED.darker());
                                return;
                            }
                            double divisor = operands.pop();
                            operands.push(divisor / denominator);
                        }
                        case "^" -> {
                            double exponent = operands.pop();
                            double base = operands.pop();
                            double checkingForExponentError = Math.pow(base, exponent);
                            if (Double.isNaN(checkingForExponentError)) {
                                EquationLabel.setForeground(Color.RED.darker());
                                return;
                            }
                            operands.push(checkingForExponentError);
                        }
                        default -> System.out.println("error");
                    }
                }

                double result;

                try {
                    result = operands.peek();
                } catch (NullPointerException exception) {
                    result = 0;
                }
                if (result % 1 == 0) {
                    ResultLabel.setText(String.valueOf((int) result));
                } else {
                    ResultLabel.setText(String.valueOf(result));
                }
        });


        JButton clear = new JButton("C");

        clear.setName("Clear");
        clear.setBounds(255, 200, 105, 50);
        clear.setFont(new Font("Monospace", Font.BOLD, 16));
        add(clear);

        clear.addActionListener(e -> {
            EquationLabel.setForeground(Color.GREEN.brighter());
            EquationLabel.setText("");
            leftParenthesesCounterWrapper.counter = 0;
            rightParenthesesCounterWrapper.counter = 0;
        });


        JButton delete = new JButton("Del");

        delete.setBounds(370, 200, 105, 50);
        delete.setName("Delete");
        delete.setFont(new Font("Monospace", Font.BOLD, 16));
        add(delete);

        delete.addActionListener(e -> {
                EquationLabel.setForeground(Color.GREEN.brighter());
                String equation = EquationLabel.getText();
                int length = equation.length();
                if (length > 0) {
                    if (equation.charAt(length - 1) == ')') {
                        rightParenthesesCounterWrapper.counter--;
                    } else if (equation.charAt(length - 1) == '(') {
                        leftParenthesesCounterWrapper.counter--;
                    }
                } else {
                    EquationLabel.setText("");
                }
                EquationLabel.setText(length > 1 ? equation.substring(0, length - 1) : "");
        });

        setLayout(null);
        setVisible(true);

    }
}
