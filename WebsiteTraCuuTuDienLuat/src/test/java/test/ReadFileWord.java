package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ReadFileWord {

	public static void main(String[] args) {
		File file = new File("D:\\TuDienPhapLuat-TomTa.doc");
		HWPFDocument document;
		try {
			document = new HWPFDocument (new FileInputStream(file));

			WordExtractor extractor = new WordExtractor(document);
			String[] fileData = extractor.getParagraphText();
			for (int i = 0; i < fileData.length; i++)
			{
				if (fileData[i] != null && !fileData[i].equals("\r\n")){
					int position = fileData[i].indexOf(':');
					if(position!= -1){
						String keySearch = fileData[i].substring(position+1);
						System.out.println(keySearch);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

}
