package com.pivotallabs.pivottexts;

import com.pivotallabs.pivottexts.pivottextsservice.PivotTextsService;
import com.pivotallabs.pivottexts.pivottextsservice.TextMessage;
import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TwilioTextsController {

    @Autowired
    PivotTextsService pivotTextsService;

    @RequestMapping(value = "/twilio-texts", method = RequestMethod.POST, produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<String> receiveText(@ModelAttribute("From") String from, @ModelAttribute("Body") String body) throws IOException, TwiMLException {
        TwiMLResponse twiMLResponse = new TwiMLResponse();

        if (StringUtils.isEmpty(from) || StringUtils.isEmpty(body)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!pivotTextsService.saveText(new TextMessage(from, body))) {
            twiMLResponse.append(new Message("We could not display your message. Is your number on Pivots?"));
        }

        return new ResponseEntity<>(twiMLResponse.toXML(), HttpStatus.OK);
    }
}
