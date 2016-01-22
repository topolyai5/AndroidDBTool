package com.topolyai.dbtool.example;

import android.content.ContentValues;

import com.topolyai.dbtool.Entity;
import com.topolyai.dbtool.Identifiable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

@Getter
@Setter
@Builder
public class TestDomain implements Entity {
    private Long id;
    private String name;
    private String type;
}
