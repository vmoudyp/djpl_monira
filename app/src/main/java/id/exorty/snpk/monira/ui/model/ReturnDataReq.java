package id.exorty.snpk.monira.ui.model;

public class ReturnDataReq {
    private String status;
    private String result;
    private String message;


    public ReturnDataReq(String status, String result, String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

}
