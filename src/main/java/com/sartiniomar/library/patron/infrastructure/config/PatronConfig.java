package com.sartiniomar.library.patron.infrastructure.config;

import com.sartiniomar.library.patron.application.port.in.CreateRegularPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.CreateResearcherPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.DeletePatronUseCase;
import com.sartiniomar.library.patron.application.port.in.GetPatronByIdUseCase;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronUseCase;
import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.application.usecase.CreateRegularPatronUseCaseImpl;
import com.sartiniomar.library.patron.application.usecase.CreateResearcherPatronUseCaseImpl;
import com.sartiniomar.library.patron.application.usecase.DeletePatronUseCaseImpl;
import com.sartiniomar.library.patron.application.usecase.GetPatronByIdUseCaseImpl;
import com.sartiniomar.library.patron.application.usecase.UpdatePatronUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatronConfig {

  @Bean
  public CreateRegularPatronUseCase createRegularPatronUseCase(PatronRepository patronRepository) {
    return new CreateRegularPatronUseCaseImpl(patronRepository);
  }

  @Bean
      public CreateResearcherPatronUseCase createResearcherPatronUseCase(PatronRepository patronRepository) {
    return new CreateResearcherPatronUseCaseImpl(patronRepository);
  }

  @Bean
  public GetPatronByIdUseCase getPatronByIdUseCase(PatronRepository patronRepository) {
    return new GetPatronByIdUseCaseImpl(patronRepository);
  }

  @Bean
  public DeletePatronUseCase deletePatronUseCase(PatronRepository patronRepository) {
    return new DeletePatronUseCaseImpl(patronRepository);
  }

  @Bean
  public UpdatePatronUseCase updatePatronUseCase(PatronRepository patronRepository) {
    return new UpdatePatronUseCaseImpl(patronRepository);
  }
}
