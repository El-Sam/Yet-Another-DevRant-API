package devRant.Application.Resource;


public class UserActivityStats{

    private long rants_count;

    private long upvotes_given;

    private long comments_count;

    private long favorites_count;

    public UserActivityStats(
            long rants_count,
            long upvotes_given,
            long comments_count,
            long favorites_count
    ) {
        this.rants_count = rants_count;
        this.upvotes_given = upvotes_given;
        this.comments_count = comments_count;
        this.favorites_count = favorites_count;
    }

    public long getRantsCount() {
        return rants_count;
    }

    public long getUpvotesGiven() {
        return upvotes_given;
    }

    public long getCommentsCount() {
        return comments_count;
    }

    public long getFavoritesCount() {
        return favorites_count;
    }
}
