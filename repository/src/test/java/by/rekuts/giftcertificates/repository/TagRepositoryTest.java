package by.rekuts.giftcertificates.repository;

import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.repos.impl.TagRepositoryImpl;
import by.rekuts.giftcertificates.repository.specs.TagSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TagRepositoryImpl.class, TestConfig.class})
@ActiveProfiles("debug")
@Transactional
public class TagRepositoryTest {

    @Autowired
    private TagRepository repository;

    @Test
    public void createTagTest() {
        Tag tag = new Tag();
        tag.setName("tratatag1");
        String path = repository.create(tag);
        Assert.assertEquals("/tags/tratatag1", path);
    }

    @Test
    public void getTagByNameTestTrue() {
        Tag tag = repository.getList(new TagSpecification("funny")).get(0);
        Assert.assertEquals("funny", tag.getName());
        Assert.assertEquals(0, tag.getId());
    }

    @Test
    public void getAllTagsTestTrue() {
        List<Tag> tags = repository.getList(new TagSpecification());
        Assert.assertEquals(5, tags.size());
        Assert.assertEquals("funny", tags.get(0).getName());
        Assert.assertEquals("wedding", tags.get(1).getName());
    }
}
