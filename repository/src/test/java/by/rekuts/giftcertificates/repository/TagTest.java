package by.rekuts.giftcertificates.repository;

import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.specs.AllTagsSpecification;
import by.rekuts.giftcertificates.repository.specs.OneTagByNameSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryConfig.class)
@ActiveProfiles("debug")
public class TagTest {

    @Autowired
    private TagRepository repository;

    //todo always return true even if not creates tag
    @Test
    public void createTagTest() {
        Tag tag = new Tag();
        tag.setName("tratatag");
        repository.create(tag);
    }

    @Test
    public void getTagByNameTestTrue() {
        OneTagByNameSpecification specification = new OneTagByNameSpecification("funny");
        Tag tag = repository.getList(specification).get(0);
        Assert.assertEquals("SELECT tag_id, name FROM gift_tag WHERE name = 'funny'", specification.getSqlQuery());
        Assert.assertEquals("funny", tag.getName());
        Assert.assertEquals(0, tag.getTagId());
    }

    @Test
    public void getAllTagsTestTrue() {
        AllTagsSpecification specification = new AllTagsSpecification();
        List<Tag> tags = repository.getList(specification);
        Assert.assertEquals("SELECT tag_id, name FROM gift_tag", specification.getSqlQuery());
        Assert.assertEquals("funny", tags.get(0).getName());
        Assert.assertEquals("wedding", tags.get(1).getName());
    }
}
