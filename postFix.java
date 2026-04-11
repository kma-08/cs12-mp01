import java.util.Stack;
import java.util.EmptyStackException;

/*
 Unresolved: 
 (1+)*2
 */

//1. Java Class named postFix
public class postFix {
	private Stack<Character> tokenContainer;
	private String infix;
	private String postfix;
	private boolean errorDetected = false;
	private String errorMsg = "";
	
	
	//Public constructors
	public postFix() {
		this.infix = "";
	}
	
	public postFix(String infix) { 
		setInfix(infix);
	}
	
	//2. setInfix(String)
	public void setInfix(String infix) { 
		errorDetected = false;
		errorMsg = "";
		postfix = "";
		String operators = "^*/%+-";
		
		int returnVal = checkInfix(infix, operators);
		
		switch (returnVal){
			case 0:
				this.infix = infix.replace(" ", ""); 
				errorMsg = "";
				break;
			case -1: 
				errorDetected = true;
				errorMsg = "Error: Invalid expression!"; //is this too vague?
				break;
			case -2:
				errorDetected = true;
				errorMsg = "Error: Missing Operand"; 
				break;
			default:
				errorDetected = true;
				errorMsg = "Error: Unbalanced Parentheses!";
				break;
		}
	}
	
	//3. getInfix()
	public String getInfix() { 
		return this.infix;
	}
	
	//4. infix2postfix()
	public String infix2postfix(){ 
		
		if(errorDetected) {
			return errorMsg;
		}
		
		tokenContainer = new Stack<>(); 
		postfix = "";
		String operators = "^*/%+-";
		String nums = ".,0123456789";
		int ctr=0;
		
		//Note that the numbering indicated in this part are based on the provided general algorithm of converting an infix expression to a postfix expression.
		
		while(errorDetected == false && ctr < infix.length()) { //2, NOTE: d q pah sure pano iimplement yung while no error 
			
			//2.a
			Character nextInput = infix.charAt(ctr); 
			
			//2.b.i & 2.b.ii: Handling Parentheses
			if(nextInput.equals('(') || nextInput.equals(')')) {
				handleParentheses(nextInput);
				ctr++;
			}
			
			//2.b.iii: Handling Operators
			else if(operators.contains(String.valueOf(nextInput))){ 
				handleOperators(nextInput);
				ctr++;
			}
			
			//2.b.iv: Handling Operands
			else if(nums.contains(String.valueOf(nextInput))){
				StringBuilder cont = new StringBuilder(); //found in the API to be efficient when dealing w/ building Characters to String. 
				String operand;
				
				while(ctr < infix.length() && nums.contains(String.valueOf(infix.charAt(ctr)))) {
					cont.append(infix.charAt(ctr));
					ctr++;
				}
				operand = cont.toString();
				if(operand.contains(".") || operand.contains(",")){
					operand = checkOperand(operand);
				}

				if (errorDetected) return errorMsg;

				postfix += operand + " ";
				
			}
			
			//Handles Unknown Input Token ("umbrella error handling" so it captures unknown operators as well)
			else if(!nums.contains(String.valueOf(nextInput)) && 
			        !operators.contains(String.valueOf(nextInput)) && 
			        !nextInput.equals('(') && 
			        !nextInput.equals(')')) {
				
				errorDetected = true;
				errorMsg = "Error: Unknown Input Token!";
				return errorMsg;
			}
			
		} //end while
		
		while(!tokenContainer.isEmpty()) { //2.c. Pop and display stack items until the stack is empty.
			popThendisplay();
		}
		
		if(errorDetected) {
			return errorMsg;
		}
		return postfix;
		
	} //end infix2postfix
	
	public String showPostfix() {
		return this.postfix;
	}
	
	//helper methods
	
	private int checkInfix(String infix, String operators) {
		int leftParenthesis = 0;
		int rightParenthesis = 0;
		
		for(Character hold: operators.toCharArray()) {
			if(infix.startsWith("" + hold) || infix.startsWith(")") || infix.endsWith("" + hold)) {
				return -1;
			}
		}
		
		for(int i = 0; i<infix.length()-1; i++) {
			if(operators.contains("" + infix.charAt(i)) && operators.contains("" + infix.charAt(i + 1))){
				return -2;
			}
			if(infix.charAt(i)=='(') {
				leftParenthesis++;
			}
			else if(infix.charAt(i)==')') {
				rightParenthesis++;
				
			}
		}
		
		if(leftParenthesis != rightParenthesis) {
			return -3;
		}
		return 0;
	}
	
	private void popThendisplay() {
		try {
			Character ch = tokenContainer.pop();
			postfix = postfix + ch + " ";
		}
		catch(EmptyStackException e) {
			errorDetected = true;
			errorMsg = "Error: Invalid Expression!";
		}
	}
	
	private void handleParentheses(Character nextInput) {
		
		if(nextInput.equals('(')) {
			tokenContainer.push(nextInput);
		}
		else {
			while(tokenContainer.isEmpty() == false && tokenContainer.peek() != '(') { //while the stack is not empty and we haven't reached the left parenthesis yet
				popThendisplay();
			}
			
			if(!tokenContainer.isEmpty() && tokenContainer.peek() =='(') {
				tokenContainer.pop(); //Since we want to pop "(", but not display it. We are sure that we will be popping "(" here since by the time that the while construct is over, "(" will be the one on top of the stack.
			} else {
				errorDetected = true;
				errorMsg = "Error: Unbalanced Parentheses!";
			}
		}
	}
	
	private void handleOperators(Character nextInput) { //i think may way pa para masimplify toh pero di pa mag sink in sakin
		while (!tokenContainer.isEmpty()) {
			Character topOfStack = tokenContainer.peek();
			
			if (topOfStack == '(') break;

		    int topPrec = precedenceChecker(topOfStack);
		    int currPrec = precedenceChecker(nextInput);

		    if (nextInput.equals('^')) { //since right associative siya
		    	if (topPrec > currPrec) {
		    		popThendisplay();
		        } else break; 
		    }
	
		    else {
		        if (topPrec >= currPrec) {
		            popThendisplay();
		        } else break;
		    }
	    }

		tokenContainer.push(nextInput);
		
	} 
	
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
	}
	
	private String checkOperand(String operand) {

	    int dotCtr = 0;
	    for (char c : operand.toCharArray()) {
	        if (c == '.') dotCtr++;
	        if (dotCtr > 1) {
	            errorDetected = true;
	            errorMsg = "Error: Too Many Decimal Points!";
	            return operand;
	        }
	    }
	    
	    //Considering more cases:
	    String integer = operand;
	    String decimals = "";

	    if (operand.contains(".")) {
	        String[] s = operand.split("\\."); 
	        integer = s[0];
	        decimals = s.length > 1 ? s[1] : ""; //ternary operator learned from CMSC11
	    }

	    if (decimals.contains(",")) {
	        errorDetected = true;
	        errorMsg = "Error: Illegal Operand Format";
	        return operand;
	    }

	    if (integer.contains(",")) {
	        if (integer.startsWith(",") || integer.endsWith(",")) {
	            errorDetected = true;
	            errorMsg = "Error: Illegal Operand Format";
	            return operand;
	        }

	        String[] s2 = integer.split(",");

	        if (s2[0].length() < 1 || s2[0].length() > 3) {
	            errorDetected = true;
	            errorMsg = "Error: Illegal Operand Format";
	            return operand;
	        }

	        for (int i = 1; i < s2.length; i++) {
	            if (s2[i].length() != 3) {
	                errorDetected = true;
	                errorMsg = "Error: Illegal Operand Format";
	                return operand;
	            }
	        }
	    }
	    return operand.replace(",", "");
	}
}
	
