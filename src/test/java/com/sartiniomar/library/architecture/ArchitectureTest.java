package com.sartiniomar.library.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest {

  private static JavaClasses classes;

  @BeforeAll
  static void setUp() {
    classes = new ClassFileImporter()
        .importPackages("com.sartiniomar.library");
  }

  @Test
  void domainMustNotDependOnApplication() {

    ArchRule rule = noClasses()
        .that().resideInAnyPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..application..");

    rule.check(classes);
  }

  @Test
  void domainMustNotDependOnInfrastructure() {

    ArchRule rule = noClasses()
        .that().resideInAnyPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..infrastructure..");

    rule.check(classes);
  }

  @Test
  void domainMustNotDependOnSpring() {

    ArchRule rule = noClasses()
        .that().resideInAnyPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "org.springframework..",
            "org.springframework.boot.."
        );

    rule.check(classes);
  }

  @Test
  void applicationMustNotDependOnInfrastructure() {

    ArchRule rule = noClasses()
        .that().resideInAnyPackage("..application..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..infrastructure..");

    rule.check(classes);
  }

  @Test
  void adaptersMustResideInInfrastructure() {

    ArchRule rule = classes()
        .that().haveSimpleNameEndingWith("AdapterRepository")
        .should().resideInAnyPackage("..infrastructure..");

    rule.check(classes);
  }

  @Test
  void loanUseCasesMustBeInApplicationUsecasePackage() {

    ArchRule rule = classes()
        .that().haveSimpleNameEndingWith("UseCaseImpl")
        .should().resideInAnyPackage("..loan.application.usecase..")
        .orShould().resideInAnyPackage("..catalog.application.usecase..")
        .orShould().resideInAnyPackage("..patron.application.usecase..");

    rule.check(classes);
  }
}
