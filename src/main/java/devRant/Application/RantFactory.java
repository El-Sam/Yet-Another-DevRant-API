package devRant.Application;

import devRant.Api.Controller;
import devRant.Application.Resource.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class RantFactory {

    /**
     * @param document: document containing the HTML of the list of rants in the current page
     * @param sort: type of sorting used, i.e `algo`, `top` or `recent`
     * @param group: group used in case of sorting by top rants, can be `all`, `day`, `week` or `month`
     * @param page: current page
     * @return Rants containing list of rants
     * @throws Exception: thrown in case of unexpected error
     */
    public Rants extractAllRantsInDocument(Document document, String sort, String group, int page) throws Exception {

        Rants rants = new Rants();

        Elements elements_rants = document.select(".rant-comment-row-widget");

        for (Element element : elements_rants) {

            Rant newRant = extractRantFromElement(element);
            rants.addRant(newRant);
        }

        rants.add(linkTo(methodOn(Controller.class).feed(sort, group, page)).withSelfRel());

        return rants;

    }

    public DetailedRant extractDetailedRantFromDocument(long id, Document document) throws Exception {

        Element rantDetails = document.selectFirst(".post-details-container.rant-comment-row-widget");

        String voteCount = rantDetails.selectFirst(".votecount").text();

        String username = rantDetails.selectFirst(".rant-username").text();

        String timestamp = rantDetails.selectFirst(".rant-timestamp").text();

        String content = rantDetails.selectFirst(".rantlist-content").text();

        Optional<Element> imageElement = Optional.ofNullable(rantDetails.selectFirst(".rant-image .details-image"));

        String imageUrl = null;

        if(imageElement.isPresent()){
            imageUrl = imageElement.get().attr("href");
        }

        Elements tagsElement = rantDetails.select(".rantlist-content-tag");

        List<String> tags = new ArrayList<>();

        for (Element tag: tagsElement) {
            tags.add(tag.text());
        }

        Elements replies = document.select(".reply-row.rant-comment-row-widget");

        List<Comment> comments = this.extractComments(replies);

        DetailedRant rant = new DetailedRant(
                id,
                username,
                Long.parseLong(voteCount),
                content,
                imageUrl,
                (null != imageUrl),
                timestamp,
                tags,
                comments.size(),
                comments
        );

        rant.add(linkTo(methodOn(Controller.class).rant(id)).withSelfRel());
        rant.add(linkTo(methodOn(Controller.class).user(username)).withRel("user"));

        return rant;
    }

    public User extractUserDetailsFromDocument(String username, Document document) throws Exception {

        Element profileBanner = document.selectFirst(".profile-banner");

        String avatar_url = profileBanner
                .selectFirst("div.profile-avatar-bg")
                .selectFirst("img").attr("src");

        String score = profileBanner
                .selectFirst("div.profile-username-container")
                .selectFirst(".profile-extras")
                .selectFirst(".profile-score").text();

        UserAbout about = getUserProfile(document);

        UserActivityStats stats = getUserActivityStats(document);

        User user = new User(
                avatar_url,
                username,
                Long.parseLong(score),
                stats,
                about
        );

        user.add(linkTo(methodOn(Controller.class).user(username)).withSelfRel());

        return user;
    }

    public Rants getUserRants(String username, Document doc) throws Exception {

        Elements rantsList = doc
                .selectFirst("div.profile-page")
                .selectFirst(".rantlist-bg")
                .selectFirst(".rantlist")
                .select(".rant-comment-row-widget")
                ;

        Rants rants = new Rants();

        for (Element element: rantsList){

            Rant rant = extractRantFromElement(element);
            rants.addRant(rant);
        }

        rants.add(linkTo(methodOn(Controller.class).userRants(username)).withSelfRel());
        rants.add(linkTo(methodOn(Controller.class).user(username)).withRel("user"));

        return rants;

    }



    ///////////////////////////////////
    ///     Protected methods       ///
    ///////////////////////////////////

    /**
     * @param document document containing the whole page of the user
     * @return extracted the details about the user
     */
    protected UserAbout getUserProfile(Document document) throws ParseException {

        Element profileDetails = document.selectFirst("div.box-profile").selectFirst(".box-content");

        Elements details = profileDetails.getElementsByTag("li");

        String about = null;
        String location = null;
        String website = null;
        String github = null;
        String skills = null;

        for (Element li: details) {

            String section = li.selectFirst(".profile-detail-col1").text();
            String value = li.selectFirst(".profile-detail-col2").text();

            switch (section){
                case "About":
                    about = value;
                    break;
                case "Skills":
                    skills = value;
                    break;
                case "Location":
                    location = value;
                    break;
                case "Website":
                    website = value;
                    break;
                case "Github":
                    github = value;
                    break;
            }
        }

        String joinedOn = profileDetails.selectFirst("div.profile-join-date").text();

        Pattern pattern = Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4}");

        Matcher matcher = pattern.matcher(joinedOn);

        long joinedOnTimestamp = 0;

        while (matcher.find()) {

            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

            Date date = formatter.parse(matcher.group(0));

            joinedOnTimestamp= date.getTime() / 1000;

        }

        return new UserAbout(
                about,
                skills,
                location,
                website,
                github,
                joinedOnTimestamp
        );
    }

    /**
     * @param document document containing the whole page of the user
     * @return extracted activity stats of the user
     */
    protected UserActivityStats getUserActivityStats(Document document){

        Elements activityElements = document
                .selectFirst("div.profile-page")
                .selectFirst(".profile-tabs")
                .getElementsByTag("a");

        long rantsCount = 0;
        long upvotesCount = 0;
        long commentsCount = 0;
        long favoritesCount = 0;

        for (Element element:activityElements) {

            String label = element.selectFirst(".tab-label").text();
            String count = element.selectFirst(".tab-count").text();

            switch (label){
                case "Rants":
                    rantsCount = Long.parseLong(count);
                    break;
                case "++'s":
                    upvotesCount = Long.parseLong(count);
                    break;
                case "Comments":
                    commentsCount = Long.parseLong(count);
                    break;
                case "Favorites":
                    favoritesCount = Long.parseLong(count);
                    break;
            }
        }

        return new UserActivityStats(
                rantsCount,
                upvotesCount,
                commentsCount,
                favoritesCount
        );
    }

    /**
     * @param commentElement: Html Element containing the comment's data
     * @return Comment resource
     */
    protected Comment extractComment(Element commentElement) throws Exception {

        String votes = commentElement.selectFirst(".votecount").text();

        String userName = commentElement.selectFirst(".user-name").text();

        String score = commentElement.selectFirst(".user-score span").text();

        String replyTimestamp = commentElement.selectFirst(".timestamp").text();

        String replyContent = commentElement.selectFirst(".rantlist-title").text();

        String replyImageUrl = null;

        Optional<Element> image = Optional.ofNullable(commentElement.selectFirst(".rant-image .details-image"));

        if(image.isPresent()){
            replyImageUrl = image.get().attr("href");
        }

        Comment comment = new Comment(
                userName,
                Long.parseLong(votes),
                replyContent,
                Long.parseLong(score),
                replyTimestamp,
                (null == replyImageUrl),
                replyImageUrl
        );

        comment.add(linkTo(methodOn(Controller.class).user(userName)).withSelfRel());

        return comment;
    }

    /**
     * @param commentsElements: HTML elements containing all comments
     * @return : list of comment resources
     */
    protected List<Comment> extractComments(Elements commentsElements) throws Exception {

        List<Comment> comments = new ArrayList<>();

        for (Element reply : commentsElements) {

            Comment comment = this.extractComment(reply);
            comments.add(comment);
        }

        return comments;
    }

    protected List<String> getTags(Element parentElementOfTags){

        List<String> tags = new ArrayList<>();

        Element tagsElement = parentElementOfTags.selectFirst(".rantlist-tags");

        for (Element tag: tagsElement.children()) {
            tags.add(tag.text());
        }

        return tags;
    }

    protected long getCommentsCount(Element parentElement){

        long commentsCount = 0L;

        Optional<Element> commentCountElement = Optional.ofNullable(parentElement.selectFirst(".commment-num"));

        if(commentCountElement.isPresent()){
            commentsCount = Long.parseLong(commentCountElement.get().text());
        }

        return commentsCount;
    }

    protected Rant extractRantFromElement(Element rantElement) throws Exception {

        String id = rantElement.attr("data-id");

        String votes = rantElement.selectFirst(".votecount").text();

        String contentText = rantElement.select(".rantlist-title-text").text();

        Optional<Element> imageElement = Optional.ofNullable(rantElement.selectFirst(".rant-image"));

        String imageUrl = null;

        if(imageElement.isPresent()){
            imageUrl = imageElement.get().getElementsByTag("img").attr("src");
        }

        List<String> tags = getTags(rantElement);

        Long commentsCount = getCommentsCount(rantElement);

        Rant newRant = new Rant(
                Long.parseLong(id),
                Long.parseLong(votes),
                contentText,
                imageUrl,
                null != imageUrl,
                tags,
                commentsCount
        );

        newRant.add(linkTo(methodOn(Controller.class).rant(Long.parseLong(id))).withSelfRel());

        return newRant;
    }
}
