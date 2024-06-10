package org.highmed.numportal.service.notification.dto.account;


import org.highmed.numportal.service.email.MessageSourceWrapper;
import org.highmed.numportal.service.notification.dto.Notification;

import lombok.Builder;

public class AccountApprovalNotification extends Notification {

  private static final String ACCOUNT_APPROVAL_SUBJECT = "mail.user-account-approval.subject";
  private static final String ACCOUNT_APPROVAL_BODY = "mail.user-account-approval.body";

  @Builder
  public AccountApprovalNotification(
      String recipientEmail,
      String recipientFirstName,
      String recipientLastName,
      String adminEmail,
      String adminFullName) {
    this.recipientEmail = recipientEmail;
    this.recipientFirstName = recipientFirstName;
    this.recipientLastName = recipientLastName;
    this.adminFullName = adminFullName;
    this.adminEmail = adminEmail;
  }

  @Override
  public String getNotificationBody(MessageSourceWrapper messageSource, String url, String operator) {
    String signOff = messageSource.getMessage(SIGN_OFF_KEY, operator);
    return messageSource.getMessage(
        ACCOUNT_APPROVAL_BODY,
        recipientFirstName,
        recipientLastName,
        adminEmail,
        adminFullName,
        signOff,
        url);
  }

  @Override
  public String getNotificationSubject(MessageSourceWrapper messageSource) {
    return messageSource.getMessage(ACCOUNT_APPROVAL_SUBJECT);
  }
}
