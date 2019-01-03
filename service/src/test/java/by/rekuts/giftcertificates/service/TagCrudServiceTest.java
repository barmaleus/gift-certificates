package by.rekuts.giftcertificates.service;

import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.repos.impl.TagRepositoryImpl;
import by.rekuts.giftcertificates.repository.specs.TagSpecification;
import by.rekuts.giftcertificates.service.converter.TagConverter;
import by.rekuts.giftcertificates.service.dto.TagDto;
import by.rekuts.giftcertificates.service.impl.TagServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class TagCrudServiceTest {
    private TagRepository repository = mock(TagRepositoryImpl.class);
    private TagConverter converter = new TagConverter();
    private TagService service = new TagServiceImpl(repository, converter);
    private List<TagDto> tagDtos;

    @Before
    public void init() {
        TagDto tagDto1 = new TagDto();
        tagDto1.setTagId(1);
        tagDto1.setName("funny");
        TagDto tagDto2 = new TagDto();
        tagDto2.setTagId(8);
        tagDto2.setName("wedding");
        tagDtos = Arrays.asList(tagDto1, tagDto2);
    }

    @Test
    public void createTagTestTrue() throws ServiceException{
        when(repository.create(any(Tag.class))).thenReturn("/tag/" + tagDtos.get(0).getName());
        String path = service.create(tagDtos.get(0));
        service.create(tagDtos.get(1));
        verify(repository, times(2)).create(any());
        Assert.assertEquals("/tag/funny", path);
    }

    @Test
    public void updateTagTestTrue() throws ServiceException{
        doNothing().when(repository).update(any(Tag.class));
        service.update(tagDtos.get(0));
        service.update(tagDtos.get(1));
        verify(repository, times(2)).update(any());
    }

    @Test
    public void deleteTagTestTrue() {
        doNothing().when(repository).delete(anyInt());
        service.delete(0);
        service.delete(1);
        verify(repository, times(2)).delete(anyInt());
    }

    @Test
    public void getAllTagsTestTrue() {
        when(repository.getList(any(TagSpecification.class)))
                .thenReturn(
                        tagDtos
                                .stream()
                                .map(el -> converter.dtoConvert(el))
                                .collect(Collectors.toList())
                );
        List<TagDto> tagDtos = service.getList();
        Assert.assertEquals("funny", tagDtos.get(0).getName());
        Assert.assertEquals("wedding", tagDtos.get(1).getName());
    }

    @Test
    public void getTagsByNameTestTrue() {
        when(repository.getList(any(TagSpecification.class)))
                .thenReturn(Collections.singletonList(converter.dtoConvert(tagDtos.get(0))));
        TagDto tagDto = service.getTagByName("funny");
        Assert.assertEquals("funny", tagDto.getName());
    }
}
