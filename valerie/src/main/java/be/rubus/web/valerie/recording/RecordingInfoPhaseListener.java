package be.rubus.web.valerie.recording;

import be.rubus.web.jerry.provider.BeanProvider;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 */
public class RecordingInfoPhaseListener implements PhaseListener {
    @Override
    public void afterPhase(PhaseEvent event) {
        RecordingInfoManager recordingInfoManager = BeanProvider.getContextualReference(RecordingInfoManager.class);
        if (!recordingInfoManager.processClassLevelConstraints(event.getFacesContext())) {
            event.getFacesContext().renderResponse();
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.PROCESS_VALIDATIONS;
    }
}
