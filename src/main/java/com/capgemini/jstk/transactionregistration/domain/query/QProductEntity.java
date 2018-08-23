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
 * QProductEntity is a Querydsl query type for ProductEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProductEntity extends EntityPathBase<ProductEntity> {

    private static final long serialVersionUID = 210006521L;

    public static final QProductEntity productEntity = new QProductEntity("productEntity");

    public final com.capgemini.jstk.transactionregistration.domain.impl.QAbstractEntity _super = new com.capgemini.jstk.transactionregistration.domain.impl.QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> created = _super.created;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> marginPercent = createNumber("marginPercent", Integer.class);

    public final StringPath name = createString("name");

    public final CollectionPath<TransactionEntity, QTransactionEntity> transactions = this.<TransactionEntity, QTransactionEntity>createCollection("transactions", TransactionEntity.class, QTransactionEntity.class, PathInits.DIRECT2);

    public final NumberPath<Double> unitPrice = createNumber("unitPrice", Double.class);

    //inherited
    public final DateTimePath<java.util.Date> updated = _super.updated;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final NumberPath<Double> weight = createNumber("weight", Double.class);

    public QProductEntity(String variable) {
        super(ProductEntity.class, forVariable(variable));
    }

    public QProductEntity(Path<? extends ProductEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductEntity(PathMetadata metadata) {
        super(ProductEntity.class, metadata);
    }

}

