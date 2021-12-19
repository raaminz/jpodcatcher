package com.raminzare.jpodcatcher.internal;

import com.raminzare.jpodcatcher.PodcastReader;
import com.raminzare.jpodcatcher.PodcastReaderException;
import com.raminzare.jpodcatcher.model.Channel;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stax.StAXSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PodcastReaderStaxParserImpl implements PodcastReader {
    @Override
    public Channel loadRSS(String uri) throws PodcastReaderException {
        try {
           XMLStreamReader reader =
                   XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(uri));
           while(reader.hasNext()){
               var event = reader.next();
               if(event == XMLEvent.START_ELEMENT){
                   System.out.println(reader.getText());
               }
           }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
