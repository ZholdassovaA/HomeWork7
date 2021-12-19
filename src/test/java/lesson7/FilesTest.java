package lesson7;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.xlstest.XLS;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilesTest {

    private final String urlUpload = "https://filelu.com/";
    private final String urlDownload = "https://github.com/selenide/selenide/blob/master/README.md";
    private final String urlDownloadPDF = "https://aldebaran.ru/author/bakulin_aleksandr/kniga_gravitaciya_i_yefir/";
    private final static String urlDownloadXLS = "https://ckmt.ru/price-download.html";

    private final SelenideElement uploadFile = $("input[type='file']");
    private final SelenideElement downloadFile = $("#raw-url");
    private final SelenideElement pdfFile = $(byText("pdf (A6)"));
    private final SelenideElement xlsFile = $("h3 a[href='https://ckmt.ru/TehresursPrice.xls']");

    private final String FILENAME = "newYear.jpg";
    private final SelenideElement uploadedFilePath = $("#o9651dc2g5ea");

    private final static String selenideReadme = "Selenide = UI Testing Framework powered by Selenium WebDriver";
    private final static String xlsTitleCheck = "СВАРОЧНАЯ (ГОСТ 2246-70)";

    @Test
    @DisplayName("Загрузка файла по относительному пути")
    void fileUploadedAndDisplayed() {
        open(urlUpload); // Открываем URL
        uploadFile.uploadFromClasspath(FILENAME); // Загружаем картинку из resources
        $("input[type='button']").click(); // Нажимаем на кнопку "Upload"
        uploadedFilePath.shouldHave(Condition.text(FILENAME)); // Проверяем наличие загруженного рисунка
    }

    @Test
    @DisplayName("Скачивание readme файла")
    void downloadReadmeFail() throws IOException {
        open(urlDownload); // Открываем URL
        File download = downloadFile.download(); // Присваиваем в переменную скаченный файл
        String file  = IOUtils.toString(new FileReader(download)); // Читаем полученную переменную классом IOUtil
        assertTrue(file.contains(selenideReadme)); // Проверяем наличие текста в файле
    }

    @Test
    @DisplayName("Скачивание PDF файла")
    void downloadPdfFail() throws IOException {
        open(urlDownloadPDF);// Открываем URL
        File pdf = pdfFile.download(); // Присваиваем в переменную скаченный файл
        PDF parsedPdf = new PDF(pdf); // Создаём объект PDF и присваиваем ему File
        Assertions.assertEquals(667, parsedPdf.numberOfPages); // Проверяем на кол-во страниц
    }

    @Test
    @DisplayName("Скачивание xls файла")
    void xlsFileDownloadTest() throws IOException {
        open(urlDownloadXLS);
        File file = xlsFile.download();
        XLS parsedXls = new XLS(file);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(137)
                .getCell(0)
                .getStringCellValue()
                .contains(xlsTitleCheck);

        assertTrue(checkPassed);
    }

}
