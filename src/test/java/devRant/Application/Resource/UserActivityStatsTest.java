package devRant.Application.Resource;

import junit.framework.TestCase;

public class UserActivityStatsTest extends TestCase {

    public void testConstructWhereAllArgs(){

        long rants_count = 66;
        long upvotes_count = 100;
        long comments_count = 20;
        long favorites_count = 0;


        UserActivityStats userActivityStats = new UserActivityStats(
            rants_count,
            upvotes_count,
            comments_count,
            favorites_count
        );

        assertEquals(rants_count, userActivityStats.getRantsCount());
        assertEquals(upvotes_count, userActivityStats.getUpvotesGiven());
        assertEquals(comments_count, userActivityStats.getCommentsCount());
        assertEquals(favorites_count, userActivityStats.getFavoritesCount());
    }
}
