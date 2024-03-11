package com.aston.restservice.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

import static com.aston.restservice.util.Constants.APPLICATION_JSON;
import static com.aston.restservice.util.Constants.CHARACTER_ENCODING;

@Slf4j
public class ResponseSender {

    private ResponseSender() {
    }

    public static void sendResponse(HttpServletResponse resp, Integer status, Object body) {
        resp.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(APPLICATION_JSON);
        resp.setStatus(status);
        Writer writer;
        try {
            writer = resp.getWriter();
            writer.write(GetProvider.getObjectMapper().writeValueAsString(body));
            log.info("Send response with status : " + status + " , body : " + body);
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

}
