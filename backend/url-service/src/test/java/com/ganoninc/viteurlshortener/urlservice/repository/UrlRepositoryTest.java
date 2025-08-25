package com.ganoninc.viteurlshortener.urlservice.repository;

import com.ganoninc.viteurlshortener.urlservice.model.UrlMapping;
import com.ganoninc.viteurlshortener.urlservice.util.FakeUrlMapping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UrlRepositoryTest {
    @Autowired
    private UrlRepository urlRepository;

    @Test
    public void itShouldSaveAndFindAllByUserEmail(){
        UrlMapping urlMapping1 = FakeUrlMapping.builder().build();
        UrlMapping urlMapping2 = FakeUrlMapping.builder().build();
        List<UrlMapping> expectedList = List.of(urlMapping1, urlMapping2);

        urlRepository.save(urlMapping1);
        urlRepository.save(urlMapping2);

        List<UrlMapping> foundUrlMappings = urlRepository.findAllByUserEmail(urlMapping1.getUserEmail());

        assertEquals(expectedList, foundUrlMappings);
    }
}
