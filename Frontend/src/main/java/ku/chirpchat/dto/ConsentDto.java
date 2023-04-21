package ku.chirpchat.dto;

import lombok.Data;

@Data
public class ConsentDto {
    private boolean consentName;
    private boolean consentIP ;
    private boolean consentAd;
    private boolean consentNews;
    private boolean consentSurveys;
    private boolean consentAnalysis;
}
