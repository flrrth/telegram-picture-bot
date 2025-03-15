package picturebot.user;

public interface UserCoolDown {

    /**
     * Returns the number of seconds the user is on 'cool down'. When 0 is returned, the user isn't on 'cool down'.
     * @param id the user ID
     * @return a <code>long</code> with the number of seconds that the user is still on 'cool down', or <code>0</code>
     * otherwise.
     */
    long getSecondsLeftOnCoolDown(Long id);
}
