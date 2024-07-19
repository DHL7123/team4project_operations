package com.evo.evoproject.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExcelUtil {

    private static final String[] COLUMNS = {"테스트 ID", "테스트 이름", "클래스 이름", "메서드 이름", "입력값", "예상 결과", "실제 결과", "테스트 상태", "비고"};
    private static final String FILE_NAME = "src/test/resources/JUnit_Test_Results.xlsx";

    private Workbook workbook;
    private Sheet sheet;
    private int rowCount;

    public ExcelUtil() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("테스트 결과");
        rowCount = 0;
        createHeaderRow();

        // Ensure the directory exists
        try {
            Files.createDirectories(Paths.get("src/test/resources"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories for the Excel file", e);
        }
    }

    private void createHeaderRow() {
        Row headerRow = sheet.createRow(rowCount++);
        for (int i = 0; i < COLUMNS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(COLUMNS[i]);
        }
    }

    public void addTestResult(String testId, String testName, String className, String methodName, String inputValues, String expectedResult, String actualResult, String testStatus, String remarks) {
        Row row = sheet.createRow(rowCount++);
        row.createCell(0).setCellValue(testId);
        row.createCell(1).setCellValue(testName);
        row.createCell(2).setCellValue(className);
        row.createCell(3).setCellValue(methodName);
        row.createCell(4).setCellValue(inputValues);
        row.createCell(5).setCellValue(expectedResult);
        row.createCell(6).setCellValue(actualResult);
        row.createCell(7).setCellValue(testStatus);
        row.createCell(8).setCellValue(remarks);
    }

    public void saveExcelFile() throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
