package com.example.abcxyz.service;

public interface PersonService {

    Object getPersonDetail(Long personId, String language);

    Object getPersonMedias(Long personId, String language);
}
