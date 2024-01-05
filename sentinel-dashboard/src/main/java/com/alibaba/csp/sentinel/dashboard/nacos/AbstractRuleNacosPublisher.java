/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
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
package com.alibaba.csp.sentinel.dashboard.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public abstract class AbstractRuleNacosPublisher<T extends RuleEntity> implements DynamicRulePublisher<List<T>> {

    private Logger logger = LoggerFactory.getLogger(AbstractRuleNacosPublisher.class);
    
    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosSentinelProperites properites;
    @Autowired
    private Converter<List<T>, String> converter;

    @Override
    public void publish(String app, List<T> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        
        String dataId = app.toLowerCase() + getDataIdPostfix();
        
        boolean result = configService.publishConfig(dataId, properites.getGroup(), converter.convert(rules), ConfigType.JSON.getType());
        
        if (!result) {
            throw new RuntimeException("更新规则到Nacos失败，详细请查看服务端日志");
        }
    }
    
    /**
     * 获取dataId后缀
     */
    public abstract String getDataIdPostfix();
}
