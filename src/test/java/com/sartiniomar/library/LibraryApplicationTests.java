package com.sartiniomar.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import java.io.File;
import java.io.FileReader;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class LibraryApplicationTests {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected EntityManager entityManager;

  @Autowired
  protected JdbcTemplate jdbcTemplate;


  @SneakyThrows
  protected String getContentFromFile(String filePath) {
    File file = new ClassPathResource(filePath).getFile();
    return FileCopyUtils.copyToString(new FileReader(file));
  }

  public void flushAndClear() {
    entityManager.flush();
    entityManager.clear();
  }
}
