package devRant.Application;

import devRant.Application.Resource.DetailedRant;
import devRant.Application.Resource.Rants;
import devRant.Application.Resource.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RantService {

    public static final String TEMPLATE_BASE = "https://devrant.com/";

    public static final String SORT_ALGO = "algo";
    public static final String SORT_RECENT = "recent";
    public static final String SORT_TOP = "top";

    public static final String TOP_IN_DAY = "day";
    public static final String TOP_IN_WEEK = "week";
    public static final String TOP_IN_MONTH = "month";
    public static final String TOP_ALL = "all";

    private RantFactory factory;

    @Autowired
    public RantService(RantFactory factory) {
        this.factory = factory;
    }

    public HttpEntity<Rants> getFeedService(String sort, String group, int page) throws Exception {

        if(sort.equals(SORT_TOP)){
            if (null == group){
                group = TOP_ALL;
            }
        }else if(group != null){
            throw new Exception("you can't use group query parameter unless you choose to sort by 'top'.");
        }

        Document doc;

        try {

            String template = TEMPLATE_BASE+"%s/%s";
            List<String> params = new ArrayList<String>(){{
                add("feed");
                add(sort);
            }};

            if(null != group){
                template += "/%s";
                params.add(group);
            }

            template += "/%s";
            params.add(Integer.toString(page));

            String url = String.format(template, params.toArray());

            doc = Jsoup.connect(url).get();

            Rants rants = factory.extractAllRantsInDocument(doc, sort, group, page);

            return new ResponseEntity<>(rants, HttpStatus.OK);

        } catch (IOException e) {
            throw e;
        }
    }

    public HttpEntity<DetailedRant> getRantService(long id) throws Exception {

        Document doc;

        try {

            String template = TEMPLATE_BASE+"%s/%d";

            String url = String.format(template, "rants", id);

            doc = Jsoup.connect(url).get();

            DetailedRant rant = factory.extractDetailedRantFromDocument(id, doc);

            return new ResponseEntity<>(rant, HttpStatus.OK);

        } catch (IOException e) {
            throw e;
        }
    }

    public HttpEntity<User> getUserService(String username) throws Exception {

        Document doc;

        try {

            String template = TEMPLATE_BASE+"%s/%s";

            String url = String.format(template, "users", username);

            doc = Jsoup.connect(url).get();

            User user = factory.extractUserDetailsFromDocument(username, doc);

            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (IOException e) {
            throw e;
        }
    }
}
