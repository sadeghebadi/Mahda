package apl.vada.lib.db.model;

@SuppressWarnings("CanBeFinal")
public class FTSModel {

    private String mId; // or UUID recommended
    private int mTableCategory;
    private String mData;

    @SuppressWarnings("unused")
    public FTSModel(String id, int tableCategory, String data) {
        this.mId = id;
        this.mTableCategory = tableCategory;
        this.mData = data;
    }

    @SuppressWarnings("unused")
    public FTSModel(String id, String data) {
        this.mId = id;
        this.mData = data;
    }

    public String getData() {
        return mData;
    }

    public String getId() {
        return mId;
    }

    public int getTableCategory() {
        return mTableCategory;
    }

}
