/*
 * Copyright 2014-2016 Rudy De Busscher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package be.rubus.web.valerie.recording;

import be.rubus.web.jerry.provider.BeanProvider;

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
