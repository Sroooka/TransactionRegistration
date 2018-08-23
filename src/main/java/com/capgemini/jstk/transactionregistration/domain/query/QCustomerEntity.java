package com.capgemini.jstk.transactionregistration.domain.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomerEntity is a Querydsl query type for CustomerEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomerEntity extends EntityPathBase<CustomerEntity> {

    private static final long serialVersionUID = 1163228666L;

    public static final QCustomerEntity customerEntity = new QCustomerEntity("customerEntity");

    public final com.capgemini.jstk.transactionregistration.domain.impl.QAbstractEntity _super = new com.capgemini.jstk.transactionregistration.domain.impl.QAbstractEntity(this);

    public final StringPath address = createString("address");

    public final DatePath<java.util.Date> birth = createDate("birth", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> created = _super.created;

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final StringPath surname = createString("surname");

    public final SetPath<TransactionEntity, QTransactionEntity> transactions = this.<TransactionEntity, QTransactionEntity>createSet("transactions", TransactionEntity.class, QTransactionEntity.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> updated = _super.updated;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QCustomerEntity(String variable) {
        super(CustomerEntity.class, forVariable(variable));
    }

    public QCustomerEntity(Path<? extends CustomerEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomerEntity(PathMetadata metadata) {
        super(CustomerEntity.class, metadata);
    }

}

