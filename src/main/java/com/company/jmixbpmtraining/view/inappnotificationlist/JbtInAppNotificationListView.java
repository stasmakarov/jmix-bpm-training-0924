package com.company.jmixbpmtraining.view.inappnotificationlist;

import com.company.jmixbpmtraining.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.notificationsflowui.view.inappnotification.InAppNotificationListView;

@Route(value = "ntf/notifications", layout = MainView.class)
@ViewController("ntf_InAppNotification.list")
@ViewDescriptor("jbt-in-app-notification-list-view.xml")
public class JbtInAppNotificationListView extends InAppNotificationListView {
}