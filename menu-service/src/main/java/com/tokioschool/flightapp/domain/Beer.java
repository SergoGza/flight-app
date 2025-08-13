package com.tokioschool.flightapp.domain;


import com.tokioschool.flightapp.config.UUIDDocument;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "beers")
public class Beer extends UUIDDocument {

    private String name;
    private String style;

}
