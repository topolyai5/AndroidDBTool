package com.topolyai.dbtool.example;

import android.content.ContentValues;

import com.topolyai.dbtool.AbstractCrudRepositoryBase;
import com.topolyai.dbtool.extractor.Extractor;

import lombok.Repository;

@Repository(type = TestDomain.class)
public class TestRepository extends AbstractCrudRepositoryBase<TestDomain> {

    public TestRepository() {
        super(TestDomain.class);
    }
}
