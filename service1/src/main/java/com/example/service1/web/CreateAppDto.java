package com.example.service1.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
class CreateAppDto {

    private String file;
    private String name;
    private String price;
    private String shortDescription;
    private String longDescription;
    private String galleryImage;
    private String version;
    private String binary;
    private String appType;
}
