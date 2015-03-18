package be.rubus.web.valerie.recording;

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
