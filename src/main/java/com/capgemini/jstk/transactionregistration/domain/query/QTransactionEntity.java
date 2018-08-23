package com.capgemini.jstk.transactionregistration.domain.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.capgemini.jstk.transactionregistration.domain.ProductEntity;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTransactionEntity is a Querydsl query type for TransactionEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTransactionEntity extends EntityPathBase<TransactionEntity> {

    private static final long serialVersionUID = 1895271720L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTransactionEntity transactionEntity = new QTransactionEntity("transactionEntity");

    public final com.capgemini.jstk.transactionregistration.domain.impl.QAbstractEntity _super = new com.capgemini.jstk.transactionregistration.domain.impl.QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> created = _super.created;

    public final QCustomerEntity customer;

    public final DateTimePath<java.util.Date> date = createDateTime("date", java.util.Date.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final CollectionPath<ProductEntity, QProductEntity> products = this.<ProductEntity, QProductEntity>createCollection("products", ProductEntity.class, QProductEntity.class, PathInits.DIRECT2);

    public final EnumPath<com.capgemini.jstk.transactionregistration.enums.TransactionStatus> status = createEnum("status", com.capgemini.jstk.transactionregistration.enums.TransactionStatus.class);

    //inherited
    public final DateTimePath<java.util.Date> updated = _super.updated;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QTransactionEntity(String variable) {
        this(TransactionEntity.class, forVariable(variable), INITS);
    }

    public QTransactionEntity(Path<? extends TransactionEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTransactionEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTransactionEntity(PathMetadata metadata, PathInits inits) {
        this(TransactionEntity.class, metadata, inits);
    }

    public QTransactionEntity(Class<? extends TransactionEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.customer = inits.isInitialized("customer") ? new QCustomerEntity(forProperty("customer")) : null;
    }

}

