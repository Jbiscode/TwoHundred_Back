package org.duckdns.bidbuy.global.common.entity;

import java.time.LocalDateTime;

import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
@Setter
public class BaseEntity {

  @CreatedDate
  private LocalDateTime createdDate;
  @LastModifiedDate
  private LocalDateTime modifiedDate;
}
