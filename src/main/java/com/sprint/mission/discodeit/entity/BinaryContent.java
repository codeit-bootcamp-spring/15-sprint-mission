package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "binary_contents")
@Getter
public class BinaryContent extends BaseEntity {
    @Column(length = 255, nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false,length = 100)
    private String contentType;

    @Column(nullable = false)
    private byte[] bytes;

    protected BinaryContent(){
    }

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        super();
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
