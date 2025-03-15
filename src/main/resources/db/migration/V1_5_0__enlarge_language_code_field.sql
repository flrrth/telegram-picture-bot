ALTER TABLE picture_bot.bot_user_details
    ALTER COLUMN language_code TYPE VARCHAR(3) USING (language_code::VARCHAR(3));
