package test;

public class TestFileName {
	public static void main (String[]a){
		String fileName = "ddopghkpglqgmmwscplyccresmosdh.doc";
		System.out.println(fileName.lastIndexOf("."));
		System.out.println(fileName.substring(0,fileName.lastIndexOf(".")));
	}
}
