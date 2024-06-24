package org.highmed.numportal.domain.dto.aqleditor.containment;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ContainmentDto {

  private final List<ContainmentDto> children = new ArrayList<>();
  private final List<FieldDto> fields = new ArrayList<>();
  private String archetypeId;
}
