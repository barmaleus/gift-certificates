package by.rekuts.giftcertificates.service.converter;

import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.service.dto.TagDto;

public class TagConverter implements CommonConverter<Tag, TagDto> {
    @Override
    public Tag dtoConvert(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setId(tagDto.getTagId());
        tag.setName(tagDto.getName());
        return tag;
    }

    @Override
    public TagDto domainConvert(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setTagId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }
}
