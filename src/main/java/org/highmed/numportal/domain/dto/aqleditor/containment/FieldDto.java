package org.highmed.numportal.domain.dto.aqleditor.containment;

import lombok.Data;

@Data
public class FieldDto {

  private String name;
  private String rmType;
  private String aqlPath;
  private String humanReadablePath;
}
