package com.bca.byc.model.export;

import com.bca.byc.enums.AdminApprovalStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ExportFilterRequest{
    private LocalDate startDate;
        private LocalDate endDate;
        private AdminApprovalStatus status;
        private Long locationId;

        public ExportFilterRequest(LocalDate startDate, LocalDate endDate) {
                this.startDate = startDate;
                this.endDate = endDate;
        }

        public ExportFilterRequest(AdminApprovalStatus status) {
                this.status = status;
        }

        public ExportFilterRequest(Long locationId) {
                this.locationId = locationId;
        }

        public ExportFilterRequest(LocalDate startDate, LocalDate endDate, AdminApprovalStatus status) {
                this.startDate = startDate;
                this.endDate = endDate;
                this.status = status;
        }

        public ExportFilterRequest(LocalDate startDate, LocalDate endDate, Long locationId) {
                this.startDate = startDate;
                this.endDate = endDate;
                this.locationId = locationId;
        }
}