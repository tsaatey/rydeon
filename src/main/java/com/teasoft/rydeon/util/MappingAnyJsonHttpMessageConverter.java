/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 *
 * @author TOSHIBA
 */
public class MappingAnyJsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {
 
  public MappingAnyJsonHttpMessageConverter() {
    List list = new ArrayList(1);
    list.add(MediaType.ALL);
    list.add(new MyMediaType("1"));
    this.setSupportedMediaTypes(list);
//    this.setDefaultCharset(Charset.forName("1; charset=utf-8"));
  }
}