package com.yuaoq.yabiz.weixin.common.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.yuaoq.yabiz.weixin.common.message.XmlMessageHeader;

/**
 * Created by exizhai on 9/29/2015.
 */
@JacksonXmlRootElement(localName = "xml")
public class VoiceRequest extends XmlMessageHeader {

    @JsonProperty("MsgId")
    private String msgId;

    @JsonProperty("MediaId")
    @JacksonXmlCData
    private String mediaId;

    @JsonProperty("Format")
    @JacksonXmlCData
    private String format;

    @JsonProperty("Recognition")
    @JacksonXmlCData
    private String recognition;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }
}
