package com.mehmandarov.qrcreator.document;

import com.google.zxing.WriterException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.mehmandarov.qrcreator.qr.QRCodeSupplier;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class PDFDocumentSupplier {

    @Inject
    QRCodeSupplier qrCodeSupplier;

    public byte[] pdfDocumentGenerator(String id) throws DocumentException, NoSuchAlgorithmException, WriterException, InvalidKeySpecException, IOException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();
        Font bigFont = FontFactory.getFont(FontFactory.COURIER, 30, Font.BOLD, BaseColor.BLACK);

        // create title contents
        Paragraph title = new Paragraph();
        title.setSpacingAfter(100);
        title.setSpacingBefore(150);
        title.setAlignment(Element.ALIGN_CENTER);
        title.add(new Chunk(id, bigFont));

        // create QR code contents
        Paragraph qrCodeImg = new Paragraph();
        Image img = Image.getInstance(qrCodeSupplier.qrCodeGenerator(id));
        img.scalePercent(70);
        img.setAlignment(Image.MIDDLE);
        qrCodeImg.add(img);

        // add metadata
        document.addTitle("Generated QR code for " + id + ".");
        document.addSubject("with a secure string");
        document.addAuthor("rm");
        document.addCreator("rm");
        document.addCreationDate();

        // add contents to the document
        document.add(title);
        document.add(qrCodeImg);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

}
