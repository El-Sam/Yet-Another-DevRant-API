package devRant.Application.Resource;

import org.springframework.hateoas.ResourceSupport;


public class User extends ResourceSupport{

    private String avatar_url;

    private String username;

    private long score;

    private UserActivityStats activity_stats;

    private UserAbout about;

    public User(
            String avatar_url,
            String username,
            long score,
            UserActivityStats stats,
            UserAbout about
    ) {
        this.avatar_url = avatar_url;
        this.username = username;
        this.score = score;
        this.about = about;
        this.activity_stats = stats;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getUsername() {
        return username;
    }

    public long getScore() {
        return score;
    }

    public UserAbout getAbout() {
        return about;
    }

    public UserActivityStats getActivity_stats() {
        return activity_stats;
    }
}
