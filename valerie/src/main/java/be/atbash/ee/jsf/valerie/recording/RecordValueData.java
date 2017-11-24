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
 */
package be.atbash.ee.jsf.valerie.recording;

/**
 *
 */
public class RecordValueData {

    private RecordValueInfo recordValueInfo;
    private Object data;

    public RecordValueData(RecordValueInfo recordValueInfo, Object data) {
        this.recordValueInfo = recordValueInfo;
        this.data = data;
    }

    public RecordValueInfo getRecordValueInfo() {
        return recordValueInfo;
    }

    public void setRecordValueInfo(RecordValueInfo recordValueInfo) {
        this.recordValueInfo = recordValueInfo;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}