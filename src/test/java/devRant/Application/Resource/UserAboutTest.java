package devRant.Application.Resource;

import junit.framework.TestCase;

import java.time.LocalDateTime;

public class UserAboutTest extends TestCase {

    public void testConstructWhereAllArgsAreNotNull(){

        String about = "bla bla bla";
        String skills = "Java, Python ...";
        String location = "Somewhere";
        String website = "www.example.com";
        String github = "blabla";
        long joinedOn = LocalDateTime.now().getSecond();


        UserAbout userAbout = new UserAbout(
                about,
                skills,
                location,
                website,
                github,
                joinedOn
        );

        assertEquals(about, userAbout.getAbout());
        assertEquals(skills, userAbout.getSkills());
        assertEquals(location, userAbout.getLocation());
        assertEquals(website, userAbout.getWebsite());
        assertEquals(github, userAbout.getGithub());
        assertEquals(joinedOn, userAbout.getJoined_on());
    }

    public void testConstructWhereSomeArgsAreNull(){

        String about = "bla bla bla";
        String skills = "Java, Python ...";
        long joinedOn = LocalDateTime.now().getSecond();


        UserAbout userAbout = new UserAbout(
                about,
                skills,
                null,
                null,
                null,
                joinedOn
        );

        assertEquals(about, userAbout.getAbout());
        assertEquals(skills, userAbout.getSkills());
        assertNull( userAbout.getLocation());
        assertNull(userAbout.getWebsite());
        assertNull(userAbout.getGithub());
        assertEquals(joinedOn, userAbout.getJoined_on());
    }
}
