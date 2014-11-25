import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/*
 * @ Team members 
 * ARJUN GOPAL  --- axg145630
 * ASHA MARY THOMAS -- axt143530
 * PAVAN KULKARNI --- pxk142330
 */

/*
 * Class for structure of a node
 */
class LinkedNode {
	long data;
	LinkedNode next;

	LinkedNode(long data) {
		this.data = data;
		this.next = null;
	}
}

/*
 * Class describing structure of an operand Operand contains: 1. name of the
 * operand 2. value assigned which is stored as a linked list
 */
class Operand {
	String name;
	LinkedNode value;

	public String getName() {
		return name;
	}

	Operand(String name, LinkedNode value) {
		this.name = name;
		this.value = value;

	}

}

// 1.Assign Value
// 2.Operations
// 3.ConditionalCheck
// 4. Assigning Operand
// 5. Print
class Expression {
	int id;
	int type;
	Operand operandA;
	Operand operandB;
	Operand operandC;
	int operator;
	boolean signBit;

	Expression(int id, int type, Operand operandA, Operand operandB,
			Operand operandC, int operator) {

		this.id = id;
		this.type = type;
		this.operandA = operandA;
		this.operandB = operandB;
		this.operandC = operandC;
		this.operator = operator;

	}
}

// Main Class
public class AXG145630_Project4 {

	static List<Expression> expressionList;
	static Map<String, Operand> operandMap;
	static long base = 10;
	static LinkedNode base10Rep;
	static String base10Result = "";

	private static LinkedNode createBase10ResultNode(LinkedNode node) {
		if (node == null)
			return null;

		node = reverseLinkedList(node);
		LinkedNode tmpNode = node;
		while (node != null) {
			long carry = node.data;
			LinkedNode base10Node = base10Rep;
			while (base10Node != null) {
				long tmp = base10Node.data * base + carry;
				base10Node.data = tmp % 10;
				carry = tmp / 10;
				if (base10Node.next == null && carry > 0) {
					base10Node.next = new LinkedNode(0);

				}
				base10Node = base10Node.next;
			}
			node = node.next;
		}
		return reverseLinkedList(tmpNode);

	}

	public static LinkedNode reverseLinkedList(LinkedNode node) {
		LinkedNode current = node;
		LinkedNode prev = null;
		LinkedNode next;
		while (current != null) {
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;

		}
		return prev;
	}

	private static String printReverse(LinkedNode n) {
		if (n == null)
			return "0";
		String value = "";

		LinkedNode prev = null;
		LinkedNode current = n;
		LinkedNode next;
		while (current != null) {
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		n = prev;

		prev = null;
		current = n;
		while (current != null) {
			value = value + current.data;
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		n = prev;

		return value;
	}

	public static String NumToStr(LinkedNode node) {
		base10Rep = new LinkedNode(0);
		node = createBase10ResultNode(node);
		return printReverse(base10Rep);

	}

	/*
	 * MAIN DRIVER FUNCTION -------------------- READS INPUT FROM STDIN IN THE
	 * FOLLOWING FORM
	 * 
	 * INPUT - (String) -------------- var=NumberInDecimal # sets x to be that
	 * number var=var+var # add two numbers and assign to var on left
	 * var=var*var # product of two numbers var=var-var # difference of two
	 * numbers var=var^var # power var # print the value of the variable to
	 * stdout var?LineNumber # if var value is not 0, then go to Line number
	 * 
	 * 
	 * INPUT IS MULTILINE ------------------ (Maximum is 1000 lines)
	 * 
	 * OUTPUT -(String) ------ Expressions evaluated
	 */
	public static void main(String[] args) throws InterruptedException {

		if (args.length != 0) {
			base = Integer.parseInt(args[0]);
		}

		Scanner sc = new Scanner(System.in);
		// Regular expression to separate operand and operators
		String regex = "(?<=op)|(?=op)".replace("op", "[-+*/=^?]");

		expressionList = new ArrayList<Expression>();
		operandMap = new HashMap<String, Operand>();

		while (true) {
			int index = sc.nextInt(); // read index
			if (index == 1001)
				break;
			String expression = sc.next(); // read expression

			// split expression into array based on defined regular expression
			String[] splittedExpression = expression.split(regex);

			Expression expressionNode = createExpression(index,
					splittedExpression);

			expressionList.add(expressionNode);

		}
		int k = 1;

		while (k <= expressionList.size()) {
			Expression expression = expressionList.get(k - 1);
			// type 3 ==> conditional looping
			if (expression.type == 3) {
				if (expression.operandA.value != null) {
					k = expression.operator;
					continue;
				} else {
					k++;
					continue;
				}

			}
			evaluateExpression(expression);
			k++;
		}
	}

	/*
	 * Evaluate and perform the action specified in the expression
	 * 
	 * @ param expressionNode ----- expression to be evaluated
	 */

	private static void evaluateExpression(Expression expressionNode) {

		// if expression type is 4 or 1, assign the right side operand value to
		// left side operand value
		if (expressionNode.type == 4 || expressionNode.type == 1) {
			expressionNode.operandA.value = expressionNode.operandB.value;
		}

		// if type=2, perform arithmetic operation specified by the operator
		// value
		if (expressionNode.type == 2) {

			// operator=1 => addition
			if (expressionNode.operator == 1) {
				expressionNode.operandA.value = addToLinkedList(
						expressionNode.operandB.value,
						expressionNode.operandC.value);
			}
			// operator=2 ==> subtraction
			if (expressionNode.operator == 2) {
				expressionNode.operandA.value = subtractTwoLinkedLists(
						expressionNode.operandB.value,
						expressionNode.operandC.value);
			}

			// operator=3 ==>multiplication
			if (expressionNode.operator == 3) {
				expressionNode.operandA.value = multiplyTwoLinkedLists(
						expressionNode.operandB.value,
						expressionNode.operandC.value);
			}

			// operator=4 ==>power
			if (expressionNode.operator == 4) {
				expressionNode.operandA.value = powerOfTwoLinkedLists(
						expressionNode.operandB.value,
						expressionNode.operandC.value);
			}
		}

		// type 5 ==>print the value of the operand
		if (expressionNode.type == 5) {

			if (expressionNode.operandA.value == null)
				System.out.println("0");
			else {
				System.out.println(NumToStr(expressionNode.operandA.value));
			}
		}

	}

	/*
	 * ADD --- Takes 2 lists as parameter adds them and returns the result
	 * 
	 * @param nodeA ----------- list 1
	 * 
	 * @param nodeB ----------- list 2
	 * 
	 * @return ----------- result = list1 +list2;
	 */

	private static LinkedNode addToLinkedList(LinkedNode nodeA, LinkedNode nodeB) {
		LinkedNode resultNode = null;
		LinkedNode tmpNode = null;
		LinkedNode prevNode = null;
		long carry = 0, sum;
		long num1 = 0;
		long num2 = 0;

		while (nodeA != null || nodeB != null) {
			if (nodeA != null) {
				num1 = nodeA.data;
				nodeA = nodeA.next;
			} else
				num1 = 0;

			if (nodeB != null) {
				num2 = nodeB.data;
				nodeB = nodeB.next;
			} else
				num2 = 0;

			sum = (num1 + num2 + carry) % base;
			carry = (num1 + num2 + carry) / base;
			tmpNode = new LinkedNode(sum);
			if (resultNode == null)
				resultNode = tmpNode;
			else
				prevNode.next = tmpNode;
			prevNode = tmpNode;

		}

		if (carry != 0) {
			tmpNode = new LinkedNode(carry);
			prevNode.next = tmpNode;
		}

		return resultNode;
	}

	/*
	 * SUBTRACT -------- Takes 2 list's as parameters and calculates the
	 * difference between them ;
	 * 
	 * @param nodeA --------------- list 1
	 * 
	 * @param nodeB --------------- list 2
	 * 
	 * @return --------------- result = list1- list2; if result is less then
	 * zero , zero is returned
	 */
	private static LinkedNode subtractTwoLinkedLists(LinkedNode nodeA,
			LinkedNode nodeB) {
		LinkedNode resultNode = null;
		LinkedNode tmpNode = null;
		LinkedNode prevNode = null;
		long carry = 0, sub;
		long num1 = 0;
		long num2 = 0;

		while (nodeA != null) {
			num1 = nodeA.data;
			nodeA = nodeA.next;
			if (nodeB != null) {
				num2 = nodeB.data;
				nodeB = nodeB.next;
			} else
				num2 = 0;
			sub = num1 - num2 - carry;
			if (sub < 0) {
				carry = 1;
				sub += base;
			} else
				carry = 0;
			if (nodeA == null && sub == 0)
				break;
			tmpNode = new LinkedNode(sub);
			if (resultNode == null)
				resultNode = tmpNode;
			else
				prevNode.next = tmpNode;
			prevNode = tmpNode;

		}
		if (nodeB != null || carry != 0)
			return null;

		boolean isZeroNode = true;
		LinkedNode testNode = resultNode;
		while (testNode != null) {
			if (testNode.data != 0)
				isZeroNode = false;
			testNode = testNode.next;
		}

		if (isZeroNode)
			return null;
		return resultNode;
	}

	/*
	 * POWER ----- Takes 2 lists as parameters returns l1 raised to l2 as result
	 * l1^l2
	 * 
	 * @param nodeA ------ list1
	 * 
	 * @param nodeB ------ list2
	 * 
	 * @return ------ result(nodeA^nodeB)
	 */
	private static LinkedNode powerOfTwoLinkedLists(LinkedNode nodeA,
			LinkedNode nodeB) {

		LinkedNode p = StrToNum("1");
		LinkedNode n = StrToNum("1");
		LinkedNode tmp = nodeB;

		if (tmp == null)
			return p;
		// If the second number is 0, then we have to return 1.
		if (tmp.next == null && tmp.data == 0)
			return p;
		while (tmp != null) {
			p = multiplyTwoLinkedLists(p, nodeA);
			tmp = subtractTwoLinkedLists(tmp, n);

		}

		return p;
	}

	/*
	 * CREATE EXPRESSION -----------------------
	 * 
	 * @param index --------------- line Number
	 * 
	 * @param splittedExpression --------------- Array of operands
	 * 
	 * @return --------------- expression(splittedExpression)
	 */

	private static Expression createExpression(int index,
			String[] splittedExpression) {
		Operand operandA;
		Operand operandB;
		Operand operandC;
		LinkedNode linkedNode;
		// If there is just one variable in the expression then print the
		// variable
		if (splittedExpression.length == 1) {
			if (operandMap.get(splittedExpression[0]) != null) {
				operandA = operandMap.get(splittedExpression[0]);
				return new Expression(index, 5, operandA, null, null, 0);
			} else {
				System.out.println("Variable Not Declared");
			}

		}
		// If an Assignment or Conditional Expression
		if (splittedExpression.length == 3) {
			if (splittedExpression[1].equals("=")) {
				if (operandMap.containsKey(splittedExpression[2])) {
					operandB = operandMap.get(splittedExpression[2]);
					if (operandMap.containsKey(splittedExpression[0])) {
						operandA = operandMap.get(splittedExpression[0]);

					} else {
						operandA = new Operand(splittedExpression[0],
								operandB.value);
						operandMap.put(operandA.name, operandA);
					}
					return (new Expression(index, 1, operandA, operandB, null,
							0));

				} else {
					linkedNode = StrToNum(splittedExpression[2]);
					operandB = new Operand(splittedExpression[2], linkedNode);
					if (operandMap.containsKey(splittedExpression[0])) {
						operandA = operandMap.get(splittedExpression[0]);
					} else {
						operandA = new Operand(splittedExpression[0],
								operandB.value);
						operandMap.put(operandA.name, operandA);
					}
					return (new Expression(index, 4, operandA, operandB, null,
							0));
				}

			} else if (splittedExpression[1].equals("?")) {

				if (operandMap.containsKey(splittedExpression[0])) {
					return (new Expression(index, 3,
							operandMap.get(splittedExpression[0]), null, null,
							Integer.parseInt(splittedExpression[2])));
				} else {
					System.out.println("Variable Not declared");
				}

			} else {
				System.out.println("Invalid Expression");
			}
		}

		// If an Operation
		if (splittedExpression.length == 5) {
			int operator = 0;
			if (operandMap.containsKey(splittedExpression[0])) {
				operandA = operandMap.get(splittedExpression[0]);
			} else {
				operandA = new Operand(splittedExpression[0], null);
				operandMap.put(operandA.name, operandA);
			}
			operandB = operandMap.get(splittedExpression[2]);
			operandC = operandMap.get(splittedExpression[4]);
			if (operandB == null || operandC == null) {
				System.out.println("Operand(s) not declared");
				return null;
			}
			if (splittedExpression[3].equals("+")) {
				operator = 1;
			} else if (splittedExpression[3].equals("-")) {
				operator = 2;
			} else if (splittedExpression[3].equals("*")) {
				operator = 3;
			} else if (splittedExpression[3].equals("^")) {
				operator = 4;
			} else {
				System.out.println("Invalid Operator");
				return null;
			}
			return (new Expression(index, 2, operandA, operandB, operandC,
					operator));
		}

		return null;
	}

	private static LinkedNode StrToNum(String string) {

		LinkedNode headNode = new LinkedNode(0);

		for (int i = 0; i < string.length(); i++) {
			long carry = string.charAt(i) - '0';
			LinkedNode node = headNode;
			while (node != null) {

				long tmp = node.data * 10 + carry;
				node.data = tmp % base;
				carry = tmp / base;
				if (node.next == null && carry > 0) {
					node.next = new LinkedNode(0);

				}
				node = node.next;
			}

		}
		boolean isZeroNode = true;
		LinkedNode testNode = headNode;
		while (testNode != null) {
			if (testNode.data != 0)
				isZeroNode = false;
			testNode = testNode.next;
		}

		if (isZeroNode)
			return null;

		return headNode;
	}

	/*
	 * MULTIPLY ----------
	 * 
	 * @param nodeA -------------- MULTIPLICAND
	 * 
	 * @param nodeB -------------- MULTIPLIER
	 * 
	 * @return -------------- LIST(Containing product of list l1 and list l2)
	 * 
	 * Algorithm - High school Multiplication Algorithm ,At each step calculate
	 * partial product and add it finally.
	 */
	private static LinkedNode multiplyTwoLinkedLists(LinkedNode nodeA,
			LinkedNode nodeB) {

		LinkedNode first = nodeA;
		LinkedNode second = nodeB;
		LinkedNode tmpNode = null;
		LinkedNode result = null;
		long multiplier;
		long count = 0;

		while (second != null) {
			multiplier = second.data;
			second = second.next;

			if (count == 0) {
				result = multiplySingleRow(first, multiplier, result);
				count = 1;
			} else {
				tmpNode = multiplySingleRow(first, multiplier, null);
				tmpNode = prependTen(tmpNode, count);
				result = addToLinkedList(result, tmpNode);
				count++;
			}
		}

		return result;

	}

	/*
	 * UTILITY FOR MULTIPLY FUNCTION ------------------------------------
	 * Prepend zeros to the linked list. Number of zeros depends on the count.
	 */
	private static LinkedNode prependTen(LinkedNode node, long count) {
		LinkedNode tmpNode;
		LinkedNode newNode = null;

		if (node == null) {
			return null;
		}
		tmpNode = node;

		while (count > 0) {
			newNode = new LinkedNode(0);
			newNode.next = tmpNode;
			tmpNode = newNode;
			count--;
		}
		node = tmpNode;

		return node;

	}

	/*
	 * UTILITY FOR MULTIPLY FUNCTION ------------------------------------
	 * Multiply a linked list with one integer and return the result.
	 */
	private static LinkedNode multiplySingleRow(LinkedNode p1, long multiplier,
			LinkedNode result) {
		long carry = 0;
		long product;
		LinkedNode p = p1;
		LinkedNode tmpNode;
		tmpNode = result;

		while (p != null) {
			product = p.data * multiplier + carry;

			if (tmpNode == null) {
				tmpNode = new LinkedNode(product % base);
				result = tmpNode;
			} else {
				tmpNode.next = new LinkedNode(product % base);
				tmpNode = tmpNode.next;

			}
			carry = product / base;
			p = p.next;
		}
		if (carry > 0) {
			tmpNode.next = new LinkedNode(carry);
			tmpNode = tmpNode.next;
		}

		return result;
	}

}
