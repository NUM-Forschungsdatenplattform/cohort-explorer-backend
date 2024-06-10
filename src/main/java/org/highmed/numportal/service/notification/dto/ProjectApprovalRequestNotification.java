package org.highmed.numportal.service.notification.dto;

import org.highmed.numportal.service.email.MessageSourceWrapper;

import lombok.Builder;

public class ProjectApprovalRequestNotification extends Notification {

  private static final String PROJECT_REQUEST_SUBJECT_KEY = "mail.project-pending-approval.subject";
  private static final String PROJECT_REQUEST_BODY_KEY = "mail.project-pending-approval.body";

  private final String coordinatorFirstName;
  private final String coordinatorLastName;
  private final String projectTitle;
  private final Long projectId;

  private final String coordinatorEmail;

  @Builder
  public ProjectApprovalRequestNotification(
      String recipientEmail,
      String recipientFirstName,
      String recipientLastName,
      String coordinatorFirstName,
      String coordinatorLastName,
      String projectTitle,
      Long projectId, String coordinatorEmail) {

    this.recipientEmail = recipientEmail;
    this.recipientFirstName = recipientFirstName;
    this.recipientLastName = recipientLastName;
    this.coordinatorFirstName = coordinatorFirstName;
    this.coordinatorLastName = coordinatorLastName;
    this.projectTitle = projectTitle;
    this.projectId = projectId;
    this.coordinatorEmail = coordinatorEmail;
  }

  @Override
  public String getNotificationSubject(MessageSourceWrapper messageSource) {
    return messageSource.getMessage(PROJECT_REQUEST_SUBJECT_KEY);
  }

  @Override
  public String getNotificationBody(MessageSourceWrapper messageSource, String url, String operator) {
    String signOff = messageSource.getMessage(SIGN_OFF_KEY, operator);
    return messageSource.getMessage(
        PROJECT_REQUEST_BODY_KEY,
        recipientFirstName,
        recipientLastName,
        coordinatorFirstName,
        coordinatorLastName,
        projectTitle,
        signOff,
        url,
        getProjectReviewUrl(url, projectId),
        coordinatorEmail);
  }
}
