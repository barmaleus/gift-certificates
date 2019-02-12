package by.rekuts.giftcertificates.service.dto;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class TagDto extends ResourceSupport {
    private int tagId;
    private String name;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", name='" + name + '\'' +
                '}';
    }
}
