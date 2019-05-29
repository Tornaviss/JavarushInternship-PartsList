Create schema if not exists test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
use test;
CREATE TABLE `parts` (
  ID INT(10) PRIMARY KEY AUTO_INCREMENT ,
  name VARCHAR(100) NOT NULL UNIQUE ,
  count INT(10) DEFAULT 1 NOT NULL ,
  type varchar(20) ,
  essential BIT DEFAULT FALSE NOT NULL
)

  COLLATE='utf8_general_ci';

INSERT INTO `parts` (name, count, type, essential)
VALUES ('MSI - B450 TOMAHAWK', 2, 'MOTHERBOARD', true),
  ('Intel 8th Generation Core i7', 4, 'CPU', true),
  ('NVIDIA GeForce MX150', 2, null, false),
  ('CORSAIR VENGEANCE LPX Series 16GB 2.4GHz DDR4', 10, 'RAM', true),
  ('NVIDIA GeForce MX150', 4, 'GRAPHIC_CARD', true),
  ('Блок питания 600W DEEPCOOL DE600 V2', 4, 'PS', true),
  ('SuperMicro M11SDV-8C', 1, 'MOTHERBOARD', true),
  ('Gigabyte GeForce GTX 1060 Windforce', 2, 'GRAPHIC_CARD', true),
  ('Custom Case WS-SRS', 1, 'CASE', true),
  ('Samsung SSD 860 EVO 500GB', 1, 'SSD', true),
  ('AMD Ryzen 7 2700X Processor', 1, 'CPU', true),
  ('Cooler Master RR-212S', 1, null, false),
  ('HP New Genuine Stream 11 Pro Series WLAN', 1, null, false),
  ('HP 850W Power Supply for Z-Series Z800 Workstation', 1, 'PS', true),
  ('HP 5JV89AT Quadro RTX 4000 Graphic Card', 1, 'GRAPHIC_CARD', true),
  ('SuperMicro X11SRM-F Motherboard', 1, 'MOTHERBOARD', true),
  ('Razer Naga Expert MMO Chroma USB', 3, null, false);
COMMIT;
  


