/*
 * Copyright (c) 2016-2017, Guangshan (guangshan1992@qq.com) and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mxixm.fastboot.weixin.module.message;

import com.mxixm.fastboot.weixin.controller.invoker.WxApiInvokeSpi;
import com.mxixm.fastboot.weixin.module.web.WxRequest;

import java.util.Collection;

/**
 * FastBootWeixin WxMessageTemplate
 *
 * @author Guangshan
 * @date 2017/8/20 20:20
 * @since 0.1.2
 */
public class WxMessageTemplate {

    /**
     * 暂时不想加入客服系统，要加入的话可以写个EnableWxCustomer
     */
    private WxApiInvokeSpi wxApiInvokeSpi;

    private WxMessageProcesser wxMessageProcesser;

    public WxMessageTemplate(WxApiInvokeSpi wxApiInvokeSpi, WxMessageProcesser wxMessageProcesser) {
        this.wxApiInvokeSpi = wxApiInvokeSpi;
        this.wxMessageProcesser = wxMessageProcesser;
    }

    public void sendMessage(WxRequest wxRequest, WxMessage wxMessage) {
        this.sendMessage(wxMessageProcesser.process(wxRequest, wxMessage));
    }

    public void sendMessage(WxRequest wxRequest, String wxMessageContent) {
        this.sendMessage(wxRequest, WxMessage.Text.builder().content(wxMessageContent).build());
    }

    public void sendMessage(WxMessage wxMessage) {
        if (wxMessage.filter == null && wxMessage.toUsers == null) {
            this.wxApiInvokeSpi.sendUserMessage(wxMessage);
        } else {
            this.wxApiInvokeSpi.sendGroupMessage(wxMessage);
        }
    }

    public void sendMessage(String toUser, String wxMessageContent) {
        this.sendMessage(toUser, WxMessage.Text.builder().content(wxMessageContent).build());
    }

    public void sendMessage(String toUser, WxMessage wxMessage) {
        wxMessage.setToUser(toUser);
        this.sendMessage(wxMessage);
    }

    public void sendGroupMessage(WxMessage wxMessage) {
        wxMessage.filter = new WxMessage.Filter();
        wxMessage.toUsers = null;
        this.sendMessage(wxMessage);
    }

    public void sendGroupMessage(int tagId, WxMessage wxMessage) {
        wxMessage.filter = new WxMessage.Filter();
        wxMessage.filter.tagId = tagId;
        wxMessage.toUsers = null;
        this.sendMessage(wxMessage);
    }

    public void sendGroupMessage(Collection<String> toUsers, WxMessage wxMessage) {
        wxMessage.toUsers = toUsers;
        wxMessage.filter = null;
        this.sendMessage(wxMessage);
    }

}
