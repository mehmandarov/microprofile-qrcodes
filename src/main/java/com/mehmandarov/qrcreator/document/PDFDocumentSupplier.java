package com.mehmandarov.qrcreator.document;

import com.google.zxing.WriterException;
import com.mehmandarov.qrcreator.qr.QRCodeSupplier;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;


public class PDFDocumentSupplier {

    @Inject
    QRCodeSupplier qrCodeSupplier;


    public byte[] pdfDocumentGenerator(String id) throws NoSuchAlgorithmException, WriterException, InvalidKeySpecException, IOException {
        String headerTitle = id;
        PDFont headerFont = PDType1Font.COURIER_BOLD;
        int marginTop = 30;
        int fontSize = 30;

        PDDocument document = new PDDocument();

        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle mediaBox = page.getMediaBox();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page,  PDPageContentStream.AppendMode.APPEND, true);

        // calculate coordinates to center the header text
        float titleWidth = headerFont.getStringWidth(headerTitle) / 1000 * fontSize;
        float titleHeight = headerFont.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
        float titleStartX = (mediaBox.getWidth() - titleWidth) / 2;
        float titleStartY = mediaBox.getHeight() - marginTop - titleHeight;

        // add header text to the document
        contentStream.beginText();
        contentStream.setFont(headerFont, fontSize);
        contentStream.newLineAtOffset(titleStartX, titleStartY);
        contentStream.showText(headerTitle);
        contentStream.endText();

        // get image as a byte array
        ByteArrayInputStream bais = new ByteArrayInputStream(qrCodeSupplier.qrCodeGenerator(id));
        BufferedImage bim = ImageIO.read(bais);

        // calculate coordinates to center the insert
        PDImageXObject pdImage = LosslessFactory.createFromImage(document, bim);
        float scale = 1f;
        int imageOffset = 100;
        float imageWidth = pdImage.getWidth() * scale;
        float imageHeight = pdImage.getHeight() * scale;
        float imageStartX = (mediaBox.getWidth() - imageWidth) / 2;
        float imageStartY = titleStartY - imageHeight - imageOffset;

        // add image into the document
        contentStream.drawImage(pdImage, imageStartX, imageStartY, pdImage.getWidth() * scale, pdImage.getHeight() * scale);

        contentStream.close();

        // add metadata
        document.getDocumentInformation().setTitle("Generated QR code for " + id + ".");
        document.getDocumentInformation().setSubject("with a secure string");
        document.getDocumentInformation().setAuthor("rm");
        document.getDocumentInformation().setCreator("rm");

        // save and close document
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

}
