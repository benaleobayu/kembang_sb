package com.bca.byc.model;

import java.io.Serializable;

public record ChanelIndexResponse(

        String id,

        String name,

        Integer orders,

        String updatedAt,

        String description,

        String logo,

        String privacy
)  implements Serializable {
}
