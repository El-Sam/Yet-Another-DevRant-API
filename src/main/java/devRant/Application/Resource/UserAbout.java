package devRant.Application.Resource;


public class UserAbout {

    private String about;

    private String skills;

    private String location;

    private String website;

    private String github;

    private long joined_on;

    public UserAbout(String about, String skills, String location, String website, String github, long joined_on) {
        this.about = about;
        this.skills = skills;
        this.location = location;
        this.website = website;
        this.github = github;
        this.joined_on = joined_on;
    }

    public String getAbout() {
        return about;
    }

    public String getSkills() {
        return skills;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getGithub() {
        return github;
    }

    public long getJoined_on() {
        return joined_on;
    }
}
