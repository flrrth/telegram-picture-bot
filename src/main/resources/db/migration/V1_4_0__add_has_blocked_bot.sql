ALTER TABLE picture_bot.bot_user
    ADD has_blocked_bot BOOLEAN DEFAULT FALSE;

ALTER TABLE picture_bot.bot_user
    ALTER COLUMN has_blocked_bot SET NOT NULL;
