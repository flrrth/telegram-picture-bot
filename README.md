# Telegram Picture Bot

This is a Telegram bot that allows the user request a random picture. It also offers a simple web page that enables the 
user to schedule a daily picture. The bot is written in Java 17. It uses:

- Spring Boot,
- [TelegramBots](https://github.com/rubenlagus/TelegramBots),
- PostgreSQL.

## Features

- Users can request a random picture,
- Users can schedule a daily picture,
- Customizable messages,
- Custom daily pictures for a set date, e.g. Christmas,
- Tracking of user activity,
- Users can upload pictures.

## How to use it

You'll need some working knowledge of Spring Boot, PostgreSQL and an operating system like Linux. You'll also have to 
set up your bot using Telegram's BotFather. You can host the bot on any system with Internet access, but the web page 
must be accessible from the Internet. The server you host the HTML document on must support SSL, otherwise Telegram
refuses to load it. The web page is a simple static HTML file, so you can host it on any web server. A limited number of
timezones is currently supported.

Credentials are read from environment variables, see [picture-bot.env](./systemd/picture-bot.env):
```properties
PICTURE_BOT_TOKEN=<token obtained from BotFather>
DB_USERNAME=<database username>
DB_PASSWORD=<database password>
```
During development, you can set these variables in your IDE.
## Organizing pictures
This bot needs pictures. Ordinary pictures go in the `regular` directory. Special pictures go in directories named after
the (optional) year, month and day, like this:
```
/base/path
    /regular
        picture.jpg
        ...
        message.properties
    /12
        /25 (Christmas, same date every year)
            picture.jpg
            ...
            message.properties (optional)
    /2025
        /2
            /28 (Start of Ramadan, different date every year)
                picture.jpg
                ...
                message.properties (optional)
```
Each directory must be outfitted with a message in a `message.properties` file. The default language is English, 
additional languages can be added by creating a `message_xx.properties` file, where `xx` is the language code, e.g. 
`message_nl.properties` for Dutch.

The content must look like this:
```properties
greeting=🎄 Merry Christmas\\! 🎄
```
The text supports Markdown, so you must use backslashes to escape special characters. It is possible include emojis and 
links:
```properties
greeting=Hello\\! Today is [International Ninja Day](https://www.daysoftheyear.com/days/ninja-day/)\\! 🥷
```

## Supported commands

The bot supports the following commands:

- `/start` - Starts the bot,
- `/help` - Shows a help text,
- `/random` - Requests a random picture, (the name of this command can be customized via the configuration),
- `/settings` - Loads the settings web app,
- `/version` - Shows the bot version.

You must configure these commands via the [BotFather](https://telegram.me/BotFather) if you like to offer the user a 
convenient way to access them.

## Roadmap

There isn't any. It is not likely this bot is developed any further. The same applies to the documentation.
