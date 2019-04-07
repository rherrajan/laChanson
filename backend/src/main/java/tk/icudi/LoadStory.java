package tk.icudi;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoadStory {

	@RequestMapping(value="/loadStory", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	String loadStory(HttpServletRequest request) throws IOException {

		String uuid = request.getParameter("uuid");
		System.out.println(" --- uuid " + uuid);
		return "holla_die_waldfee du " + uuid; 
//		return "holla_die_waldfee"; 
	}

	@ModelAttribute
	public void setVaryResponseHeader(HttpServletResponse response) {
	    response.setHeader("Access-Control-Allow-Origin", "*");	  
//	    response.setHeader("Access-Control-Request-Headers", "Origin, X-Requested-With, Content-Type, Accept");	  
	}   
		
}
