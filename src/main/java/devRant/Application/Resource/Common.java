package devRant.Application.Resource;

import org.springframework.hateoas.ResourceSupport;

public class Common extends ResourceSupport {

    protected final String content;

    protected final long votes;

    protected final boolean has_image;

    protected final String image_url;

    protected final String timestamp;

    public Common(String content, long votes, boolean has_image, String image_url, String timestamp) {
        this.content = content;
        this.votes = votes;
        this.has_image = has_image;
        this.image_url = image_url;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public long getVotes() {
        return votes;
    }

    public boolean isHas_image() {
        return has_image;
    }

    public String getImage_url() {
        return image_url;
    }
}
