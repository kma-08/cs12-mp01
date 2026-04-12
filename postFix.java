import java.util.Stack;
import java.util.EmptyStackException;
import java.lang.ArrayIndexOutOfBoundsException;


 //String operators = "^*/%+-";
 //String numbers = ".0123456789";
//beh di ko napansin kanina hindi to pwedeng instance variables dahil hindi siya state considered sha as bad practice,
//so kahit pwede mo siyang gawin, hindi siya pasok sa oop

//nahandle ba natin yung kapag may inenter kunyari na $% unknown na characters in general?
/*
 problem pa
 if yung testdrive ng user is sunod sunod na
 like
 postFix myconverter = new postFix();
		
		myconverter.setInfix("1.2+(2.5-3.567)*4.8901ˆ2");
		
		System.out.println(myconverter.getInfix());
		
		System.out.println(myconverter.infix2postfix());
		
		System.out.println(myconverter.showPostfix());
 lahat yan maglalagay ng error. So ang ginawa ko naglagay na lang ako return message pag hindi naset nang ayos yung infix. di ko alam pano modify yung sa evaluate
 Mga di pa gumagana:
 1.2+(2.5-3.(5)67)*4.8901^2
 1.2+(2.5-3.567)(*)4.8901^2
 
 Iniisip ko kung dapat sa getInfix ba return ng error msg or dapat sa set pa lang mag return na 
 
 After natin matapos, ayusin natin readability tsaka add helpful comments
 */

//1. Java Class named postFix
public class postFix {
	private Stack<Character> tokenContainer;//1. Empty stack of operators
	private String infix; //input
	private String postfix; //output
	private boolean errorDetected = false; //error checker
	private String errorMsg = ""; //output error
	private int ctr; //represents the current index
	

	//2. setInfix(String)
	public String setInfix(String infix) { 
		errorMsg = errorChecking(infix); //if walang error, mag rreturn ng null string, if may error yung errorMsg ibabalik
		
		if(errorMsg.equals("")) { //if null string, ibig sabihin walang error, pwede na iassign yung infix na pinasok
			this.infix = infix;
		}
		else {
			errorDetected = true; //nilagay ko pa rin para hindi mainvoke yung infix2postfix
		}
		return errorMsg; //regardless if errorMsg or null string, ireturn pa rin since if null string wla lang naman un
	}
	//3. getInfix()
	public String getInfix() { 
		if(errorDetected == false) {
			return this.infix;
		}
		else return errorMsg;
	}
	
	//4. infix2postfix()
	public String infix2postfix(){ 
		
		if(errorDetected) {
			return "You can't invoke this method yet. Set your infix properly.";
		}
		
		tokenContainer = new Stack<>(); 
		postfix = "";
		String operators = "^*/%+-";
		String nums = ".,0123456789";
		ctr=0;
		
		//Note that the numbering indicated in this part are based on the provided general algorithm of converting an infix expression to a postfix expression.
		
		while(errorDetected == false && ctr < infix.length()) { //2, NOTE: d q pah sure pano iimplement yung while no error 
			
			//2.a
			Character nextInput = infix.charAt(ctr); 
			
			if(Character.isWhitespace(nextInput)){
				ctr++;
				continue;
			}
			
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
				/*
				 Kasi ang problem ko rito pede namang yung error checking ng operand is maexecute rin errorChecking method
				 kaso ang original problem nga natin dun ay hindi natin naextract nang ayos yung operand. So ang need gawin:
				 
				 1. Extract muna operand
				 2. Kada operand ichceck.
				 
				 So madali lang naman mag add ng pag extract ng operands, icopypaste ko lang yung nandiro originally.
				 Ang problema ay panget dahil may duplicate code, lalo hahaba. tas naisip ko gagi pede naman nga 
				 kasing gumawa na lang ng method na magbubuild ng operands. So tawag na lang dito tas tawag din sa errorChecking.
				 
				 
				 */
				String operand = buildOperand(infix, nums, ctr);
				postfix += operand + " ";
				ctr += operand.length();
				
			}
			
			else {
			    ctr++; 
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
	
	//5. showPostfix
	public String showPostfix() {
		
		if(errorDetected) {
			return "You can't invoke this method yet. Set your infix properly.";
		}
		
		System.out.println(this.postfix);
		return this.postfix;
	}
	
	//6. evaluatePostfix()
	public double evaluatepostfix() {
		
		//if(errorDetected) {
			//return "You can't invoke this method yet. Set your infix properly.";
		//}
		
		Stack <Double> postContainer = new Stack <>();
		
		String[] tokens = this.postfix.trim().split("\\s+");// yung \s=any whitespace \s+ = split by one or more spaces kaya double \\ kasi special character si \
		
		for (int i = 0; i < tokens.length; i++) {
		    String token = tokens[i];

		    try {
		        double num = Double.parseDouble(token);
		        postContainer.push(num);
		    } catch (NumberFormatException e) {
		        double b = postContainer.pop();
		        double a = postContainer.pop();

		        switch (token) {
		            case "+": postContainer.push(a + b); break;
		            case "-": postContainer.push(a - b); break;
		            case "*": postContainer.push(a * b); break;
		            case "/": postContainer.push(a / b); break;
		            case "%": postContainer.push(a % b); break;
		            case "^": postContainer.push(Math.pow(a, b)); break;
		        }
		    }
		}
		
		return postContainer.pop();
	}
	
	
	
	
	//helper methods
	
	private String buildOperand(String infix, String nums, int startIndex) {
	    StringBuilder cont = new StringBuilder();
	    int i = startIndex;

	    while (i < infix.length() && nums.contains(String.valueOf(infix.charAt(i)))) {
	        cont.append(infix.charAt(i));
	        i++;
	    }

	    return cont.toString();
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
				errorMsg = "Unbalanced Parentheses!"; //need to since ito yung hindi nahandle sa parentheses
			}
		}
	}
	
	private void handleOperators(Character nextInput) { 
		while (!tokenContainer.isEmpty()) {
			Character topOfStack = tokenContainer.peek();
			
			if (topOfStack == '(') break;

		    int topPrec = precedenceChecker(topOfStack);
		    int currPrec = precedenceChecker(nextInput);

		    if (nextInput.equals('^')) {
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
	
	private String errorChecking(String infix) { //since dapat isang method lang to gawin ko na lang na by blocks yung kada error handling
		
		String error = "Error: ";
		String operators = "^*/%+-";
		String nums = ".,0123456789";
		
	//1. Unbalanced parenthesis in the infix expression.
		//by order pala dapat yung pag check dito. So kunyari
		// (8+5)) not valid
		// ((3+3)*5) valid
		// 8+3)(+4 not valid
		//78))+67(( not valid
		/*
		 Yung idea is isa lang kailangan natin na counter: kunyari
		 int ctr = 0;
		 if na encounter si '(' - mag iincrement
		 if na encounter si ')' - mag dedecrement
		 
		 Habang di pa nag eend yung loop,
			 Notice na sa mga valid cases, lagi lang shang positive
			 Pero kapag invalid, nag nenegative sha
			 
		Last check: Kapag nag zero yung ctr, balanced sha. If not zero, unbalanced
		 */
		int counter = 0;

		for (int i = 0; i < infix.length(); i++) {
		    char ch = infix.charAt(i);

		    if (ch == '(') {
		        counter++;
		    }
		    else if (ch == ')') {
		        counter--;

			    if (counter < 0) {
			    	return error + "Unbalanced Parenthesis!";
			    }
		    }
		}
		
		if (counter != 0) {
			return error + "Unbalanced Parenthesis!";
		}
		
	//2. Unknown Operators
		
		for (int i=0; i<infix.length(); i++) {
			char holder = infix.charAt(i);

			if(!nums.contains(String.valueOf(holder)) && holder!= '(' && holder!= ')' && holder!= ' ') {
				
				if(operators.indexOf(holder)== -1) {
					return error + "Unknown Operators!";
				}
			}
	
		}//endfor

	//3. Missing Operands
		
		//need talaga yung StringBuilder dito dahil hindi tlaga siya by operand mag operate, itong naka comment yung original. 
		//Try mo icomment yung may Stringbuilder tas itong original yung iimplement mo
		
		for(int i = 0; i<infix.length()-1; i++) {
			
			if(operators.contains(String.valueOf(infix.charAt(i))) && operators.contains(String.valueOf(infix.charAt(i + 1)))){
				return error + "Missing Operands/s";
			}
		}
		
		for(int i = 0; i<operators.length(); i++) {		
			char holder = operators.charAt(i);
			if(infix.startsWith(String.valueOf(holder)) || infix.endsWith(String.valueOf(holder))) {
				return error + "Missing Operands/s";
			}
		}
	
	//3. Math Errors
		
	//4. Error Handling for Illegal Use of Comma
		int i = 0;
		
		while (i < infix.length()) {
		    char ch = infix.charAt(i);

		    if (nums.contains(String.valueOf(ch))) {
		        String operand = buildOperand(infix, nums, i);

		      //split integer and fractional part
				String[] part = operand.split("\\.");
				
				/*
				 tinry catch ko na lang to since nung tinry ko to:
				 myconverter.setInfix("1(.)2+(2.5-3.567)*4.8901^2");
				 
				 hindi rin daw pwede to String integerPart = part[0]; 
				 nag arrayindex out of bounds
				 */
				try {
					String integerPart = part[0];
					
					//No comma is allowed
					if(!integerPart.contains(",")) {
						if (!integerPart.matches("\\d+")) {
							return error + "Illegal Operand Format!";
						}
					} 
					
					//Split by commas
					String[] integers = integerPart.split(",");
					
					//1-3 digits dapat yung first group
					if(!integers[0].matches("\\d{1,3}")) {
						return error + "Illegal Operand Format!";
					}
					
					//remaining groups dapat exactly 3 digits
					
					for (int j = 1; j<integers.length; j++) {
						if (!integers[j].matches("\\d{3}")) {
							return error + "Illegal Operand Format!";
						}
					}

			        i += operand.length(); //para the next i na gamitin natin will really move to the next operand, hindi lang basta sa next na character
				}
				catch(ArrayIndexOutOfBoundsException e) {
					return error + "Illegal Operand Format!";
				}
				if (part.length > 1) { 
				    String fracPart = part[1];
				    if (!fracPart.matches("\\d*")) {
				    	return error + "Illegal Operand Format!";
				    }
				}
		    }
		    else {
		        i++;
		    }
		}
		return "";
	}

	
	
}
