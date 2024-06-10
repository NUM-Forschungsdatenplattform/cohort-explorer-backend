package org.highmed.numportal.service.notification.dto.account;

import org.highmed.numportal.service.email.MessageSourceWrapper;
import org.highmed.numportal.service.notification.dto.Notification;

import lombok.Builder;

public class OrganizationUpdateNotification extends Notification {

  private static final String ORGANIZATION_UPDATE_SUBJECT = "mail.user-organization-update.subject";
  private static final String ORGANIZATION_UPDATE_BODY = "mail.user-organization-update.body";
  private final String organization;
  private final String formerOrganization;

  @Builder
  public OrganizationUpdateNotification(
      String recipientEmail,
      String recipientFirstName,
      String recipientLastName,
      String adminEmail,
      String adminFullName,
      String organization,
      String formerOrganization) {
    this.recipientEmail = recipientEmail;
    this.recipientFirstName = recipientFirstName;
    this.recipientLastName = recipientLastName;
    this.adminFullName = adminFullName;
    this.adminEmail = adminEmail;
    this.organization = organization;
    this.formerOrganization = formerOrganization;
  }

  @Override
  public String getNotificationBody(MessageSourceWrapper messageSource, String url, String operator) {
    String signOff = messageSource.getMessage(SIGN_OFF_KEY, operator);
    return messageSource.getMessage(
        ORGANIZATION_UPDATE_BODY,
        recipientFirstName,
        recipientLastName,
        signOff,
        url,
        adminFullName,
        adminEmail,
        organization,
        formerOrganization);
  }

  @Override
  public String getNotificationSubject(MessageSourceWrapper messageSource) {
    return messageSource.getMessage(ORGANIZATION_UPDATE_SUBJECT);
  }
}
