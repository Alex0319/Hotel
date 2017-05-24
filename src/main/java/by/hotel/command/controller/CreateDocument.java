package by.hotel.command.controller;

import by.hotel.bean.DocumentObject;
import by.hotel.command.exception.CommandException;
import by.hotel.factory.impl.DocumentBuilderMapper;
import by.hotel.service.documentservice.DocumentBuilderService;
import by.hotel.service.exception.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
@Controller
public class CreateDocument   {
    @ResponseBody
    @RequestMapping(value = "/create_doc", method = RequestMethod.GET, produces = "application/json")
    public Object execute(HttpServletRequest req, HttpServletResponse response) throws CommandException {
        Map<String, String[]> requestParams = req.getParameterMap();
        DocumentObject documentObject;
        try {
            DocumentBuilderMapper documentBuilderMapper = DocumentBuilderMapper.getInstance();
            DocumentBuilderService documentBuilderService = documentBuilderMapper.getDocumentBuilderService(requestParams.get("docname")[0]);
            documentObject = documentBuilderService.buildDocument(requestParams);
            sendDocument(response, documentObject);
        }catch (ServiceException | IOException e){
            throw new CommandException(e);
        }
        return documentObject;
    }

    private void sendDocument(HttpServletResponse response, DocumentObject documentObject) throws IOException{
        if(documentObject != null){
            response.setContentType(documentObject.getMimeType());
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", documentObject.getDocumentName()));
            response.getOutputStream().write(documentObject.getDocumentBytes());
            response.getOutputStream().close();
        }
    }
}