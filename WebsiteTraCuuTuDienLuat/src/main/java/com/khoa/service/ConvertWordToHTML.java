package com.khoa.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.khoa.entity.WordType;
import com.khoa.ultils.Ultils;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.IURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;

@Service
public class ConvertWordToHTML {

    @Value("${PATH_IMAGES:directoryImagesPath}")
    public String PATH_IMAGES;
    
    public ConvertWordToHTML() {
    }

    private byte[] convertDocToHTML(File file) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, Exception {
        // change the type from XWPFDocument to HWPFDocument
        HWPFDocument hwpfDocument = null;
        FileInputStream fis = new FileInputStream(file);
        POIFSFileSystem fileSystem = new POIFSFileSystem(fis);
        hwpfDocument = new HWPFDocument(fileSystem);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
        // add processDocument method
        wordToHtmlConverter.processDocument(hwpfDocument);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        return out.toByteArray();

    }

    private byte[] convertDocxToHTML(File file) {
        try {
            // 1) Load DOCX into XWPFDocument
            InputStream docx = new FileInputStream(file);
            System.out.println("InputStream" + docx);
            XWPFDocument document = new XWPFDocument(docx);

            // 2) Prepare XHTML options (here we set the IURIResolver to load
            // images from a "word/media" folder)
            XHTMLOptions options = XHTMLOptions.create(); // .URIResolver(new
                                                          // FileURIResolver(new
                                                          // File("word/media")));;

            // Extract image
            String folderChild = Ultils.getName(file.getName());
            File imagesFolder = new File(PATH_IMAGES+File.separator+folderChild);
            
            imagesFolder.mkdirs();
            options.setExtractor(new FileImageExtractor(imagesFolder));
            // URI resolver
            options.URIResolver(new IURIResolver() {
                @Override
                public String resolve(String uri) {
                    return "images/"+folderChild+"/"+uri;
                }
            });
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, out, options);
            return out.toByteArray();
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }
        return null;
    }

    public byte[] convertWordHTML(File file, WordType typeFile) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, Exception {
        if (typeFile.equals(WordType.DOC)) {
            return this.convertDocToHTML(file);
        } else if (typeFile.equals(WordType.DOCX)) {
            return this.convertDocxToHTML(file);
        } else
            return null;
    }

    private String[] getArrayParagrapFormDoc(File file) throws FileNotFoundException, Exception {
        HWPFDocument document;
        WordExtractor extractor = null;
        document = new HWPFDocument(new FileInputStream(file));
        extractor = new WordExtractor(document);
        String[] fileData = extractor.getParagraphText();
        return fileData;
    }

    private String[] getArrayParagrapFormDocx(File file) throws FileNotFoundException, Exception {
        String[] fileData = null;
        List<String> list = new ArrayList<String>();
        List<XWPFParagraph> paragraphs;
        XWPFDocument document = new XWPFDocument(new FileInputStream(file));
        paragraphs = document.getParagraphs();
        for (XWPFParagraph para : paragraphs) {
            list.add(para.getText());
        }
        fileData = new String[list.size()];
        fileData = list.toArray(fileData);
        return fileData;
    }

    public String[] getArrayParagrap(File file, WordType wordType) throws FileNotFoundException, Exception {
        if (wordType.equals(WordType.DOC)) {
            return this.getArrayParagrapFormDoc(file);
        } else if (wordType.equals(WordType.DOCX)) {
            return this.getArrayParagrapFormDocx(file);
        }
        return null;
    }
}
