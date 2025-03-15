package picturebot.bot.factory;

import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;

public interface GetUserProfilePhotosFactory {

    /**
     * Creates a new GetUserProfilePhotos-request for the given user.
     * @param userId the user
     * @return a new GetUserProfilePhotos-request for the given user.
     */
    GetUserProfilePhotos createGetUserProfilePhotos(Long userId);
}
