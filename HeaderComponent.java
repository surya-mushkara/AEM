package apps.singtel.components.content.header;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletRequest;

import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.jcr.Node;
import com.adobe.cq.slightly.WCMUsePojo;
import com..apache.sling.settings.SlingSettingsServices;

import javax.servlet.http.HttpSession;

public class HeaderComponent extends WCMUsePojo {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private SlingHttpServletRequest request;
	private Page currentPage;
	private ResourceResolver resourceResolver;
	private Resource resource;
	private ValueMap props;
	
	private String readHeaderRootPath;
	private int maxItemShown;
	
	private boolean active = flase;
	private String finalText = "";

    private String navMenuRootPath;
	private String navTitle;
	private String navTitlePath;
	
	
	private String resourcePath;
	private String navigationPagePath;
	
	private String runMode;
	
	@Override
	public void activate() throws Exception {
		currentPage = getCurrentPage();
		request = getRequest();
		resourceResolver = request.getResourceResolver();
		
		readHeaderRootPath = getProperties().get("rootPath","/content/Singtel/sg/en");
		maxItemShown = Integer.parseInt(getProperties().get("maxItemShown", "5"));
		
		resourcePath = get("resourcePath",String.class);
		navTitle = get("navTitle",String.class);
		navTitlePath = get("navTitlePath",String.class);
		navMenuRootPath = get("navMenuRootPath",String.class);
		navigationPagePath = get("navPagePath" , String.class);
		
		
		runMode = getSlingScriptHelper().getService(SlingSettingsServices.class).getRunModes().toString();
		
	}
	
	public boolean isActive() {
		String currentPath = currentPage.getPath();
		
		if(currentPage.indexOf(navMenuRootPath) > -1){
			active = true;
		}else{
			active = false;
		}
		
		return active;
		
	}
		
	public int getMaxItemShown(){
		return maxItemShown;
	}
	
	private List<Page> getChildPages(String root){
		List<Page> results = new ArrayList<Page>();
		
		try
		{
			ResourceResolver resourceResolver = request.getResourceResolver();
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			Page rootPage = pageManager.getPage(root);
			
			PageFilter pgFilter = new PageFilter(request);
			Iterator<Page> navChildren =rootPage.listChildren(pgFilter);
			
			while(navChildren.hasNext()){
				Page child = navChildren.next();
				results.add(child);
			}
			
		}
		catch (Exception ex)
		{
			logger.error("Header -> getChildPages: "+ex.getMessage());
			ex.printStackTrace();
			
		}
		
		return results;
		
	}
	
	public List<Page> getChildrenList(){
		String root = getRootPath();
		List<Page> childrenList = getChildPages(root);
		return childrenList;
	}
	
	public String getRootPath(){
		return readHeaderRootPath;
	}
	

	public List<Page> getFirstLevelMenuItems(){
		List<Page> firstLevelMenuItems = new ArrayList<Page>();
		List<Page> mainMenuList = getChildrenList();
		
		
		for(int i=0; i<mainMenuList.size(); i++){
			Page mainMenuList = mainMenuList.get(i);
			
			if(currentPage.getPath().indexOf(mainMenuItem.getPath()) > -1){
				firstLevelMenuItems = getChildPages(mainMenuItem.getPath());
				break;
			}
		
		}
		return firstLevelMenuItems;
		
	}
	
	
	
	public String getFinalText(){
		String[] wordArray = navTitle.split("\\",-1);
		
		int wordMiddle = (wordArray.length / 2);
		for(int i=0; i<wordArray.length; i++)
		{
			finalText += wordArray[i];
			if(i== (wordMiddle - 1))
				 finalText += "<br/>";
			 else
				 finalText += " ";
			 
		}
		 return finalText;
		 
	}
	
}
		
		
		
		
	 


