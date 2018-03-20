package devRant.Application.Resource;


import java.util.List;

public class DetailedRant extends Rant {

    protected final List<Comment> comments;

    protected final String username;

    protected final String timestamp;

    public DetailedRant(
            long id,
            String username,
            long votes,
            String content,
            String image_url,
            boolean has_image,
            String timestamp,
            List<String> tags,
            long comments_count,
            List<Comment> comments
    ){

        super(id, votes, content, image_url, has_image, tags, comments_count);

        this.username = username;
        this.comments = comments;
        this.timestamp = timestamp;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getUsername() {
        return username;
    }
}
