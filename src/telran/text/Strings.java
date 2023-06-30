package telran.text;

import java.util.HashMap;
import java.util.function.BinaryOperator;

public class Strings {
	
	static HashMap<String, BinaryOperator<Integer>> mapOperations;
	static {
		mapOperations = new HashMap<>();
		mapOperations.put("-", (a, b) -> a - b);
		mapOperations.put("+", (a, b) -> a + b);
		mapOperations.put("*", (a, b) -> a * b);
		mapOperations.put("/", (a, b) -> a / b);
	}
	static HashMap<String, BinaryOperator<Double>> mapDoubleOperations;
	static {
		mapDoubleOperations = new HashMap<>();
		mapDoubleOperations.put("-", (a, b) -> a - b);
		mapDoubleOperations.put("+", (a, b) -> a + b);
		mapDoubleOperations.put("*", (a, b) -> a * b);
		mapDoubleOperations.put("/", (a, b) -> a / b);
	}

	public static String javaVariableName() {
		//return "[a-zA-Z$][\\w$]*|_[\\w$]+";
		return "([a-zA-Z$][\\w$]*|_[\\w$]+)";
	}

	public static String zero_300() {
		return "[1-9]\\d?|[1-2]\\d\\d|300|0";
	}

	public static String ipV4Octet() {
		return "[0-9]\\d?|[0-1]\\d\\d|2[0-4]\\d|25[0-5]";
		//return  "([01]?\\d\\d?|2([0-4]\\d|5[0-5]))";
	}

	public static String ipV4() {
		String res = "";
		for (int i = 0; i < 4; i++) {
			res += "(" + ipV4Octet() + ")";
			if (i != 3) {
				res += ".";
			}
		}
		return res;
		//String octetRegex = ipV4Octet();
		//return String.format("(%s\\.){3}%1$s",octetRegex);
	}

	public static String arithmeticExpression() {
		String operandRE = operand();
		String operatorRE = operator();
		return String.format("%1$s(%2$s%1$s)*",operandRE, operatorRE );
	}
	public static String operator() {
		return "\\s*([-+*/])\\s*";
	}
	public static String operand() {
		//assumption: not unary operators
		String operNumeric = "([\\d]+\\.?[\\d]*)";
		String operVariable = javaVariableName();
		return String.format("(%s|%s)", operNumeric, operVariable);
	}
	public static boolean isArithmeticExpression(String expression) {
		expression = expression.trim();
		return expression.matches(arithmeticExpression());
	}
	
	public static int computeExpression(String expression) {
		if (!isArithmeticExpression(expression)) {
			throw new IllegalArgumentException("Wrong arithmetic expression");
		}
		expression = expression.replaceAll("\\s+", "");
		String[] operands = expression.split(operator());
		String [] operators = expression.split(operand());
		int res = Integer.parseInt(operands[0]);
		for(int i = 1; i < operands.length; i++) {
			int operand = Integer.parseInt(operands[i]);
			res = mapOperations.get(operators[i]).apply(res, operand);
		}
		
		return res;
	}
	
	//Update whole code for any numbers (double)
	//Update code taking into consideration possible variable names
	
	public static double computeExpression(String expression, 
					HashMap<String, BinaryOperator<Double>> mapVariables) {
		if (!isArithmeticExpression(expression)) {
			throw new IllegalArgumentException("Wrong arithmetic expression");
		}
		expression = expression.replaceAll("\\s+", "");
		String[] operands = expression.split(operator());
		String [] operators = expression.split(operand());
		double res = Double.parseDouble(operands[0]);
		for(int i = 1; i < operands.length; i++) {
			double operand = Double.parseDouble(operands[i]);
			res = mapVariables.get(operators[i]).apply(res, operand);
		}
		
		return res;
	}
}
