import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;

public class ExportToPDF {
    private PDDocument document = new PDDocument();
    private PDPage page = new PDPage(PDRectangle.A4);
//    private PDResources resources = new PDResources();

    private PDFont font = PDType1Font.HELVETICA;
    private float fontSize = 12.0f;

    public void createCertificat(String date, String name){
        try {
            File pdfDocument = new File("certificat.pdf");

            landscapeMode();
            addBackgroundImage("img/fond-certificat.jpg");
            addText(date, name);

            document.addPage(page);
            document.save(pdfDocument);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void landscapeMode(){
        PDPageContentStream contentStream = null;

        try {

            PDRectangle pageSize = page.getMediaBox();
            float pageWidth = pageSize.getWidth();

            //Tourne à 90°
            page.setRotation(90);

            //Applique la rotation via une transformation par Matrice
            contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
            contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                contentStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void addBackgroundImage(String imgPath){
        PDPageContentStream contentStream = null;

        try
        {
            //Récupère l'image
            PDImageXObject pdImage = PDImageXObject.createFromFile(imgPath, document);
            contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

            // reduce this value if the image is too large
            float scale = 0.61f;

            //Ajout l'image au pdf
            contentStream.drawImage(pdImage, 20, 20, pdImage.getWidth()*scale, pdImage.getHeight()*scale);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                contentStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addText(String date, String name){
        PDPageContentStream contents = null;

        try {
            contents = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
            contents.beginText();
            contents.setFont(font, fontSize);
            contents.newLineAtOffset(190, 150);
            contents.showText(date);
            contents.newLineAtOffset(390, 0);
            contents.showText(name);
            contents.newLineAtOffset(-340, 120);
            contents.showText("Tunis as passed this test !");
            contents.endText();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                contents.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}