package search;

public class PageData {
	
	String url;
	String pageDate;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPageDate() {
		
		if(pageDate.length() > 1100){
			pageDate = pageDate.substring(0, 1000);
		}
		return pageDate;
	}
	public void setPageDate(String pageDate) {
		this.pageDate = pageDate;
	}
	

}
