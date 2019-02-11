package by.rekuts.giftcertificates.service.impl;

import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.specs.TagSpecification;
import by.rekuts.giftcertificates.service.TagService;
import by.rekuts.giftcertificates.service.converter.TagConverter;
import by.rekuts.giftcertificates.service.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository repository;
    private final TagConverter converter;

    @Autowired
    public TagServiceImpl(TagRepository repository, TagConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Transactional
    @Override
    public String create(TagDto tagDto) {
        return repository.create(converter.dtoConvert(tagDto));
    }

    //not used. Instead of this you may delete tag and create new one.
    @Transactional
    @Override
    public void update(TagDto tagDto) {
        repository.update(converter.dtoConvert(tagDto));
    }

    @Transactional
    @Override
    public void delete(int id) {
        repository.delete(id);
    }

    @Override
    public List<TagDto> getList(Integer page, Integer itemsPerPage) {
        return repository
                .getList(new TagSpecification(), page, itemsPerPage)
                .stream()
                .map(converter::domainConvert)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto getTagByName(String tagName) {
        return repository
                .getList(new TagSpecification(tagName), null, null)
                .stream()
                .map(converter::domainConvert)
                .findFirst()
                .orElse(null);
    }
}
