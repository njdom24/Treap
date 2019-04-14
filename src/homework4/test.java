package homework4;

public class test
{
	public static void main(String[] args) {

		var testTree = new Treap < Integer >();
		testTree.add (4 ,81);
		testTree.add (2 ,69);
		testTree.add (6 ,30);
		testTree.add (1 ,16);
		testTree.add (3 ,88);
		testTree.add (5 ,17);
		testTree.add (7 ,74);

		System.out.println(testTree.toString());



	}
}
