-- London

INSERT INTO picture_bot.bot_user (id, first_seen, last_seen, settings_message_id)
VALUES (1, '2024-01-01 00:00:00.000000', '2024-01-01 00:00:00.000000', 0);

INSERT INTO picture_bot.settings (is_enabled, schedule, is_spoiler_enabled, user_id, last_modified, timezone_id)
VALUES (true, '11:00:00', false, 1, '2024-01-01 00:00:00.000000', 0);

UPDATE picture_bot.bot_user SET settings_id = 1 WHERE id = 1;

INSERT INTO picture_bot.bot_user_details ("timestamp", user_name, first_name, last_name, is_bot, language_code, user_id)
VALUES ('2024-01-01 00:00:00.000000', 'london', 'London', '', false, 'en', 1);

-- Amsterdam

INSERT INTO picture_bot.bot_user (id, first_seen, last_seen, settings_message_id)
VALUES (2, '2024-01-01 00:00:00.000000', '2024-01-01 00:00:00.000000', 0);

INSERT INTO picture_bot.settings (is_enabled, schedule, is_spoiler_enabled, user_id, last_modified, timezone_id)
VALUES (true, '12:00:00', false, 2, '2024-01-01 00:00:00.000000', 1);

UPDATE picture_bot.bot_user SET settings_id = 2 WHERE id = 2;

INSERT INTO picture_bot.bot_user_details ("timestamp", user_name, first_name, last_name, is_bot, language_code, user_id)
VALUES ('2024-01-01 00:00:00.000000', 'amsterdam', 'Amsterdam', '', false, 'nl', 2);

-- Bucharest

INSERT INTO picture_bot.bot_user (id, first_seen, last_seen, settings_message_id)
VALUES (3, '2024-01-01 00:00:00.000000', '2024-01-01 00:00:00.000000', 0);

INSERT INTO picture_bot.settings (is_enabled, schedule, is_spoiler_enabled, user_id, last_modified, timezone_id)
VALUES (true, '13:00:00', false, 3, '2024-01-01 00:00:00.000000', 2);

UPDATE picture_bot.bot_user SET settings_id = 3 WHERE id = 3;

INSERT INTO picture_bot.bot_user_details ("timestamp", user_name, first_name, last_name, is_bot, language_code, user_id)
VALUES ('2024-01-01 00:00:00.000000', 'bucharest', 'Bucharest', '', false, 'nl', 3);

-- Moscow

INSERT INTO picture_bot.bot_user (id, first_seen, last_seen, settings_message_id)
VALUES (4, '2024-01-01 00:00:00.000000', '2024-01-01 00:00:00.000000', 0);

INSERT INTO picture_bot.settings (is_enabled, schedule, is_spoiler_enabled, user_id, last_modified, timezone_id)
VALUES (true, '14:00:00', false, 4, '2024-01-01 00:00:00.000000', 3);

UPDATE picture_bot.bot_user SET settings_id = 4 WHERE id = 4;

INSERT INTO picture_bot.bot_user_details ("timestamp", user_name, first_name, last_name, is_bot, language_code, user_id)
VALUES ('2024-01-01 00:00:00.000000', 'moscow', 'Moscow', '', false, 'nl', 4);

-- Scheduler

INSERT INTO picture_bot.bot_user (id, first_seen, last_seen, settings_message_id)
VALUES (5, '2024-01-01 00:00:00.000000', '2024-01-01 00:00:00.000000', 0);

INSERT INTO picture_bot.settings (is_enabled, schedule, is_spoiler_enabled, user_id, last_modified, timezone_id)
VALUES (true, '07:00:00', false, 5, '2024-01-01 00:00:00.000000', 1);

UPDATE picture_bot.bot_user SET settings_id = 5 WHERE id = 5;

INSERT INTO picture_bot.bot_user_details ("timestamp", user_name, first_name, last_name, is_bot, language_code, user_id)
VALUES ('2024-01-01 00:00:00.000000', 'scheduler', 'Scheduler', '', false, 'nl', 5);

-- John Doe

INSERT INTO picture_bot.bot_user (id, first_seen, last_seen, settings_message_id)
VALUES (6, '2024-01-01 00:00:00.000000', '2024-01-01 00:00:00.000000', 1234);

INSERT INTO picture_bot.settings (is_enabled, schedule, is_spoiler_enabled, user_id, last_modified, timezone_id)
VALUES (false, '07:00:00', false, 6, '2024-01-01 00:00:00.000000', 1);

UPDATE picture_bot.bot_user SET settings_id = 6 WHERE id = 6;

INSERT INTO picture_bot.bot_user_details ("timestamp", user_name, first_name, last_name, is_bot, language_code, user_id)
VALUES ('2024-01-01 00:00:00.000000', 'john_d', 'John', 'Doe', false, 'nl', 6);

-- Jane Doe

INSERT INTO picture_bot.bot_user (id, first_seen, last_seen, settings_message_id, request_count)
VALUES (7, '2024-01-01 00:00:00.000000', '2024-01-01 00:00:00.000000', 1234, 1);

INSERT INTO picture_bot.settings (is_enabled, schedule, is_spoiler_enabled, user_id, last_modified, timezone_id)
VALUES (false, '07:00:00', false, 7, '2024-01-01 00:00:00.000000', 1);

UPDATE picture_bot.bot_user SET settings_id = 7 WHERE id = 7;

INSERT INTO picture_bot.bot_user_details ("timestamp", user_name, first_name, last_name, is_bot, language_code, user_id)
VALUES ('2024-01-01 00:00:00.000000', 'jane_d', 'Jane', 'Doe', false, 'nl', 7);
