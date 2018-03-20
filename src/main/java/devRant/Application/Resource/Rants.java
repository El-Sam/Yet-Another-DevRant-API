package devRant.Application.Resource;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class Rants extends ResourceSupport{

    private List<Rant> rants;

    public Rants(){

        this.rants = new ArrayList<>();
    }

    public List<Rant> getRants() {
        return rants;
    }

    public void addRant(Rant rant){
        this.rants.add(rant);
    }
}
