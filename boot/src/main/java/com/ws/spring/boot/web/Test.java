package com.ws.spring.boot.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.AbstractDocument;

/**
 * <p>
 * 类名称：Test
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-11-07 14:18
 * </p>
 * <p>
 * 修改人：
 * </p>
 * <p>
 * 修改时间：
 * </p>
 * <p>
 * 修改备注：
 * </p>
 * <p>
 * Copyright (c) 版权所有
 * </p>
 *
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/city")
public class Test {

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String test(@PathVariable("id") Long id) {

        return String.format("{\"value\":%s}", id);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String save(@RequestBody Entiry entiry) {

        return String.format("{\"value\":%s}", 111);
    }

    class Entiry {
        private String name;
        private String value;

        public Entiry(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
