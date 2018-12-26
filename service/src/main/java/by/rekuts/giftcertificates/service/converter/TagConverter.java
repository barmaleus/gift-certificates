package by.rekuts.giftcertificates.service.converter;

import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.service.dto.TagDto;

public class TagConverter implements GiftCertConverter<Tag, TagDto> {
    @Override
    public Tag dtoConvert(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setTagId(tagDto.getTagId());
        tag.setName(tagDto.getName());
        return tag;
    }

    @Override
    public TagDto domainConvert(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setTagId(tag.getTagId());
        tagDto.setName(tag.getName());
        return tagDto;
    }
}
