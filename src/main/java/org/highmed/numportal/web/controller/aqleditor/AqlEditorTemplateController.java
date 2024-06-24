package org.highmed.numportal.web.controller.aqleditor;

import java.util.List;
import lombok.AllArgsConstructor;
import org.highmed.numportal.domain.dto.aqleditor.template.TemplateDto;
import org.highmed.numportal.service.aqleditor.AqlEditorTemplateService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/aqleditor/v1/template",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class AqlEditorTemplateController extends BaseController {

  private final AqlEditorTemplateService aqlEditorTemplateService;

  @GetMapping
  public ResponseEntity<List<TemplateDto>> getAll() {
    return ResponseEntity.ok(aqlEditorTemplateService.getAll());
  }
}
