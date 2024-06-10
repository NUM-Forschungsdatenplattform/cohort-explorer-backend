package org.highmed.numportal.service.notification.dto;

import org.highmed.numportal.domain.model.ProjectStatus;
import org.highmed.numportal.service.email.MessageSourceWrapper;

import lombok.Builder;

public class ProjectStatusChangeRequestNotification extends ProjectStatusChangeNotification {

  @Builder(builderMethodName = "changeRequestBuilder")
  public ProjectStatusChangeRequestNotification(String recipientEmail, String recipientFirstName,
      String recipientLastName,
      String approverFirstName,
      String approverLastName,
      String projectTitle,
      ProjectStatus projectStatus,
      ProjectStatus oldProjectStatus,
      long projectId,
      String approverEmail) {
    super(
        recipientEmail, recipientFirstName, recipientLastName, approverFirstName, approverLastName, projectTitle, projectStatus, oldProjectStatus,
        projectId, approverEmail);
  }

  @Override
  public String getNotificationBody(MessageSourceWrapper messageSource, String url, String operator) {
    String signOff = messageSource.getMessage(SIGN_OFF_KEY, operator);
    return messageSource.getMessage(
        PROJECT_STATUS_CHANGE_BODY_KEY,
        recipientFirstName,
        recipientLastName,
        projectTitle,
        projectStatus,
        approverFirstName,
        approverLastName,
        signOff,
        url,
        getProjectEditUrl(url, projectId),
        oldProjectStatus,
        approverEmail);
  }
}
