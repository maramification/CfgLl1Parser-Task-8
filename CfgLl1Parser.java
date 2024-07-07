package csen1002.main.task8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Write your info here
 * 
 * @name Maram Hossam El-Deen Mohamed
 * @id 49-1891
 * @labNumber 18
 */

public class CfgLl1Parser {

	private ArrayList<String> variables = new ArrayList<String>();
	private ArrayList<String> terminals = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> rules = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> firsts = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> follows = new HashMap<String, ArrayList<String>>();
	private HashMap<String, HashMap<String, String>> parsingTable = new HashMap<String, HashMap<String, String>>();
	private Stack<String> pdaStack = new Stack<String>();
	
	public CfgLl1Parser(String input) {
		String[] temp = input.split("#");
		String[] vars = temp[0].split(";");
		String[] terms = temp[1].split(";");
		String[] trans = temp[2].split(";");
		String[] first = temp[3].split(";");
		String[] follow = temp[4].split(";");
		
		
		//adding the variables
		for (int i = 0; i < vars.length; i++) {
			variables.add(vars[i]);
		}
		
		//adding terminals
		for (int i = 0; i < terms.length; i++) {
			terminals.add(terms[i]);
		}
		
		//adding rules
		for (int i = 0; i < trans.length; i++) {
			String[] curr = trans[i].split("/");
			String[] varList = curr[1].split(",");
			
			ArrayList<String> init = new ArrayList<String>();
			
			//adding the key variable
			rules.put(curr[0], init);
			
			for (int k = 0; k < varList.length; k++) {
				rules.get(curr[0]).add(varList[k]);
			}
			
		}
		
		//adding firsts
		for (int i = 0; i < first.length; i++) {
			String[] curr = first[i].split("/");
			String[] varList = curr[1].split(",");
			
			ArrayList<String> init = new ArrayList<String>();
			
			//adding the key variable
			firsts.put(curr[0], init);
			
			for (int k = 0; k < varList.length; k++) {
				firsts.get(curr[0]).add(varList[k]);
			}
			
		}
		
		
		//adding follows
		for (int i = 0; i < follow.length; i++) {
			String[] curr = follow[i].split("/");
			String[] varList = curr[1].split("");
			
			ArrayList<String> init = new ArrayList<String>();
			
			//adding the key variable
			follows.put(curr[0], init);
			
			for (int k = 0; k < varList.length; k++) {
				follows.get(curr[0]).add(varList[k]);
			}
			
		}
		
		terminals.add("$");
		
		
	}

	/**
	 * @param input The string to be parsed by the LL(1) CFG.
	 * 
	 * @return A string encoding a left-most derivation.
	 */
	public String parse(String input) {
		constructParsingTable();
		String ConcatedInput = input + "$";
		String[] inputArr = ConcatedInput.split("");
		String finalString = variables.get(0);
		String inputConcat = "";
		boolean keepGoing = true;
		boolean error = false;
		
		//push $ into stack
		pdaStack.push("$");
		
		//push start variable into stack
		pdaStack.push(variables.get(0));
//		System.out.println("STARTING VAR: " + variables.get(0));
		
		
			for (int i = 0; i < inputArr.length; i++) {
				if (! (keepGoing)) {
					break;
				}
				
				while (! (pdaStack.isEmpty())) {
//					System.out.println("STACK IS NOT EMPTY...");
//					System.out.println("CURRENT INPUT CHAR..." + inputArr[i]);
					String popped = pdaStack.pop();
					if (variables.contains(popped)) {
//						System.out.println("Variable " + popped + " is popped...");
						String inp = parsingTable.get(popped).get(inputArr[i]);
						
						if (! (inp.equals("EMPTY")) && ! (inp.equals("e"))) {
							//finalString = finalString + ";" + inp;
//							System.out.println("Parsing sub is..." + inp);
							//pop first var and push its equivalent parsing			
							String[] toPush = inp.split("");
							
							for(int j = toPush.length-1; j >= 0 ; j--) {
								pdaStack.push(toPush[j]);
//								System.out.println("TO PUSH..." + toPush[j]);
								
//								System.out.println("Stack contents:");
//						        for (String element : pdaStack) {
////						            System.out.println(element);
//						        }
							}
							
							//add to the final string the stack contents
//							if(! (inputConcat.equals(""))) {
//								finalString = finalString + ";" + inputConcat;
//							}

							//loop over all stack until u reach the last element ($)
							String toConcat = "";
							for (int z = pdaStack.size()-1; z > 0; z--) {
//								System.out.println("CURRENT CHAR..." + pdaStack.get(z));
								String toCompare = pdaStack.get(z);
								if (! (toCompare.equals("e"))) {
									toConcat = toConcat + pdaStack.get(z);
								}
								
							}
							
							finalString = finalString + ";" + inputConcat + toConcat;
//							System.out.println("FINAL STRING: " + finalString);
							
						}
						
						if(inp.equals("EMPTY")) {
//							finalString = finalString + ";" + "ERROR";
							keepGoing = false;
							error = true;
							break;
						}
						
						
						//if inp is epsilon
						if(inp.equals("e")) {
//							System.out.println("EPSILON PUSH");
							//make sure en mafish input strip to concat before
//							if(! (inputConcat.equals(""))) {
//								finalString = finalString + ";" + inputConcat;
//							}

							//loop over all stack until u reach the last element ($)
							String toConcat = "";
							for (int z = pdaStack.size()-1; z > 0; z--) {
//								System.out.println("CURRENT CHAR..." + pdaStack.get(z));
								String toCompare = pdaStack.get(z);
								if (! (toCompare.equals("e"))) {
									toConcat = toConcat + pdaStack.get(z);
								}
								
							}
							
							finalString = finalString + ";" + inputConcat + toConcat;
//							System.out.println("FINAL STRING: " + finalString);
						}
						

					}
					
				
					
					//if what is popped is a terminal
					if (terminals.contains(popped)) {
						if (popped.equals(inputArr[i])) {
							inputConcat = inputConcat + popped;
//							System.out.println("INPUTCONCAT..." + inputConcat);
							
							break;
						}
						
						else {
							keepGoing = false;
							error = true;
							break;
						}
					}

				}

					
					
					

			}
			
			
		if ((pdaStack.size() > 1)) {
			error = true;
//			for (String element : pdaStack) {
////	            System.out.println(element);
//	        }
			
		}
		
		
		if (error) {
			finalString = finalString + ";" + "ERROR";
		}
		
//		System.out.println("FINAL STRING: " + finalString);
		return finalString;
	}
	
	
	public void constructParsingTable() {
		//add variables
		for (int i = 0; i < variables.size(); i++) {

			HashMap<String, String> terminalHash = new HashMap<String, String>();
			
			for (int j = 0; j < terminals.size(); j++) {
				String var = variables.get(i);
				String ter = terminals.get(j);
				
				
				//checking the firsts
				//looping over derivations
				for (int k = 0; k < rules.get(var).size(); k++) {
					
					String derivation = rules.get(var).get(k);
					String firstChar = String.valueOf(derivation.charAt(0));
							
				//if it is a terminal
					if (terminals.contains(firstChar)) {
						if (firstChar.equals(ter)) {
							terminalHash.put(ter, derivation);
//							parsingTable.put(var, terminalHash);
						}
					}
					
					
				//if it is a variable
					else if (variables.contains(firstChar)) {
						//checking its first
						if (firsts.get(firstChar).contains(ter)) {
							terminalHash.put(ter, derivation);
//							System.out.println("TERM IS: " + ter);
//							System.out.println("DERIVATION IS: " + derivation);
//							parsingTable.put(var, terminalHash);
						}
						
						//checking if first contains epsilon and then checking if follow contains the terminal
						else if(firsts.get(firstChar).contains("e")) {
							//loop through derivation
							if (derivation.length() > 1) {
								for (int l = 1; l < derivation.length(); l++) {
									//if the next char's firsts contain the terminal
									String nextChar = String.valueOf(derivation.charAt(l));
									
									//if next char is a variable
									if(variables.contains(nextChar)) {
										if (firsts.get(nextChar).contains(ter)) {
											terminalHash.put(ter, derivation);
											break;
										}
										//else habos ala el ganbo until i hit a terminal
									}
									
									
									//if next char is a terminal
									else if (terminals.contains(nextChar)) {
										if (nextChar.equals(ter)) {
											terminalHash.put(ter, derivation);
											
								}
										break;
									}
																
							}
							}
							
					}
				}
					
					//if it is epsilon
					else if (firstChar.equals("e")) {
						if (follows.get(var).contains(ter)) {
							terminalHash.put(ter, derivation);
//							parsingTable.put(var, terminalHash);
						}
						
					}
					if (! (terminalHash.containsKey(ter))) {
						terminalHash.put(ter, "EMPTY");
					}
			}
			
			parsingTable.put(variables.get(i), terminalHash);
		}
		}
		
	}
	
	
//	public static void main(String[] args) { 
////		String test = "S;A;B#a;b;c;d#S/AaS,d;A/BbBaSc,e;B/e#S/ab,d;A/b,e;B/e#S/$c;A/a;B/ab";
////		String test2 = "S;T#a;c;i#S/iST,e;T/cS,a#S/i,e;T/c,a#S/$ac;T/$ac";
////		String test3 = "S;I;O;W;J#a;i;j;n;p;q;r#S/rJ,JW;I/J,iI,e;O/iIj,j,e;W/Jp,pO,nSr,e;J/q,a#S/r,aq;I/aq,i,e;O/i,j,e;W/aq,p,n,e;J/q,a#S/$r;I/j;O/$r;W/$r;J/$ajnpqr";
////		CfgLl1Parser cfg = new CfgLl1Parser(test3);
////		cfg.parse("apiqnrnj");
//		
////		
////		System.out.println("VARIABLES: " + cfg.variables);
////		System.out.println("TERMINALS: " + cfg.terminals);
////		
////		System.out.println("RULES:");
////      for (Entry<String, ArrayList<String>> entry : cfg.rules.entrySet()) {
////          System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
////      }
////      
////      
////		System.out.println("FIRSTS:");
////	      for (Entry<String, ArrayList<String>> entry : cfg.firsts.entrySet()) {
////	          System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
////	      }
////	      
////	      
////			System.out.println("FOLLOWS:");
////		      for (Entry<String, ArrayList<String>> entry : cfg.follows.entrySet()) {
////		          System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
////		      }
////		      
////		      System.out.println("PARSING TABLE:");
////		      for (Map.Entry<String, HashMap<String, String>> entry : cfg.parsingTable.entrySet()) {
////		            String key = entry.getKey();
////		            HashMap<String, String> innerMap = entry.getValue();
////		            System.out.println("Key: " + key);
////		            System.out.println("Value: " + innerMap);
////		        }
//		
//		
//	}


}




