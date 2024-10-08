package com.company.jmixbpmtraining.view.processinstancelist;

import com.company.jmixbpmtraining.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.bpmflowui.view.processinstance.ProcessInstanceListView;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "bpm/processinstances", layout = MainView.class)
@ViewController("bpm_ProcessInstance.list")
@ViewDescriptor("jbt-process-instance-list-view.xml")
public class JbtProcessInstanceListView extends ProcessInstanceListView {

    private static final Logger log = LoggerFactory.getLogger(JbtProcessInstanceListView.class);
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private Dialogs dialogs;

    @Subscribe(id = "deleteAllBtn", subject = "clickListener")
    public void onDeleteAllBtnClick(final ClickEvent<JmixButton> event) {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance pi : instances) {
            String id = pi.getId();
            runtimeService.deleteProcessInstance(id, "deleting all action");
            log.info("Process instance deleted: " + id);
        }

        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .finished().list();
        for (HistoricProcessInstance hpi : historicProcessInstances) {
            String id = hpi.getId();
            historyService.deleteHistoricProcessInstance(id);
            log.info("Historic process instance deleted: " + id);
        }

        dialogs.createMessageDialog()
                .withHeader("Success")
                .withText("All process instances deleted")
                .open();
    }

}