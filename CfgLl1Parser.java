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
		
		
		for (int i = 0; i < vars.length; i++) {
			variables.add(vars[i]);
		}
		
		for (int i = 0; i < terms.length; i++) {
			terminals.add(terms[i]);
		}
		
		for (int i = 0; i < trans.length; i++) {
			String[] curr = trans[i].split("/");
			String[] varList = curr[1].split(",");
			
			ArrayList<String> init = new ArrayList<String>();
			
			rules.put(curr[0], init);
			
			for (int k = 0; k < varList.length; k++) {
				rules.get(curr[0]).add(varList[k]);
			}
			
		}
		
		for (int i = 0; i < first.length; i++) {
			String[] curr = first[i].split("/");
			String[] varList = curr[1].split(",");
			
			ArrayList<String> init = new ArrayList<String>();
			
			firsts.put(curr[0], init);
			
			for (int k = 0; k < varList.length; k++) {
				firsts.get(curr[0]).add(varList[k]);
			}
			
		}
		
		
		for (int i = 0; i < follow.length; i++) {
			String[] curr = follow[i].split("/");
			String[] varList = curr[1].split("");
			
			ArrayList<String> init = new ArrayList<String>();
			
			follows.put(curr[0], init);
			
			for (int k = 0; k < varList.length; k++) {
				follows.get(curr[0]).add(varList[k]);
			}
			
		}
		
		terminals.add("$");
		
		
	}

	public String parse(String input) {
		constructParsingTable();
		String ConcatedInput = input + "$";
		String[] inputArr = ConcatedInput.split("");
		String finalString = variables.get(0);
		String inputConcat = "";
		boolean keepGoing = true;
		boolean error = false;
		
		pdaStack.push("$");
		
		pdaStack.push(variables.get(0));
		
		
			for (int i = 0; i < inputArr.length; i++) {
				if (! (keepGoing)) {
					break;
				}
				
				while (! (pdaStack.isEmpty())) {
					String popped = pdaStack.pop();
					if (variables.contains(popped)) {
						String inp = parsingTable.get(popped).get(inputArr[i]);
						
						if (! (inp.equals("EMPTY")) && ! (inp.equals("e"))) {
							String[] toPush = inp.split("");
							
							for(int j = toPush.length-1; j >= 0 ; j--) {
								pdaStack.push(toPush[j]);
							}
							
							String toConcat = "";
							for (int z = pdaStack.size()-1; z > 0; z--) {
								String toCompare = pdaStack.get(z);
								if (! (toCompare.equals("e"))) {
									toConcat = toConcat + pdaStack.get(z);
								}
								
							}
							
							finalString = finalString + ";" + inputConcat + toConcat;
							
						}
						
						if(inp.equals("EMPTY")) {
							keepGoing = false;
							error = true;
							break;
						}
						
						
						if(inp.equals("e")) {

							String toConcat = "";
							for (int z = pdaStack.size()-1; z > 0; z--) {
								String toCompare = pdaStack.get(z);
								if (! (toCompare.equals("e"))) {
									toConcat = toConcat + pdaStack.get(z);
								}
								
							}
							
							finalString = finalString + ";" + inputConcat + toConcat;
						}
						

					}
					
				
					
					if (terminals.contains(popped)) {
						if (popped.equals(inputArr[i])) {
							inputConcat = inputConcat + popped;							
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
			
		}
		
		
		if (error) {
			finalString = finalString + ";" + "ERROR";
		}
		
		return finalString;
	}
	
	
	public void constructParsingTable() {
		for (int i = 0; i < variables.size(); i++) {

			HashMap<String, String> terminalHash = new HashMap<String, String>();
			
			for (int j = 0; j < terminals.size(); j++) {
				String var = variables.get(i);
				String ter = terminals.get(j);

				for (int k = 0; k < rules.get(var).size(); k++) {
					
					String derivation = rules.get(var).get(k);
					String firstChar = String.valueOf(derivation.charAt(0));
							
					if (terminals.contains(firstChar)) {
						if (firstChar.equals(ter)) {
							terminalHash.put(ter, derivation);
						}
					}
					
					
					else if (variables.contains(firstChar)) {
						if (firsts.get(firstChar).contains(ter)) {
							terminalHash.put(ter, derivation);
						}
						
						else if(firsts.get(firstChar).contains("e")) {
							if (derivation.length() > 1) {
								for (int l = 1; l < derivation.length(); l++) {
									String nextChar = String.valueOf(derivation.charAt(l));
									
									if(variables.contains(nextChar)) {
										if (firsts.get(nextChar).contains(ter)) {
											terminalHash.put(ter, derivation);
											break;
										}
									}
									
									
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
					
					else if (firstChar.equals("e")) {
						if (follows.get(var).contains(ter)) {
							terminalHash.put(ter, derivation);
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

}




