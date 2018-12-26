package by.rekuts.giftcertificates.service.impl;

import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.specs.AllTagsSpecification;
import by.rekuts.giftcertificates.repository.specs.OneTagByNameSpecification;
import by.rekuts.giftcertificates.service.TagService;
import by.rekuts.giftcertificates.service.dto.TagDto;
import by.rekuts.giftcertificates.service.converter.TagConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Override
    public String create(TagDto tagDto) {
        return repository.create(converter.dtoConvert(tagDto));
    }

    //not used. Instead of this you may delete tag and create new one.
    @Override
    public void update(TagDto tagDto) {
        repository.update(converter.dtoConvert(tagDto));
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }

    @Override
    public List<TagDto> getList() {
        List<Tag> tags = repository.getList(new AllTagsSpecification());
        return tags.stream()
                .map(converter::domainConvert)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto getTagByName(String tagName) {
        OneTagByNameSpecification spec = new OneTagByNameSpecification(tagName);
        List<Tag> tags = repository.getList(spec);
        Optional<TagDto> optionalTagDto = tags.stream()
                .map(converter::domainConvert)
                .findFirst();
///////////// return equals code below ////////////////
//        if (optionalTagDto.isPresent()) {
//            return optionalTagDto.get();
//        } else {
//            return null;
//        }
        return optionalTagDto.orElse(null);
    }
}
