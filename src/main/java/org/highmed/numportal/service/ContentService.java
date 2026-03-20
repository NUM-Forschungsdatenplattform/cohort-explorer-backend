package org.highmed.numportal.service;

import org.highmed.numportal.domain.dto.CardDto;
import org.highmed.numportal.domain.dto.MetricsDto;
import org.highmed.numportal.domain.dto.NavigationItemDto;
import org.highmed.numportal.domain.dto.ProjectInfoDto;
import org.highmed.numportal.domain.model.Content;
import org.highmed.numportal.domain.model.ContentType;
import org.highmed.numportal.domain.repository.ContentItemRepository;
import org.highmed.numportal.service.ehrbase.EhrBaseService;
import org.highmed.numportal.service.exception.SystemException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.openehr.sdk.response.dto.QueryResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.highmed.numportal.domain.templates.ExceptionsTemplate.COULDN_T_PARSE_CARD;
import static org.highmed.numportal.domain.templates.ExceptionsTemplate.COULDN_T_PARSE_NAVIGATION_CONTENT;
import static org.highmed.numportal.domain.templates.ExceptionsTemplate.COULDN_T_SAVE_CARD;
import static org.highmed.numportal.domain.templates.ExceptionsTemplate.COULDN_T_SAVE_NAVIGATION_CONTENT;

@Slf4j
@Service
public class ContentService {

  public static final String LIST_CLINICS =
      "SELECT distinct c/context/health_care_facility/name as health_care_facility FROM EHR e CONTAINS COMPOSITION c";
  private static final int PROJECT_COUNT = 5;

  private final ContentItemRepository contentItemRepository;
  private final ObjectMapper mapper;
  private final ProjectService projectService;
  private final AqlService aqlService;
  private final EhrBaseService ehrBaseService;
  private final UserDetailsService userDetailsService;

  @Autowired
  public ContentService(
      ContentItemRepository contentItemRepository,
      ObjectMapper mapper,
      @Lazy ProjectService projectService,
      AqlService aqlService,
      EhrBaseService ehrBaseService,
      UserDetailsService userDetailsService) {
    this.contentItemRepository = contentItemRepository;
    this.mapper = mapper;
    this.projectService = projectService;
    this.aqlService = aqlService;
    this.ehrBaseService = ehrBaseService;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Retrieves info about the latest five projects
   *
   * @return the five latest projects
   */
  public List<ProjectInfoDto> getLatestProjects(List<String> roles) {
    return projectService.getLatestProjectsInfo(PROJECT_COUNT, roles);
  }

  /**
   * Computes platform metrics: number of projects and number of aqls
   *
   * @return the metrics
   */
  public MetricsDto getMetrics() {
    return MetricsDto.builder()
                     .aqls(aqlService.countAqls())
                     .projects(projectService.countProjects())
                     .build();
  }

  public String getNavigationItems() {
    try {
      List<Content> contents =
          contentItemRepository.findByType(ContentType.NAVIGATION).orElse(new ArrayList<>());
      if (contents.isEmpty()) {
        return "[]";
      } else {
        return contents.get(0).getContent();
      }
    } catch (Exception e) {
      log.error("Couldn't parse navigation content", e);
      throw new SystemException(
          ContentService.class, COULDN_T_PARSE_NAVIGATION_CONTENT, String.format(COULDN_T_PARSE_NAVIGATION_CONTENT, e.getMessage()));
    }
  }

  public void setNavigationItems(List<NavigationItemDto> navigationItemDtos) {
    try {
      List<Content> contents =
          contentItemRepository.findByType(ContentType.NAVIGATION).orElse(new ArrayList<>());
      Content navigation;
      if (contents.isEmpty()) {
        navigation = Content.builder().type(ContentType.NAVIGATION).build();
      } else {
        navigation = contents.get(0);
      }

      navigation.setContent(mapper.writeValueAsString(navigationItemDtos));
      contentItemRepository.save(navigation);
    } catch (Exception e) {
      log.error("Couldn't save navigation content", e);
      throw new SystemException(ContentService.class, COULDN_T_SAVE_NAVIGATION_CONTENT,
          String.format(COULDN_T_SAVE_NAVIGATION_CONTENT, e.getMessage()));
    }
  }

  public String getCards() {
    try {
      List<Content> contents =
          contentItemRepository.findByType(ContentType.CARD).orElse(new ArrayList<>());
      if (contents.isEmpty()) {
        return "[]";
      } else {

        return contents.get(0).getContent();
      }
    } catch (Exception e) {
      log.error("Couldn't parse card", e);
      throw new SystemException(ContentService.class, COULDN_T_PARSE_CARD,
          String.format(COULDN_T_PARSE_CARD, e.getMessage()));
    }
  }

  public void setCards(List<CardDto> cardDtos) {
    try {
      List<Content> contents =
          contentItemRepository.findByType(ContentType.CARD).orElse(new ArrayList<>());
      Content navigation;
      if (contents.isEmpty()) {
        navigation = Content.builder().type(ContentType.CARD).build();
      } else {
        navigation = contents.get(0);
      }

      navigation.setContent(mapper.writeValueAsString(cardDtos));
      contentItemRepository.save(navigation);
    } catch (Exception e) {
      log.error("Couldn't save card", e);
      throw new SystemException(ContentService.class, COULDN_T_SAVE_CARD,
          String.format(COULDN_T_SAVE_CARD, e.getMessage()));
    }
  }

  public List<String> getClinics(String loggedInUserId) {
    userDetailsService.checkIsUserApproved(loggedInUserId);
    QueryResponseData responseData = ehrBaseService.executePlainQuery(LIST_CLINICS);
    return responseData.getRows().stream()
                       .map(row -> (String) row.get(0))
                       .collect(Collectors.toList());
  }
}
