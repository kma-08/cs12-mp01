import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import java.util.EmptyStackException;

public class postFix {

	private Stack<Character> tokenContainer;
	private String infix;
	private String postfix;
	private boolean errorDetected = false;
	private String errorMsg = "";
	private static final String REGEX_FOR_OPERAND = "^(\\d+|\\d{1,3}(,\\d{3})+)(\\.\\d+)?$";
	
	public void setInfix(String infix) { 
		this.infix = presetInfix(infix); //The presetInfix method will first validate the format of the infix argument, that means we check every operand, operator, and parentheses.
	}

	public String getInfix() {
		if(!errorDetected) return this.infix; //if it so happens that an error is detected, it will return the error message rather than get the infix.
		return errorMsg;
	}

	public String infix2postfix(){

		if(errorDetected) { //if setInfix fails, i.e., an error has been detected, this method will return an error message instead of doing its usual proceedings.
			return "You can't invoke this method yet. Set your infix properly.";
		}

		tokenContainer = new Stack<>(); //We first initialize an empty stack.
		StringBuilder postfixBuilder = new StringBuilder();

		List<String> tokens = tokenize(infix); //this list stores the operators, parentheses, and operands based on their proper grouping. Note that the format of each token here has been already validated prior the setting of the infix.
		if(errorDetected) return errorMsg;
		int i = 0;
		
		while(!errorDetected && tokens.size() > i) { //While no error and not end of infix expression

			String token = tokens.get(i);
			
			//Handling Operands
			if (Character.isDigit(token.charAt(0)) || token.charAt(0) == '.') { //We check if first character of the token is a '.' or a digit, cause by then it will be an operand.
				postfixBuilder.append(token).append(" "); //We add space every after operand for a clean format.
			}
			
			//Handling Parentheses
			else if (token.equals("(")) {
				tokenContainer.push('(');
			}

			else if (token.equals(")")) {
				while (!tokenContainer.isEmpty() && tokenContainer.peek() != '(') {
					popThendisplay(postfixBuilder);
				}

				if (!tokenContainer.isEmpty() && tokenContainer.peek() == '(') {
					tokenContainer.pop(); 
				} else { 
					errorDetected = true;
					errorMsg = "Unbalanced Parentheses!"; //This error checking is for an edge case that if we did not find the '(' on top of the stack, it only means that the parentheses are unbalanced.
					return errorMsg;
				}
			}
			
			//Handling Operators
			else if (isOperator(token.charAt(0))) {
				handleOperators(token.charAt(0), postfixBuilder);
			}
			i++;
		}
		
		//2.c. When the end of the infix expression is reached, pop and display stack items until the stack is empty.
		while(!tokenContainer.isEmpty() && !errorDetected) {
			popThendisplay(postfixBuilder);
		}

		if(errorDetected) return errorMsg;

		postfix = postfixBuilder.toString();
		return "";
	}

	public String showPostfix() { //this will return an error message if the setInfix produces an error
		if(errorDetected) {
			return "You can't invoke this method yet. Set your infix properly."; 
		}
		return postfix;
	}

	public double evaluatepostfix() {

		if(errorDetected) {
			throw new IllegalStateException("You can't invoke this method yet. Set your infix properly, then invoke infix2postfix() method.");
		}
		
		if (postfix == null || postfix.trim().isEmpty()) {
	        throw new IllegalStateException("The postfix expression is empty.");
	    }
		
		Stack<Double> postContainer = new Stack<>(); //1. Initialize an empty stack
		String[] tokens = postfix.trim().split("\\s+"); //We split the postfix expression using whitespace.
		
		//2. Repeat the following until the end of the expression (l-r):
		for (int i = 0; i < tokens.length; i++) {
			String nextToken = tokens[i]; //(a) Get the next token(operand,operator) in the expression
			
			if(nextToken.length() ==1 & isOperator(nextToken.charAt(0))) { //(c) If the token is an operator: 
				
				if (postContainer.size() < 2) {
	                throw new RuntimeException("There are not enough operands to perform the operation.");
	            }
				//i. Pop two values from the stack
				double b = postContainer.pop();
				double a = postContainer.pop();
				//ii. Apply the operator to these two values, and iii. Push the resulting value back onto the stack.
				switch (nextToken) {
				case "+": postContainer.push(a + b); break;
				case "-": postContainer.push(a - b); break;
				case "*": postContainer.push(a * b); break;
				case "/":
					if (b == 0) throw new ArithmeticException("Division by zero");
					postContainer.push(a / b);
					break;
				case "%": postContainer.push(a % b); break;
				case "^": 
					validateOperation(a, b, "^");
					postContainer.push(Math.pow(a, b));
					break;
				default:
					throw new RuntimeException("Unknown operator: " + nextToken);
				}
			}
			else {
				double num = Double.parseDouble(nextToken.replace(",", ""));
				postContainer.push(num);
			}		
		}//endfor

		if(postContainer.size() != 1) {
			throw new RuntimeException("Invalid postfix expression.");
		} 
		return postContainer.pop(); //(d) When the end of expression is encountered, its value is on top of the stack.
	}
	
	                                               //Helper Methods
	
	private String presetInfix(String infix) {
		errorDetected = false;
		errorMsg = "";
		this.postfix = "";

		errorMsg = errorChecking(infix);

		if(errorMsg.equals("")) {
			return infix;
		} else {
			errorDetected = true;
		}
		return errorMsg;
	}
	
	private List<String> tokenize(String infix) {
		errorDetected = false;
		errorMsg = "";

	    List<String> tokens = new ArrayList<>();
	    int i = 0;

	    while (i < infix.length()) {
	        char ch = infix.charAt(i);

	        if (Character.isWhitespace(ch)) { //We ignore whitespace.
	            i++;
	            continue;
	        }
	        
	        //If we are dealing with an operand:
	        if (Character.isDigit(ch)) { 
	            StringBuilder num = new StringBuilder();

	            while (i < infix.length() && (Character.isDigit(infix.charAt(i)) ||
	                    infix.charAt(i) == '.' || infix.charAt(i) == ',')) {

	                		num.append(infix.charAt(i));
	                		i++;
	            }

	            tokens.add(num.toString());
	            continue;
	        }
	        
	        //If we are dealing with operators or parentheses:
	        if (isOperator(ch) || ch == '(' || ch == ')') { 
	            tokens.add(String.valueOf(ch));
	            i++;
	            continue;
	        }

	        //If we have an invalid character:
	        errorDetected = true;
	        errorMsg = "Invalid character: " + ch;
	        return tokens;
	    }//end while
	    return tokens;
	}//end tokenize
	
	//A method for popping and displaying. So we can just call it instead of constantly repeating the code.
	private void popThendisplay(StringBuilder postfixBuilder) {
		try {
			postfixBuilder.append(tokenContainer.pop()).append(" ");
		} catch (EmptyStackException e) {
			errorDetected = true;
			errorMsg = "Invalid Expression!";
		}
	}//end popThendisplay

	private void handleOperators(Character nextInput, StringBuilder postfixBuilder) {

		while (!tokenContainer.isEmpty()) {
			char top = tokenContainer.peek();

			if (top == '(') break;

			int topPrec = precedenceChecker(top);
			int currPrec = precedenceChecker(nextInput);

			if (nextInput == '^') {
				if (topPrec > currPrec) {
					popThendisplay(postfixBuilder);
				} else break;
			} else {
				if (topPrec >= currPrec) {
					popThendisplay(postfixBuilder);
				} else break;
			}
		}
		tokenContainer.push(nextInput);
	}//end handleOperators

	private int precedenceChecker(Character ch){
		switch(ch) {
			case '^': return 3;
			case '*':
			case '/':
			case '%': return 2;
			case '+':
			case '-': return 1;
			default: return 0;
		}
	}//end precedenceChecker

	private boolean isOperator(char ch) {
		return ch=='+'||ch=='-'||ch=='*'||ch=='/'||ch=='%'||ch=='^';
	}

	private boolean isDigitOrDot(char ch) {
		return (ch>='0'&&ch<='9')||ch=='.'||ch==',';
	}
	
	//Error Handling for Math Errors:
	private void validateOperation(double a, double b, String op) {
	    switch (op) {
	        case "^":
	            if (a == 0 && b == 0)
	                throw new ArithmeticException("0^0 is undefined");
	            break;
	        case "/":
	            if (b == 0)
	                throw new ArithmeticException("Division by zero");
	            break;
	    }
	}

	private String errorChecking(String infix) {
		errorDetected = false;
		errorMsg = "";
		String error = "Error: ";
		
		//Error Handling for an Empty Input:
		if (infix == null || infix.trim().isEmpty()) {
			return "Error: Empty expression";
		} 

		//This is used for initial checking if the parenthesis are balanced. The final validation is in the infix2postfix() to check if the top of the stack is '('.
		int counter = 0;
		for (int i = 0; i < infix.length(); i++) {
			char ch = infix.charAt(i);
			if (ch == '(') counter++;
			else if (ch == ')') counter--;

			if (counter < 0) return error + "Unbalanced Parenthesis!";
		}
		if (counter != 0) return error + "Unbalanced Parenthesis!";

		//Error Handling for Empty Parenthese w/o Space/s:
		if(infix.contains("()")) {
			return error + "Empty parentheses!";
		}

		//Error Handling for Invalid Character:
		for (int i = 0; i < infix.length(); i++) {
			char ch = infix.charAt(i);

			if(!Character.isWhitespace(ch) &&
			   !isDigitOrDot(ch) &&
			   !isOperator(ch) &&
			   ch!='(' && ch!=')') {

				return error + "Invalid character: " + ch;
			}
		}
		
		List<String> tokens = tokenize(infix);

		if (errorDetected) {
		    String msg = errorMsg;
		    errorDetected = false;
		    errorMsg = "";
		    return msg;
		}

		for (String token : tokens) {
		    if (Character.isDigit(token.charAt(0))) {
		        if (!token.matches(REGEX_FOR_OPERAND)) {
		            return "Error: Invalid number format: " + token;
		        }
		    }
		}//endfor

		//Error Handling for Missing Operands e.g. 1++2:
		for(int i=0;i<infix.length()-1;i++){
			char a=infix.charAt(i);
			char b=infix.charAt(i+1);

			if(isOperator(a)&&isOperator(b))
				return error+"Missing Operands/s";

			if(a=='(' && isOperator(b))
				return error+"Missing operand after '('";

			if(isOperator(a)&&b==')')
				return error+"Missing operand before ')'";
		}

		//Error Handling for Improper Use of Parentheses e.g. )(, 2(, and )3:
		for(int i=0;i<infix.length()-1;i++){
			char a=infix.charAt(i);
			char b=infix.charAt(i+1);

			if((a==')'&&b=='(')||
			   (isDigitOrDot(a)&&b=='(')||
			   (a==')'&&isDigitOrDot(b))){
				return error+"Implicit multiplication not allowed!";
			}
		}
		return ""; //if there is no error
	}
}//end postFix
