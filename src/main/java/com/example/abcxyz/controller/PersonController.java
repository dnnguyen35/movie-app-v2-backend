package com.example.abcxyz.controller;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{personId}")
    public ResponseEntity<ApiResponse<Object>> getPersonDetail(@PathVariable("personId") Long personId,
                                                               @RequestHeader(value = "Accept-Language",
                                                                       defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "Success", this.personService.getPersonDetail(personId,
                language)));
    }

    @GetMapping("/{personId}/medias")
    public ResponseEntity<ApiResponse<Object>> getPersonMedias(@PathVariable("personId") Long personId,
                                                               @RequestHeader(value = "Accept-Language",
                                                                       defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "Success", this.personService.getPersonMedias(personId,
                language)));
    }
}
