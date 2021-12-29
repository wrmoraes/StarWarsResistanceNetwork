package resistance.star.wars.socialnetwork.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

public class MessagesConfig {
  @Value("${spring.messages.basename}")
  private String messagesLocation;

  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    source.setBasenames(messagesLocation);
    source.setUseCodeAsDefaultMessage(true);
    return source;
  }
}
