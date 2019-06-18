package com.mehmandarov.qrcreator;

import com.google.zxing.WriterException;
import com.mehmandarov.qrcreator.document.PDFDocumentSupplier;
import com.mehmandarov.qrcreator.qr.QRCodeContentsSupplier;
import com.mehmandarov.qrcreator.qr.QRCodeSupplier;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 *
 */
@Path("/qr")
@Singleton
public class QRController {

    @Inject
    QRCodeSupplier qrCodeSupplier;

    @Inject
    QRCodeContentsSupplier qrCodeContentsSupplier;

    @Inject
    PDFDocumentSupplier pdfDocumentSupplier;

    @GET
    @Path("{id}/json")
    @Produces("application/json")
    @Operation(summary = "Returns a JSON with a string and a hash based on string specified in the URL",
            description = "Generates a JSON with a string and a hash based on string provided in " +
                    "the URL and the hashed (with salt) value of that string.")
    @Metered(name = "numberOfCallsTo_StringKeyTuples_JSON", unit = MetricUnits.MINUTES,
            description = "Metrics to monitor numbers of calls to get QR codes.", absolute = true)
    @Counted(unit = MetricUnits.NONE,
            name = "totalCallsCountToGenerateNameKeyTuples",
            absolute = true,
            monotonic = true,
            displayName = "Number of calls to generate the string-key tuples.",
            description = "Total count of calls to generate string-key tuples.",
            tags = {"calls=StringKeyTuple"})
    public Response createIdKeyTuple(@PathParam("id") String id) {
        String nameKeyTuple = null;
        try {
            nameKeyTuple = qrCodeContentsSupplier.getQRCodeContents(id);
            return Response.ok(nameKeyTuple).build();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.serverError().build();
    }

    @GET
    @Path("{id}")
    @Produces("image/png")
    @Operation(summary = "Returns a QR code image based on string specified in the URL",
            description = "Generates a QR code PNG image containing a JSON object with the string provided in " +
                    "the URL and the hashed (with salt) value of that string.")
    @Metered(name = "numberOfCallsQR_PNG", unit = MetricUnits.MINUTES,
            description = "Metrics to monitor numbers of calls to get QR codes.", absolute = true)
    @Counted(unit = MetricUnits.NONE,
            name = "totalCallsCountToGenerateQRCodes",
            absolute = true,
            monotonic = true,
            displayName = "Number of calls to generate the QR codes.",
            description = "Total count of calls to generate QR codes.",
            tags = {"calls=QR-Image"})
    public Response createQR(@PathParam("id") String id) {
        try {

            byte[] imageData = qrCodeSupplier.qrCodeGenerator(id);
            return Response.ok(imageData)
                            .header("Content-Disposition", "inline; filename=\"" + id + ".png\"")
                            .build();
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.serverError().build();
    }

    @GET
    @Path("{id}/pdf")
    @Produces("application/pdf; charset=utf-8")
    @Operation(summary = "Returns a PDF file with the QR code based on string specifies in the URL",
            description = "Generates a PDF with the QR code containing a JSON object with the string provided in " +
                    "the URL and the hashed (with salt) value of that string.")
    @Metered(name = "numberOfCallsQR_PDF", unit = MetricUnits.MINUTES,
            description = "Metrics to monitor numbers of calls to get QR codes.", absolute = true)
    @Counted(unit = MetricUnits.NONE,
            name = "totalCallsCountToGenerateQRPDFCodes",
            absolute = true,
            monotonic = true,
            displayName = "Number of calls to generate PDF files with QR codes.",
            description = "Total count of calls to generate PDF files with QR codes.",
            tags = {"calls=QR-PDF"})
    public Response createQRPDF(@PathParam("id") String id) {
        try {
            byte[] pdfDocument = pdfDocumentSupplier.pdfDocumentGenerator(id);
            return Response.ok(pdfDocument)
                            .header("Content-Disposition", "inline; filename=\"" + id + ".pdf\"")
                            .build();
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.serverError().build();
    }



}
