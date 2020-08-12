package get.idcard.code;

public class ResuletFields {

	private Integer code = 0;	//0:false,1:true;
	private String msg = null;
	
	public ResuletFields() {
		super();
	}
	public ResuletFields(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
