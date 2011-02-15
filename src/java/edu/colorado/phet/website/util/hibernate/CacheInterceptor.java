/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.util.hibernate;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

public class CacheInterceptor extends EmptyInterceptor {

    private static final Logger logger = Logger.getLogger( CacheInterceptor.class.getName() );

    @Override
    public void onDelete( Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types ) {
        logger.debug( "onDelete " + entity.getClass().getName() );
        super.onDelete( entity, id, state, propertyNames, types );
    }

    @Override
    public boolean onSave( Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types ) {
        logger.debug( "onSave " + entity.getClass().getName() );
        return super.onSave( entity, id, state, propertyNames, types );
    }

    @Override
    public void onCollectionRemove( Object collection, Serializable key ) throws CallbackException {
        super.onCollectionRemove( collection, key );
    }

    @Override
    public void onCollectionRecreate( Object collection, Serializable key ) throws CallbackException {
        super.onCollectionRecreate( collection, key );
    }

    @Override
    public void onCollectionUpdate( Object collection, Serializable key ) throws CallbackException {
        super.onCollectionUpdate( collection, key );
    }

    @Override
    public void afterTransactionCompletion( Transaction tx ) {
        super.afterTransactionCompletion( tx );
    }
}
