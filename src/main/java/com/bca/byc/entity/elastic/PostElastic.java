package com.bca.byc.entity.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post")
public class PostElastic {

    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long id;

    @Field(name = "secure_id", type = FieldType.Text)
    private String secureId;

    @Field(name = "description", type = FieldType.Text)
    private String description;

    @Field(name = "content_type", type = FieldType.Text)
    private String contentType;

    @Field(name = "is_commentable", type = FieldType.Boolean)
    private Boolean isCommentable;

    @Field(name = "is_shareable", type = FieldType.Boolean)
    private Boolean isShareable;

    @Field(name = "is_show_likes", type = FieldType.Boolean)
    private Boolean isShowLikes;

    @Field(name = "is_posted", type = FieldType.Boolean)
    private Boolean isPosted;

    @Field(name = "report_status", type = FieldType.Text)
    private String reportStatus;

    @Field(name = "is_active", type = FieldType.Boolean)
    private Boolean isActive;


    @Field(name = "like_count", type = FieldType.Long)
    private Long likeCount;

    @Field(name = "comment_count", type = FieldType.Long)
    private Long commentCount;

    @Field(name = "share_count", type = FieldType.Long)
    private Long ShareCount;



    @Field(name = "post_location_id", type = FieldType.Text)
    private String postLocationId;

    @Field(name = "post_category_id", type = FieldType.Text)
    private String postCategoryId;

    @Field(name = "user_id", type = FieldType.Text)
    private String userId;
}
