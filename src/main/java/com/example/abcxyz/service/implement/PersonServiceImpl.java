package com.example.abcxyz.service.implement;

import com.example.abcxyz.configuration.TmdbApi;
import com.example.abcxyz.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    private final TmdbApi tmdbApi;

    public PersonServiceImpl(TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    @Override
    public Object getPersonDetail(Long personId, String language) {
        return this.tmdbApi.getPersonDetail(personId, language);
    }

    @Override
    public Object getPersonMedias(Long personId, String language) {
        return this.tmdbApi.getPersonMedias(personId, language);
    }
}
