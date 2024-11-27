package com.software.modsen.ridesmicroservice.controllers;

import com.software.modsen.ridesmicroservice.entities.report.RideReportDto;
import com.software.modsen.ridesmicroservice.services.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.software.modsen.ridesmicroservice.entities.report.ReportMessage.REPORT_WAS_SUCCESSFULLY_SEND_MESSAGE;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/report")
    public ResponseEntity<String> buildReport(
            @Valid @RequestBody RideReportDto rideReportDto
            ) {
        reportService.generateRideReport(
                rideReportDto.format(), rideReportDto.startDate(), rideReportDto.endDate(), rideReportDto.dateType(),
                rideReportDto.email(), rideReportDto.rows());

        return new ResponseEntity<>(String.format(REPORT_WAS_SUCCESSFULLY_SEND_MESSAGE, rideReportDto.email()),
                HttpStatus.CREATED);
    }
}
