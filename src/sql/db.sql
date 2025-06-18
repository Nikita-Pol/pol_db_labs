SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema media_insight_db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `media_insight_db` ;
-- Changed CHARACTER SET and COLLATE for older MySQL versions
CREATE SCHEMA IF NOT EXISTS `media_insight_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `media_insight_db` ;

-- -----------------------------------------------------
-- Table `media_insight_db`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `media_insight_db`.`role` ;
CREATE TABLE IF NOT EXISTS `media_insight_db`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL UNIQUE,
  `description` TEXT,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `media_insight_db`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `media_insight_db`.`user` ;
CREATE TABLE IF NOT EXISTS `media_insight_db`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `email` VARCHAR(128) NOT NULL UNIQUE,
  `password` VARCHAR(128) NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_role_idx` (`role_id` ASC),
  CONSTRAINT `fk_user_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `media_insight_db`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `media_insight_db`.`media_content`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `media_insight_db`.`media_content` ;
CREATE TABLE IF NOT EXISTS `media_insight_db`.`media_content` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(128) NOT NULL,
  `description` TEXT,
  `type` VARCHAR(32) NOT NULL,
  `file_path` VARCHAR(255), 
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_media_content_user_idx` (`user_id` ASC),
  CONSTRAINT `fk_media_content_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `media_insight_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `media_insight_db`.`project`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `media_insight_db`.`project` ;
CREATE TABLE IF NOT EXISTS `media_insight_db`.`project` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `description` TEXT,
  `created_at` DATETIME NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_project_user_idx` (`user_id` ASC),
  CONSTRAINT `fk_project_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `media_insight_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `media_insight_db`.`analysis_task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `media_insight_db`.`analysis_task` ;
CREATE TABLE IF NOT EXISTS `media_insight_db`.`analysis_task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `status` VARCHAR(64) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `user_id` INT NOT NULL,
  `project_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_analysis_task_user_idx` (`user_id` ASC),
  INDEX `fk_analysis_task_project_idx` (`project_id` ASC),
  CONSTRAINT `fk_analysis_task_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `media_insight_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_analysis_task_project`
    FOREIGN KEY (`project_id`)
    REFERENCES `media_insight_db`.`project` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `media_insight_db`.`task_content`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `media_insight_db`.`task_content` ;
CREATE TABLE IF NOT EXISTS `media_insight_db`.`task_content` (
  `media_content_id` INT NOT NULL,
  `analysis_task_id` INT NOT NULL,
  PRIMARY KEY (`media_content_id`, `analysis_task_id`),
  INDEX `fk_task_content_analysis_task_idx` (`analysis_task_id` ASC),
  CONSTRAINT `fk_task_content_media_content`
    FOREIGN KEY (`media_content_id`)
    REFERENCES `media_insight_db`.`media_content` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_content_analysis_task`
    FOREIGN KEY (`analysis_task_id`)
    REFERENCES `media_insight_db`.`analysis_task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `media_insight_db`.`report`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `media_insight_db`.`report` ;
CREATE TABLE IF NOT EXISTS `media_insight_db`.`report` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `content` TEXT,
  `created_at` DATETIME NOT NULL,
  `analysis_task_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_report_analysis_task_idx` (`analysis_task_id` ASC),
  CONSTRAINT `fk_report_analysis_task`
    FOREIGN KEY (`analysis_task_id`)
    REFERENCES `media_insight_db`.`analysis_task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Data Inserts for `media_insight_db`
-- -----------------------------------------------------
START TRANSACTION;

-- Insert into `role`
INSERT INTO `media_insight_db`.`role` (`name`, `description`) VALUES
('Administrator', 'Full system access and management.'),
('Analyst', 'Can create tasks, view reports, manage content.'),
('Viewer', 'Can view reports and limited content.');

-- Insert into `user`
INSERT INTO `media_insight_db`.`user` (`name`, `email`, `password`, `role_id`) VALUES
('Ivan Adminov', 'ivan_admin@gmail.com', 'qwerty', (SELECT id FROM `role` WHERE name = 'Administrator')),
('Olena Analyst', 'olena_analyst@gmail.com', 'qwerty', (SELECT id FROM `role` WHERE name = 'Analyst')),
('Petro Viewer', 'petro_viewer@gmail.com', 'qwerty', (SELECT id FROM `role` WHERE name = 'Viewer')),
('Maria Marketer', 'maria_m@gmail.com', 'qwerty', (SELECT id FROM `role` WHERE name = 'Analyst'));

-- Insert into `project`
INSERT INTO `media_insight_db`.`project` (`name`, `description`, `created_at`, `user_id`) VALUES
('Brand Monitoring', 'Monitoring mentions of our brand across media.', CURRENT_TIMESTAMP(), (SELECT id FROM `user` WHERE email = 'ivan_admin@gmail.com')),
('Competitor Analysis', 'Analyzing media presence of key competitors.', CURRENT_TIMESTAMP(), (SELECT id FROM `user` WHERE email = 'olena_analyst@gmail.com')),
('Campaign Performance', 'Evaluating media impact of recent marketing campaign.', CURRENT_TIMESTAMP(), (SELECT id FROM `user` WHERE email = 'maria_m@gmail.com'));

-- Insert into `media_content`
INSERT INTO `media_insight_db`.`media_content` (`title`, `description`, `type`, `file_path`, `user_id`) VALUES
('News Article on Tech A', 'Detailed article about new technology from major news source.', 'text', 'http://example.com/news/tech_a', (SELECT id FROM `user` WHERE email = 'olena_analyst@gmail.com')),
('Tweet about Product X', 'User review tweet for Product X.', 'text', 'http://twitter.com/user/product_x_tweet', (SELECT id FROM `user` WHERE email = 'olena_analyst@gmail.com')),
('Image: Company Logo', 'Official company logo in high resolution.', 'image', 'http://example.com/assets/logo.png', (SELECT id FROM `user` WHERE email = 'ivan_admin@gmail.com')),
('Video: Product Demo', 'Short video demonstrating Product Y features.', 'video', 'http://youtube.com/product_y_demo', (SELECT id FROM `user` WHERE email = 'maria_m@gmail.com')),
('Blog Post: Industry Trends', 'Expert analysis of current industry trends.', 'text', 'http://blog.industry.com/trends', (SELECT id FROM `user` WHERE email = 'olena_analyst@gmail.com'));

-- Insert into `analysis_task`
INSERT INTO `media_insight_db`.`analysis_task` (`name`, `status`, `created_at`, `user_id`, `project_id`) VALUES
('Monitor Brand Mentions Q2', 'in progress', CURRENT_TIMESTAMP(), (SELECT id FROM `user` WHERE email = 'olena_analyst@gmail.com'), (SELECT id FROM `project` WHERE name = 'Brand Monitoring')),
('Competitor X Sentiment', 'completed', CURRENT_TIMESTAMP(), (SELECT id FROM `user` WHERE email = 'olena_analyst@gmail.com'), (SELECT id FROM `project` WHERE name = 'Competitor Analysis')),
('Campaign A Media Impact', 'pending', CURRENT_TIMESTAMP(), (SELECT id FROM `user` WHERE email = 'maria_m@gmail.com'), (SELECT id FROM `project` WHERE name = 'Campaign Performance')),
('Product Y Video Analysis', 'in progress', CURRENT_TIMESTAMP(), (SELECT id FROM `user` WHERE email = 'maria_m@gmail.com'), (SELECT id FROM `project` WHERE name = 'Campaign Performance'));

-- Insert into `task_content` (linking media_content to analysis_task)
INSERT INTO `media_insight_db`.`task_content` (`media_content_id`, `analysis_task_id`) VALUES
((SELECT id FROM `media_content` WHERE title = 'News Article on Tech A'), (SELECT id FROM `analysis_task` WHERE name = 'Monitor Brand Mentions Q2')),
((SELECT id FROM `media_content` WHERE title = 'Tweet about Product X'), (SELECT id FROM `analysis_task` WHERE name = 'Monitor Brand Mentions Q2')),
((SELECT id FROM `media_content` WHERE title = 'Tweet about Product X'), (SELECT id FROM `analysis_task` WHERE name = 'Competitor X Sentiment')), -- A single media item can be part of multiple tasks
((SELECT id FROM `media_content` WHERE title = 'Video: Product Demo'), (SELECT id FROM `analysis_task` WHERE name = 'Product Y Video Analysis')),
((SELECT id FROM `media_content` WHERE title = 'Blog Post: Industry Trends'), (SELECT id FROM `analysis_task` WHERE name = 'Competitor X Sentiment'));


-- Insert into `report`
INSERT INTO `media_insight_db`.`report` (`name`, `content`, `created_at`, `analysis_task_id`) VALUES
('Q2 Brand Mentions Summary', '{"total_mentions": 1500, "positive": 70, "negative": 10, "neutral": 20}', CURRENT_TIMESTAMP(), (SELECT id FROM `analysis_task` WHERE name = 'Monitor Brand Mentions Q2')),
('Competitor X Sentiment Report', '{"overall_sentiment": "neutral", "key_themes": ["innovation", "market share"]}', CURRENT_TIMESTAMP(), (SELECT id FROM `analysis_task` WHERE name = 'Competitor X Sentiment'));

COMMIT;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;