package devRant.Application.Resource;

public class Comment extends Common {

    private final String username;

    private final long score;

    public Comment(
            String username,
            long votes,
            String content,
            long score,
            String timestamp,
            boolean has_image,
            String image_url
    ) {
        super(content, votes, has_image, image_url, timestamp);

        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public long getScore() {
        return score;
    }
}
