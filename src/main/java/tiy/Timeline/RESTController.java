package tiy.Timeline;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * Created by fenji on 10/10/2016.
 */
public class RESTController {

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String returnJson(HttpServletRequest request) throws IOException {
        String jsonBody;
        jsonBody = request.getInputStream().toString();
        System.out.println(jsonBody);

        return jsonBody;
    }
}
