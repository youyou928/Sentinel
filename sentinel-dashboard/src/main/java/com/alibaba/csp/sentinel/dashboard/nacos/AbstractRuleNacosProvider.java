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
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public abstract class AbstractRuleNacosProvider<T extends RuleEntity> implements DynamicRuleProvider<List<T>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosSentinelProperites properites;
    @Autowired
    private Converter<String, List<T>> converter;

    @Override
    public List<T> getRules(String appName) throws Exception {
        String dataId = appName.toLowerCase() + getDataIdPostfix();
        
        String rules = configService.getConfig(dataId, properites.getGroup(), properites.getTimeout());
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
    
    /**
     * 获取dataId后缀
     */
    public abstract String getDataIdPostfix();
}
