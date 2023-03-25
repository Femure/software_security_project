package ku.review.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import  ku.review.model.CaptchaResponse;

@Component
public class CaptchaValidator {
	
	@Autowired
	private RestTemplate restTemplate;
 
	public boolean isValidCaptcha(String captcha) {
		
		String url= "https://www.google.com/recaptcha/api/siteverify";
		String params="?secret=6LdIBPokAAAAAFhUMfR9ELHOYlmZwjlo5C3Gh5eO&response="+captcha;
		String completeUrl=url+params;
		CaptchaResponse resp= restTemplate.postForObject(completeUrl, null, CaptchaResponse.class);
		return resp.isSuccess();
	}
}