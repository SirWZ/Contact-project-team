package org.apache.tika.parser.pdf;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class PDFParserTest {
    @Test
    public void test() throws IOException {
        PDDocument document = PDDocument.load(new File("C:\\Users\\sun\\Desktop\\杂七杂八\\Google三篇论文\\Google-MapReduce中文版_1.0.pdf"));
        Splitter splitter = new Splitter();

        splitter.setStartPage(1);
        splitter.setEndPage(document.getNumberOfPages());

        List<PDDocument> documents = splitter.split(document);
        PDFTextStripper textStripper = new PDFTextStripper();
        ;
        for (int i = 0; i < documents.size(); i++) {
            System.out.println(String.format("Page:%d", i));
            PDDocument doc = documents.get(i);
            String text = textStripper.getText(doc);
            System.out.println(text);
        }
    }
}