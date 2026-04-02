import java.util.Stack;

public class postFix {
	private Stack<String> tokenContainer;
	private String infix;
	private String postfix = "";
	
	public void setInfix(String infix) {
		this.infix = infix;
	}
	
	public String getInfix() {
		return this.infix;
	}
	
	public void infix2postfix(){
		tokenContainer = new Stack<>(); //1. Initialize an empty stack of operators
		int ctr=0;
		String operators = "^*/%+-";
		String nums = ".0123456789";
		
		while(ctr < infix.length()) { //you wnna go over the infix string
			String nextInput = infix.substring(ctr, ctr+1);
			
			//handling parentheses
			if(nextInput.equals("(") || nextInput.equals(")")) {
				handleParentheses(nextInput);
				ctr++;
			}
			
			//handling operators
			else if(operators.contains(nextInput)){
				handleOperators(nextInput);
				ctr++;
			}
			
			//handling operands
			else {
				String cont = ""; 
				while(ctr < infix.length() && nums.contains(infix.substring(ctr, ctr+1))) {
					cont = cont + infix.substring(ctr, ctr+1);
					ctr++;
				}
				postfix = postfix + cont + " ";
			}
			
		} //end while
		
		while(!tokenContainer.isEmpty()) { //popping and displaying 'til emptyy
			popThendisplay();
		}
		
	} //end infix2postfix
	
	public String showPostfix() {
		return this.postfix;
	}
	
	
	
import java.util.Stack;
import java.util.EmptyStackException;

/*
 TO DO:
 
 1. Clarify kay sir if okay lang na sa error handling ay mag throw new IllegalArgumentException na lang since yung nasa hinihingi niya kasi is may return
 */

//1. Java Class named postFix
public class postFix {
	private Stack<Character> tokenContainer;
	private String infix;
	private String postfix;
	
	//2. setInfix(String)
	public void setInfix(String infix) { 
		this.infix = infix;
	}
	
	//3. getInfix()
	public String getInfix() { 
		return this.infix;
	}
	
	//4. infix2postfix()
	public void infix2postfix(){ 
		tokenContainer = new Stack<>(); 
		postfix = "";
		int ctr=0;
		String operators = "^*/%+-";
		String nums = ".0123456789";
		
		//Note that the numbering indicated in this part are based on the provided general algorithm of converting an infix expression to a postfix expression.
		
		while(ctr < infix.length()) { //2.b, NOTE: d q pah sure pano iimplement yung while no error 
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
				
				while(ctr < infix.length() && nums.contains(String.valueOf(infix.charAt(ctr)))) {
					cont.append(infix.charAt(ctr));
					ctr++;
				}
				postfix = postfix + cont.toString() + " ";
				
			}
			
			//Handles Unknown Input Token ("umbrella error handling" so it captures unknown operators as well)
			else if(!nums.contains(String.valueOf(nextInput)) && 
			        !operators.contains(String.valueOf(nextInput)) && 
			        !nextInput.equals('(') && 
			        !nextInput.equals(')')) {

			    throw new IllegalArgumentException("Unknown Token!"); //conflicted ako if return na ganto or pwedeng throw new IllegalArgException pero sabi ni sir magrereturn dw dpat e 
			}
			
		} //end while
		
		while(!tokenContainer.isEmpty()) { //2.c. Pop and display stack items until the stack is empty.
			popThendisplay();
		}
		
	} //end infix2postfix
	
	public String showPostfix() {
		return this.postfix;
	}
	
	//helper methods
	
	private void popThendisplay() {
		try {
			Character ch = tokenContainer.pop();
			postfix = postfix + ch + " ";
		}
		catch(EmptyStackException e) {
			throw new IllegalArgumentException("Invalid Expression!");
		}
	}
	
	private void handleParentheses(Character nextInput) {
		if(nextInput.equals('(')) {
			tokenContainer.push(nextInput);
		}
		else if(nextInput.equals(')')) {
			while(!tokenContainer.isEmpty() && !(tokenContainer.peek()).equals('(')) {
				popThendisplay();
			}
			
			if(tokenContainer.isEmpty()) {
				throw new IllegalArgumentException("Unbalanced Parentheses!");
			}
			
			tokenContainer.pop(); //Since we want to pop "(", but not display it. We are sure that we will be popping "(" here since by the time that the while construct is over, "(" will be the one on top of the stack.
		}
	}
	
	private void handleOperators(Character nextInput) { //i think may way pa para masimplify toh pero di pa mag sink in sakin
		if(tokenContainer.isEmpty() || precedenceChecker(tokenContainer.peek()) < precedenceChecker(nextInput)) {
			tokenContainer.push(nextInput);
		}
		else if (!tokenContainer.isEmpty() && precedenceChecker(tokenContainer.peek()) > precedenceChecker(nextInput)){
			while(!tokenContainer.isEmpty() && precedenceChecker(tokenContainer.peek()) > precedenceChecker(nextInput)) { //Since we want to pop the operator na and include it in the postfix if it happens to have a higher precedence than the input token.
				popThendisplay();
			}
		}
		else { //for the equal case
			if(nextInput.equals('^')) { //since right associative sha
				tokenContainer.push(nextInput);
			}
			else {
				Character ch = tokenContainer.pop();
				postfix = postfix + ch + " ";
				tokenContainer.push(nextInput);
			}
		}
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
	
	
}	
	//helper methods
	
	private int precedenceChecker(String str){
		switch(str) {
			case "^": return 3;
			case "*":
			case "/":
			case "%": return 2;
			case "+":
			case "-": return 1;
			default: return 0;
		}
	}
	
	private void popThendisplay() {
		String str = tokenContainer.pop();
		postfix = postfix + str + " ";
	}
	
	private void handleParentheses(String nextInput) {
		if(nextInput.equals("(")) {
			tokenContainer.push(nextInput);
		}
		else if(nextInput.equals(")")) {
			while(!(tokenContainer.peek()).equals("(")) {
				popThendisplay();
			}
			tokenContainer.pop(); //cuz we want to pop ( this then ignore. By the time na makaalis na sha sa while si ( na yung nasa taas ng stack
		}
	}
	
	private void handleOperators(String nextInput) {
		if(tokenContainer.isEmpty() || precedenceChecker(tokenContainer.peek()) < precedenceChecker(nextInput)) {
			tokenContainer.push(nextInput);
		}
		else if (!tokenContainer.isEmpty() && precedenceChecker(tokenContainer.peek()) > precedenceChecker(nextInput)){
			while(precedenceChecker(tokenContainer.peek()) > precedenceChecker(nextInput)) { //kailangan kasi natin ilagay sa ilalim ung nextInput if mas mababa yung precedence niya
				popThendisplay();
			}
		}
		else { //for the equal case
			if(nextInput.equals("^")) { //since right associative sha
				tokenContainer.push(nextInput);
				
			}
			else {
				String str = tokenContainer.pop();
				postfix = postfix + str + " ";
				tokenContainer.push(nextInput);
			}
		}
	}
	
}
