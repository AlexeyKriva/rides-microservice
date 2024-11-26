package com.software.modsen.ridesmicroservice.entities.passenger;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passenger")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Passenger entity.")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    @Schema(example = "passenger@gmail.com")
    private String email;

    @Column(name = "phone_number", nullable = false)
    @Schema(example = "+375449870563")
    private String phoneNumber;

    @Column(name = "is_deleted", nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean isDeleted;
}