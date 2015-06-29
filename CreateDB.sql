-- MySQL Script generated by MySQL Workbench
-- 06/04/15 20:08:03
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Accounts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Accounts` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Accounts` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Nickname` VARCHAR(45) NOT NULL,
  `LastName` VARCHAR(45) NULL,
  `FirstName` VARCHAR(45) NULL,
  `Gender` ENUM('Male', 'Female') NULL,
  `Password` VARCHAR(60) NULL,
  `BirthDate` DATE NULL,
  `About` TEXT NULL,
  `GameRating` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `Nickname_UNIQUE` (`Nickname` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Friends`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Friends` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Friends` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `AccIDFrom` INT UNSIGNED NOT NULL,
  `AccIDTo` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_Friends_Accounts_idx` (`AccIDFrom` ASC),
  INDEX `fk_Friends_Accounts1_idx` (`AccIDTo` ASC),
  CONSTRAINT `fk_Friends_Accounts`
    FOREIGN KEY (`AccIDFrom`)
    REFERENCES `mydb`.`Accounts` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Friends_Accounts1`
    FOREIGN KEY (`AccIDTo`)
    REFERENCES `mydb`.`Accounts` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`WaitingFriends`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`WaitingFriends` ;

CREATE TABLE IF NOT EXISTS `mydb`.`WaitingFriends` (
  `ID` INT NOT NULL,
  `AccIDFrom` INT NOT NULL,
  `AccIDTo` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_WaitingFriends_Accounts1_idx` (`AccIDFrom` ASC),
  INDEX `fk_WaitingFriends_Accounts2_idx` (`AccIDTo` ASC),
  CONSTRAINT `fk_WaitingFriends_Accounts1`
    FOREIGN KEY (`AccIDFrom`)
    REFERENCES `mydb`.`Accounts` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_WaitingFriends_Accounts2`
    FOREIGN KEY (`AccIDTo`)
    REFERENCES `mydb`.`Accounts` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Conversations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Conversations` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Conversations` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `AccIDFrom` INT NOT NULL,
  `AccIDTo` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Conversations_Accounts1_idx` (`AccIDFrom` ASC),
  INDEX `fk_Conversations_Accounts2_idx` (`AccIDTo` ASC),
  CONSTRAINT `fk_Conversations_Accounts1`
    FOREIGN KEY (`AccIDFrom`)
    REFERENCES `mydb`.`Accounts` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Conversations_Accounts2`
    FOREIGN KEY (`AccIDTo`)
    REFERENCES `mydb`.`Accounts` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Messages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Messages` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Messages` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Text` TEXT NULL,
  `Sender` ENUM('1', '2') NOT NULL,
  `Conversations_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Messages_Conversations1_idx` (`Conversations_ID` ASC),
  CONSTRAINT `fk_Messages_Conversations1`
    FOREIGN KEY (`Conversations_ID`)
    REFERENCES `mydb`.`Conversations` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Games`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Games` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Games` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Date` DATETIME NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Participations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Participations` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Participations` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `GameID` INT NOT NULL,
  `AccID` INT NOT NULL,
  `RatingChange` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Participations_Games1_idx` (`GameID` ASC),
  INDEX `fk_Participations_Accounts1_idx` (`AccID` ASC),
  CONSTRAINT `fk_Participations_Games1`
    FOREIGN KEY (`GameID`)
    REFERENCES `mydb`.`Games` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Participations_Accounts1`
    FOREIGN KEY (`AccID`)
    REFERENCES `mydb`.`Accounts` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;