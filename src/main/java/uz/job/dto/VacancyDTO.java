package uz.job.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.job.enums.Region;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class VacancyDTO {
    private Long chatId;
    private String jobTitle;
    private Region region;
    private String district;
    private String company;
    private String salary;
    private String phone;
    private String username;
    private String fullName;
    private String acceptTime;
    private String workTime;
    private String additional;
    private LocalDateTime createdAt;
}
