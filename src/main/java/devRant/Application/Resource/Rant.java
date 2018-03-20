package devRant.Application.Resource;


import java.util.List;

public class Rant extends Common{

    protected final long rant_id;

    protected final long comments_count;

    protected final List<String> tags;

    public Rant(
            long id,
            long votes,
            String content,
            String image_url,
            boolean has_image,
            List<String> tags,
            long comments_count
    ){

        super(content, votes, has_image, image_url, null);

        this.rant_id = id;
        this.tags = tags;
        this.comments_count = comments_count;
    }

    public long getRant_id() {
        return rant_id;
    }

    public List<String> getTags() {
        return tags;
    }

    public long getComments_count() {
        return comments_count;
    }
}
