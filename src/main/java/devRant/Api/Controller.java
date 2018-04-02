package devRant.Api;

import devRant.Application.RantService;
import devRant.Application.Resource.DetailedRant;
import devRant.Application.Resource.Rant;
import devRant.Application.Resource.Rants;
import devRant.Application.Resource.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private RantService rantService;

    @Autowired
    public Controller(RantService rantService) {
        this.rantService = rantService;
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public HttpEntity<Rants> feed(
            @RequestParam(value = "sort", required = false, defaultValue = RantService.SORT_ALGO) String sort,
            @RequestParam(value = "group", required = false) String group,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) throws Exception {

        return rantService.getFeedService(sort, group, page);
    }

    @RequestMapping(value = "/rants/{id}", method = RequestMethod.GET)
    public HttpEntity<DetailedRant> rant(@PathVariable("id") long id) throws Exception {

        return rantService.getRantService(id);
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public HttpEntity<User> user(@PathVariable("username") String username) throws Exception {
        return rantService.getUserService(username);
    }

    @RequestMapping(value = "/users/{username}/rants", method = RequestMethod.GET)
    public HttpEntity<Rants> userRants(@PathVariable("username") String username) throws Exception {
        return rantService.getUserRantsService(username);
    }
}
