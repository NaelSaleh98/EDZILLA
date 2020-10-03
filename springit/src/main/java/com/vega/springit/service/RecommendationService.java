package com.vega.springit.service;

import org.springframework.stereotype.Service;

@Service
public class RecommendationService {


    public float personCorrelation(short [] userA , short [] userB , int length){
        int count = 0;
        for (int i = 0; i < length; i++) {
            if (userA[i] == userB[i]) count++;
        }
        return count/(float)length;
    }
}
