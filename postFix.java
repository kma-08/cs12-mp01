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
