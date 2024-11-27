package com.software.modsen.ridesmicroservice.services;

import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import com.software.modsen.ridesmicroservice.exceptions.InvalidReportDateTypeException;
import com.software.modsen.ridesmicroservice.exceptions.InvalidReportTypeException;
import com.software.modsen.ridesmicroservice.exceptions.ReportGenerateException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.software.modsen.ridesmicroservice.exceptions.ExceptionMessage.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final RideService rideService;
    private final EmailService emailService;

    public void generateRideReport(String format, LocalDateTime startDate, LocalDateTime endDate,
                                     String dateType, String email, int rows) {
        switch (format.toLowerCase()) {
            case "excel" -> {
                generateRideReportExcel(startDate, endDate, dateType.toLowerCase(), email, rows);
            }
            default -> throw new InvalidReportTypeException(String.format(INVALID_REPORT_TYPE_MESSAGE,
                    format));
        }
    }

    public void generateRideReportExcel(LocalDateTime startDate, LocalDateTime endDate,
                                        String dateType, String email, int rows) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Workbook workbook = new XSSFWorkbook()) {

            List<Ride> rides = findRidesByDateTime(startDate, endDate, dateType, rows);

            Sheet sheet = workbook.createSheet("Rides");

            CellStyle headerStyle = headerStyle(workbook);

            CellStyle rowStyle = rowStyle(workbook);

            CellStyle alternateRowStyle = alternativeRowStyle(workbook);

            Row header = sheet.createRow(0);
            String[] headers = {"Departure point", "Arrival point", "Status", "Date time", "Price", "Currency"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Данные
            int rowIndex = 1;
            for (Ride ride : rides) {
                Row row = sheet.createRow(rowIndex);
                CellStyle currentRowStyle = (rowIndex % 2 == 0) ? rowStyle : alternateRowStyle;

                row.createCell(0).setCellValue(ride.getFromAddress());
                row.createCell(1).setCellValue(ride.getToAddress());
                row.createCell(2).setCellValue(ride.getRideStatus().name());
                row.createCell(3).setCellValue(ride.getOrderDateTime().toString()); // Убедитесь, что формат даты строковый
                row.createCell(4).setCellValue(ride.getPrice());
                row.createCell(5).setCellValue(ride.getCurrency().name());

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(currentRowStyle);
                }

                rowIndex++;
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            rides.forEach(System.out::println);

            workbook.write(outputStream);

            emailService.sendReport(email, outputStream.toByteArray());
        } catch (IOException e) {
            throw new ReportGenerateException(REPORT_GENERATE_ERROR_MESSAGE);
        }
    }

    public List<Ride> findRidesByDateTime(LocalDateTime startDate, LocalDateTime endDate,
                                          String dateType, int rows) {
        Pageable pageable = Pageable.ofSize(rows);

        switch (dateType) {
            case "before" -> {
                return rideService.findRideBeforeDate(startDate, pageable);
            }
            case "after" -> {
                return rideService.findRideAfterDate(startDate, pageable);
            }
            case "between" -> {
                return rideService.findRideRangeDate(startDate, endDate, pageable);
            }
            default -> {
                throw new InvalidReportDateTypeException(String.format(INVALID_REPORT_DATE_TYPE_MESSAGE, dateType));
            }
        }
    }

    public CellStyle headerStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();

        Font headerFont = workbook.createFont();

        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        return headerStyle;
    }

    public CellStyle rowStyle(Workbook workbook) {
        CellStyle rowStyle = workbook.createCellStyle();

        rowStyle.setBorderBottom(BorderStyle.THIN);
        rowStyle.setBorderTop(BorderStyle.THIN);
        rowStyle.setBorderLeft(BorderStyle.THIN);
        rowStyle.setBorderRight(BorderStyle.THIN);

        return rowStyle;
    }

    public CellStyle alternativeRowStyle(Workbook workbook) {
        CellStyle alternateRowStyle = workbook.createCellStyle();

        alternateRowStyle.cloneStyleFrom(rowStyle(workbook));
        alternateRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        alternateRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return alternateRowStyle;
    }
}