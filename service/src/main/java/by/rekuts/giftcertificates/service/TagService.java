package by.rekuts.giftcertificates.service;

import by.rekuts.giftcertificates.service.dto.TagDto;

public interface TagService extends CrudService<TagDto> {
    /**
     * @return object from database searched by name
     */
    TagDto getTagByName(String tagName);
}
