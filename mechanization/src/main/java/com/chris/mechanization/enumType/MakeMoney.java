package com.chris.mechanization.enumType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-07 22:01
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public enum MakeMoney {
    SYMBOL,
    COIN
}
