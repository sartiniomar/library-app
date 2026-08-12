package com.sartiniomar.library.catalog.infrastructure.web.integration.support.helper;

import com.sartiniomar.library.LibraryApplicationTests;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class BasicHelper extends LibraryApplicationTests {

  @Autowired
  protected EntityManager entityManager;

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  public void flushAndClear() {
    entityManager.flush();
    entityManager.clear();
  }
}
